package com.cxcy.zjb.springboot.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Created by LINWENHAO on 2018/8/12.
 */
@Entity // 实体
@Data   //自动生成get和set方法
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id //主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    private Long id;//比赛项目的唯一标识

    @NotEmpty(message = "比赛项目名称不能为空")
    @Size(min=1, max=30)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String name;       //项目名称

    @Column(nullable = false) // 映射为字段，值不能为空
    private Integer pNumber=1;   //报名人数，默认为单人

    @Column
    private Long matchId; //比赛id

    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinTable(name = "event_leader", joinColumns = @JoinColumn(name = "e_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> userList;   //项目负责人



}
