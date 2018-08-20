package com.cxcy.zjb.springboot.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 教师信息实体
 * Created by LINWENHAO on 2018/8/14.
 */
@Entity
@Data
public class Teacher implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id  // 主键
    @GeneratedValue(strategy= GenerationType.IDENTITY) // 自增长策略
    private Long id;

    @NotEmpty(message = "学院/办公室信息不能为空")
    @Size(min=2, max=30)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String college; //学院或者办公室

    @NotEmpty(message = "在职学校信息不能为空")
    @Size(min=2, max=50)
    private String school; //在职学校

    @NotEmpty(message = "专业信息不能为空")
    @Size(min=2, max=50)
    private String specially; //负责哪个专业的导师

    @Column(name="number")
    private Integer number;  // 教师工号

    @NotEmpty(message = "民族信息不能为空")
    @Column(nullable = false) // 映射为字段，值不能为空
    private String nation; //民族

    @NotEmpty(message = "职位信息不能为空")
    @Column(nullable = false) // 映射为字段，值不能为空
    private String position; //职位

    @NotEmpty(message = "政治面貌信息不能为空")
    @Column(nullable = false) // 映射为字段，值不能为空
    private String politicsstatus; //政治面貌

    @Lob  // 大对象，映射 MySQL 的 Long Text 类型
    @Basic(fetch=FetchType.LAZY) // 懒加载
    @NotEmpty(message = "个人简介不能为空")
    @Size(min=2)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String credential;           //目前研究方向和指导学生要求
}
