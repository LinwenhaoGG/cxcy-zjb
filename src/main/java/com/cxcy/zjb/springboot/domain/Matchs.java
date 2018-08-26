package com.cxcy.zjb.springboot.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 比赛实体类
 * Created by LINWENHAO on 2018/8/12.
 */
@Entity // 实体
@Data   //自动生成get和set方法
public class Matchs implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id //主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    private Long id;//比赛的唯一标识

    @NotEmpty(message = "比赛名称不能为空")
    @Size(min=2, max=30)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String name;

    @Lob  // 大对象，映射 MySQL 的 Long Text 类型
    @Basic(fetch=FetchType.LAZY) // 懒加载
    @NotEmpty(message = "内容不能为空")
    @Size(min=2)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String content;  //内容

    @Lob  // 大对象，映射 MySQL 的 Long Text 类型
    @Basic(fetch=FetchType.LAZY) // 懒加载
    @NotEmpty(message = "内容不能为空")
    @Size(min=2)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String htmlContent; // 将 md 转为 html

    @Column(nullable = false) // 映射为字段，值不能为空
    @CreationTimestamp  // 由数据库自动创建时间
    private Timestamp createTime; //创建时间

    @Column // 映射为字段
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date startTime;  //比赛开始时间

    @Column // 映射为字段
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date overTime;   //比赛结束时间

    @Column // 映射为字段
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date lastsigntime;  //截止报名时间

    @Column // 映射为字段
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date lastsubmittime;   //截止提交时间

    @Column(name="user_id")
    private Long user;   //发布人

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "match_event", joinColumns = @JoinColumn(name = "match_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"))
    private List<Event> eventList;   //比赛项目

    /**
     * 添加比赛项目
     * @param event
     */
    public void addEvent(Event event) {
        this.eventList.add(event);
    }
    /**
     * 删除比赛项目
     * @param eventId
     */
    public void removeEvent(Long eventId) {
        for (int index=0; index < this.eventList.size(); index ++ ) {
            if (eventList.get(index).getId().equals(eventId)) {
                this.eventList.remove(index);
                break;
            }
        }

    }
}
