package com.cxcy.zjb.springboot.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * 学生基本信息表
 * Created by LINWENHAO on 2018/8/14.
 */
@Entity
@Data
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id  // 主键
    @GeneratedValue(strategy= GenerationType.IDENTITY) // 自增长策略
    private Long id;

    @NotEmpty(message = "年级信息不能为空")
    @Column(name = "edu", nullable = false, columnDefinition = "tinyint(2)")
    private Integer edu;//年级

    @NotEmpty(message = "学院信息不能为空")
    @Size(min=2, max=30)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String college; //学院

    @NotEmpty(message = "专业班级不能为空")
    @Size(min=2, max=50)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String classes; //专业班级

    @Column(name="number")
    private Integer number;  // 学号

    @NotEmpty(message = "民族信息不能为空")
    @Column(nullable = false) // 映射为字段，值不能为空
    private String nation; //民族

    @NotEmpty(message = "政治面貌信息不能为空")
    @Column(nullable = false) // 映射为字段，值不能为空
    private String politicsstatus; //政治面貌

    @Lob  // 大对象，映射 MySQL 的 Long Text 类型
    @Basic(fetch=FetchType.LAZY) // 懒加载
    @NotEmpty(message = "个人简介不能为空")
    @Size(min=2)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String credential;           //个人简介


    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="g_id")
    private Long gId;
}
