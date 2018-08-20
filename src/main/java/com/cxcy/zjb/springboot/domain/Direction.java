package com.cxcy.zjb.springboot.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Direction 作品方向实体
 */
@Entity
@Data
public class Direction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long d_id;         //主键，方向的唯一标识

    @NotEmpty(message = "作品方向名称不能为空")
    @Size(min = 1)         //名称字数最少为2
    @Column(nullable = false)      //映射为字段，值不能为空
    private String d_name;

    protected Direction(){       //jpa规范要求，无参构造函数，设为protected防止直接使用

    }

}
