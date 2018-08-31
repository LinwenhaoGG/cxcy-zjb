package com.cxcy.zjb.springboot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 资讯信息类实体
 * Created by LINWENHAO on 2018/8/12.
 */
@Entity // 实体
@Data   //自动生成get和set方法
public class Information implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id //主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    private Long id;//资讯分类的唯一标识

    @NotEmpty(message = "标题不能为空")
    @Size(min=2, max=50)
    @Column(nullable = false, length = 50) // 映射为字段，值不能为空
    private String title;  //标题

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

    @NotEmpty(message = "作者名称不能为空")
    @Size(min=2, max=30)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String author;

    @Column(name="readSize")
    private Integer readSize = 0; // 访问量、阅读量

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)//不级联删除、被访问时才加载
    @JoinColumn(name="user_id")
    private User user;   //发布人

    @Column(nullable = false) // 映射为字段，值不能为空
    @org.hibernate.annotations.CreationTimestamp  // 由数据库自动创建时间
    private Timestamp createTime;

    @OneToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id")
    private InformationCategory informationCategory; //资讯所属分类


    public Information(String title,String content,String author){
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
        this.htmlContent = content;
    }
}
