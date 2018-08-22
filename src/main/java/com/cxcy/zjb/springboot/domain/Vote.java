package com.cxcy.zjb.springboot.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Vote 点赞实体
 */
@Entity
@Data
public class Vote implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     //自增长策略
    private Long vId;                  //主键

    @Column(name="user_id")
    private Long user;                  //与User一对一关系

    @Column(nullable = false) // 映射为字段，值不能为空
    @org.hibernate.annotations.CreationTimestamp  // 由数据库自动创建时间
    private Timestamp vCreateTime;

    protected Vote(){                   //无参构造函数
    }
}

