package com.cxcy.zjb.springboot.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 *资讯分类实体类
 * Created by LINWENHAO on 2018/8/12.
 */
@Entity // 实体
@Data   //自动生成get和set方法
public class InformationCategory implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id //主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    private Long id;//资讯分类的唯一标识

    @NotEmpty(message = "分类名称不能为空")
    @Size(min=2, max=30)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String name;

    protected InformationCategory() {
    }

    public InformationCategory(Long id,String name){
        this.id = id;
        this.name = name;
    }

}
