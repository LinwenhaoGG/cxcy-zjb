package com.cxcy.zjb.springboot.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Production 作品实体
 */
@Entity
@Data
public class Production implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long p_id;                  //作品主键

    @NotEmpty(message = "作品标题不能为空")
    @Size(min=2, max=50)
    @Column(nullable = false, length = 50) // 映射为字段，值不能为空
    private String p_title;             //作品标题

    @Lob  // 大对象，映射 MySQL 的 Long Text 类型
    @Basic(fetch=FetchType.LAZY) // 懒加载
    @NotEmpty(message = "内容不能为空")
    @Size(min=2)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String p_content;           //作品内容

    @Lob  // 大对象，映射 MySQL 的 Long Text 类型
    @Basic(fetch=FetchType.LAZY) // 懒加载
    @NotEmpty(message = "内容不能为空")
    @Size(min=2)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String htmlContent; // 将 md 转为 html

    @Column(length = 100)
    private String p_video;             //作品视频存储路径

    @NotEmpty(message = "作品类别不能为空")
    @Column(name = "p_sort", nullable = false, columnDefinition = "tinyint(2)")
    private Integer p_sort=1;             //作品类别，0代表创意类，1代表实践类

    @Column(nullable = false) // 映射为字段，值不能为空
    @org.hibernate.annotations.CreationTimestamp  // 由数据库自动创建时间
    private Timestamp p_uploadTime;     //作品上传时间

    @Column(name = "p_check", nullable = false, columnDefinition = "tinyint(2)")
    private Integer p_check=1;            //审核情况标识，0表示已审核，1表示未审核，2表示审核未通过

    @Column(name="voteSize")
    private Integer e_voteSize = 0;     //点赞量

    @Column(name="commentSize")
    private Integer commentSize = 0;    //评论量

    @Column(name="readSize")
    private Integer readSize = 0;       //访问、阅读量

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="catagory_id")
    private Catagorys catagorys;          //作品类型,ca_id作为外键

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "production_comment", joinColumns = @JoinColumn(name = "prod_id", referencedColumnName = "p_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"))
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "production_vote", joinColumns = @JoinColumn(name = "prod_id", referencedColumnName = "p_id"),
            inverseJoinColumns = @JoinColumn(name = "vote_id", referencedColumnName = "v_id"))
    private List<Vote> votes;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="u_id")
    private User user;

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
    public boolean addVote(Vote vote) {
        boolean isExist = false;
        // 判断重复
        for (int index=0; index < this.votes.size(); index ++ ) {
            if (this.votes.get(index).getUser().getId().equals(vote.getUser().getId()) ) {
                isExist = true;
                break;
            }
        }

        if (!isExist) {
            this.votes.add(vote);
            this.e_voteSize= this.votes.size();
        }

        return isExist;
    }
    /**
     * 取消点赞
     * @param voteId
     */
    public void removeVote(Long voteId) {
        for (int index=0; index < this.votes.size(); index ++ ) {
            if (this.votes.get(index).getV_id() .equals(voteId) ) {
                this.votes.remove(index);
                break;
            }
        }
        this.e_voteSize = this.votes.size();
    }
}
