package com.cxcy.zjb.springboot.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * 权限实体
 *
 * Created by LINWENHAO on 2018/8/6.
 */
@Entity
@Data
public class Authority implements GrantedAuthority {


    private static final long serialVersionUID = 1L;

    @Id//主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增
    private Long id;  //标识

    @Column(nullable = false) //映射为字段，不能为空
    private String name;  //角色名称

    //实现角色
    @Override
    public String getAuthority() {
        return name;
    }
}
