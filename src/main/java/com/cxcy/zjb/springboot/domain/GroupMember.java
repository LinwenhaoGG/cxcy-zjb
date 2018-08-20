package com.cxcy.zjb.springboot.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 小组成员
 * Created by LINWENHAO on 2018/8/12.
 */
@Entity // 实体
@Data   //自动生成get和set方法
public class GroupMember implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id //主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    private Long id;//成员的的唯一标识

    @NotEmpty(message = "姓名不能为空")
    @Size(min=2, max=30)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String name; //姓名

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

    @Column(name="phone")
    private String phone ;  // 手机号码

    @Column(name = "style", nullable = false, columnDefinition = "tinyint(2)")
    private Integer style=0; //标识是否队长，默认为队员
}
