package com.cxcy.zjb.springboot.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Growth 成长体系实体
 */
@Entity
@Data
public class Growth implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY) // 自增长策略
    private Long id;

    @Column(name="s_id")
    private Long user;                      //外键也是主键

    @Column(name="g_vote")
    private Integer gVote = 0;                 //总点赞数

    @Column(name="g_comment")
    private Integer gComment = 0;              //总评论数

    @Column(name="g_readSize")
    private Integer gReadSize = 0;              //总查看数

    @Column(name="g_integration")
    private Integer gIntegration = 0;          //总积分数

    @Column(name="g_rank")
    private Integer gRank = 0;                 //排名数

    //需要手动创建growth
    public Growth(){

    }
}
