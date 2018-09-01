package com.cxcy.zjb.springboot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Catagory 作品分类
 */
@Entity
@Data
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Catagorys implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //自增长策略
    private Long cId;     //作品分类的唯一标识


    @Column(name="direction_id")
    private Long direction;     //作品方向的主键id为作品分类的外键，它们为一对一的关系

    @NotEmpty(message = "作品分类名称不能为空")
    @Size(min = 2)
    @Column(nullable = false)      //映射为字段，值不能为空
    private String caName;

    public Catagorys(Long direction, String caName) {
        this.direction = direction;
        this.caName = caName;
    }
}
