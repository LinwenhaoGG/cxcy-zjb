package com.cxcy.zjb.springboot.domain;

import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 浏览记录表
 */
@Entity
@Data
public class Browse {
    private static final long serialVersionUID = 1L;

    @Id //主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    private Long bId;//浏览的唯一标识

    @Column(name="user_id", nullable = false,unique = true)
    private Long user;                  //浏览者

    @Column(name="catagory_id", nullable = false)
    private Long catagory;          //浏览类型，即作品类型

    @Column(name = "b_browse_time", nullable = false) // 映射为字段，值不能为空
    private String browseTime;     //最后浏览时间


}
