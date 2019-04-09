package com.cxcy.zjb.springboot.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 公司信息实体类
 * Created by LINWENHAO on 2018/8/14.
 */
@Data
@Entity
public class Company implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id  // 主键
    @GeneratedValue(strategy= GenerationType.IDENTITY) // 自增长策略
    private Long id;

    @NotEmpty(message = "公司名称不能为空")
    @Size(min=2, max=30)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String companyName; //公司名称

    @NotEmpty(message = "企业社会代码不能为空")
    @Column(nullable = false) // 映射为字段，值不能为空
    private String number; //企业社会代码

    @NotEmpty(message = "公司法定代表人不能为空")
    @Column(nullable = false) // 映射为字段，值不能为空
    private String boss; //公司法定代表人

    @NotEmpty(message = "营业执照不能为空")
    @Column(nullable = false) // 映射为字段，值不能为空
    @Size(min=2, max=100)
    private String license; //营业执照

    @NotEmpty(message = "公司类型不能为空")
    @Column(nullable = false) // 映射为字段，值不能为空
    private String type; //公司类型

    @NotEmpty(message = "联系人不能为空")
    @Column(nullable = false) // 映射为字段，值不能为空
    private String contacts; //联系人

    @NotEmpty(message = "联系电话不能为空")
    @Column(nullable = false) // 映射为字段，值不能为空
    private String phone; //联系电话


}
