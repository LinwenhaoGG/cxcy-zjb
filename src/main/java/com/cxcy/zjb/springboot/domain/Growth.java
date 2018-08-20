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
    private Long id;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="s_id")
    private User user;                      //外键也是主键

    @Column(name="g_vote")
    private Integer g_vote = 0;                 //总点赞数

    @Column(name="g_comment")
    private Integer g_comment = 0;              //总评论数

    @Column(name="g_readSize")
    private Integer g_readSize = 0;              //总评论数

    @Column(name="g_integration")
    private Integer g_integration = 0;          //总积分数

    @Column(name="g_rank")
    private Integer g_rank = 0;                 //排名数

    protected Growth(){

    }
}
