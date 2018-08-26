package com.cxcy.zjb.springboot.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 比赛小组实体
 * Created by LINWENHAO on 2018/8/12.
 */
@Entity // 实体
@Data   //自动生成get和set方法
public class MatchGroup implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id //主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    private Long id;//小组的唯一标识

    @Column(length = 30) // 映射为字段，值不能为空
    private String name;  //小组名称

    @Column(length = 100)
    private String docAddress;  //小组doc文档地址

    @Column(length = 100)
    private String mvAddress;  //小组视频文件地址

    @Column(name = "achievement", columnDefinition = "decimal(3,2)")
    private double achievement=0.00;//成绩

    @Column(nullable = false) // 映射为字段，值不能为空
    @org.hibernate.annotations.CreationTimestamp  // 由数据库自动创建时间
    private Timestamp signtime; //报名时间

    @Column(name="event_id")
    private Long event;   //参加的项目

    @Column(name="user_id")
    private Long user;   //报名参加的用户id

    @OneToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinTable(name = "group_people", joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "number_id", referencedColumnName = "id"))
    private List<GroupMember> groupMemberList;   //小组成员
}
