package com.cxcy.zjb.springboot.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Production 作品实体
 */
@Entity
@Document(indexName = "production", type = "production")
@Data
public class Production implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pId;                  //作品主键

    @NotEmpty(message = "作品标题不能为空")
    @Size(min=2, max=50)
    @Column(nullable = false, length = 50) // 映射为字段，值不能为空
    private String pTitle;             //作品标题

    @NotEmpty(message = "作品概要不能为空")
    @Column(nullable = false,length = 100)
    private String pSummary;            //作品简介

    @NotEmpty(message = "内容不能为空")
    @Column(nullable = false) // 映射为字段，值不能为空
    private String pContent;           //作品内容文件链接

    @Column(length = 100)
    private String pVideo;             //作品视频存储路径

    @NotNull(message = "作品类别不能为空")
    @Column(name = "p_sort", nullable = false, columnDefinition = "tinyint(2)")
    private Integer pSort=1;             //作品类别，0代表创意类，1代表实践类

    @Column(name = "p_upload_time",nullable = false) // 映射为字段，值不能为空
    @CreationTimestamp  // 由数据库自动创建时间
    private Timestamp puploadTime;     //作品上传时间

    @Column(name = "p_check", nullable = false, columnDefinition = "tinyint(2)")
    private Integer pCheck=1;            //审核情况标识，0表示已审核，1表示未审核，2表示审核未通过

    @Column(name="voteSize")
    private Integer eVoteSize = 0;     //点赞量

    @Column(name="commentSize")
    private Integer commentSize = 0;    //评论量

    @Column(name="readSize")
    private Integer readSize = 0;       //访问、阅读量

    @Column(name="catagory_id")
    private Long catagorys;          //作品类型,ca_id作为外键

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "production_comment", joinColumns = @JoinColumn(name = "prod_id", referencedColumnName = "pId"),
            inverseJoinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"))
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "production_vote", joinColumns = @JoinColumn(name = "prod_id", referencedColumnName = "pId"),
            inverseJoinColumns = @JoinColumn(name = "vote_id", referencedColumnName = "vId"))
    private List<Vote> votes;

    @Column(name="u_id")
    private Long user;

//    添加评论的同时要顺便修改
    public void setComments(List<Comment> comments) {
        this.comments = comments;
        this.commentSize = this.comments.size();
    }

//    添加点赞的同时要修改数量
    public void setVotes(List<Vote> votes) {
        this.votes = votes;
        this.eVoteSize = this.votes.size();
    }

    /**
     * 添加评论
     * @param comment
     */
    public void addComment(Comment comment) {
        this.comments.add(comment);
        this.commentSize = this.comments.size();
    }
    /**
     * 删除评论
     * @param commentId
     */
    public void removeComment(Long commentId) {
        for (int index=0; index < this.comments.size(); index ++ ) {
            if (comments.get(index).getId().equals(commentId)) {
                this.comments.remove(index);
                break;
            }
        }

        this.commentSize = this.comments.size();
    }

    /**
     * 点赞
     * @param vote
     * @return
     */
    public void addVote(Vote vote) {
        if (this.votes == null) {
            this.votes = new ArrayList<>();
        }
        this.votes.add(vote);
        this.eVoteSize = this.votes.size();
    }


    /**
     * 取消点赞
     * @param voteId
     */
    public void removeVote(Long voteId) {
        for (int index=0; index < this.votes.size(); index ++ ) {
            if (this.votes.get(index).getVId() .equals(voteId) ) {
                this.votes.remove(index);
                break;
            }
        }
        this.eVoteSize = this.votes.size();
    }

    @Override
    public String toString() {
        return "Production{" +
                "pId=" + pId +
                ", pTitle='" + pTitle + '\'' +
                ", pContent='" + pContent + '\'' +
                ", pVideo='" + pVideo + '\'' +
                ", pSort=" + pSort +
                ", pUploadTime=" + puploadTime +
                ", pCheck=" + pCheck +
                ", eVoteSize=" + eVoteSize +
                ", commentSize=" + commentSize +
                ", readSize=" + readSize +
                ", catagorys=" + catagorys +
                ", comments=" + comments +
                ", votes=" + votes +
                ", user=" + user +
                '}';
    }
}
