package com.cxcy.zjb.springboot.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户实体类
 * Created by LINWENHAO on 2018/8/12.
 */
@Entity
@Data
public class User implements Serializable,UserDetails {  //
    private static final long serialVersionUID = 1L;

    @Id  // 主键
    @GeneratedValue(strategy= GenerationType.IDENTITY) // 自增长策略
    private Long id;

    //注册页面中，姓名暂时可为空，在认证页面再填写
    @Size(min=2, max=20)
    @Column(nullable = true, length = 20) // 映射为字段，值不能为空
    private String name;  //用户真实姓名

    //邮箱可为空
    @Size(max=50)
    @Email(message= "邮箱格式不对" )
    @Column(nullable = true, length = 50, unique = true)
    private String email;   //用户邮箱

    //手机
    @NotEmpty(message = "手机号码不能为空")
    @Length(min = 11,max = 11,message = "手机号码长度是11位的")
    private String telephone;

    @NotEmpty(message = "账号不能为空")
    @Size(min=3, max=20)
    @Column(nullable = false, length = 20, unique = true)
    private String username; // 用户账号，用户登录时的唯一标识

    @NotEmpty(message = "密码不能为空")
    @Size(max=100)
    @Column(length = 100)
    private String password; // 登录时密码

    @Column(length = 200)
    private String avatar = "http://localhost:8080/file/upload/b6a273f470b14800bdd1ac29112378a3.jpg"; // 默认头像图片地址

    @Column(name = "sex", nullable = false, columnDefinition = "tinyint(2)")
    private Integer sex = 1;  //性别，0为女生，1为男生；

    @Column(name = "state", nullable = false, columnDefinition = "tinyint(2)")
    private Integer state=0;  //账号状态，0为未认证，1为已认证，2为审核中，3为认证失败

    @Column(name = "style", nullable = false, columnDefinition = "tinyint(2)")
    private Integer style=0;  //账号类型，0为未确定身份，1为学生，2为老师，3为企业，4为管理员

    @Column(name = "is_use", nullable = false, columnDefinition = "tinyint(2)")
    private Integer isUse = 1;  //是否可用,1可用，0不可用

    @Column(name="s_id")
    private Long student;   //认证的学生

    @Column(name="c_id")
    private Long company;   //认证的企业

    @Column(name="t_id")
    private Long teacher;   //认证的老师

    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private List<Authority> authorities;  //用户的角色表

    //该构造方法public，需要直接使用该方法区接受前端传来的数据
    public User() { // JPA 的规范要求无参构造函数；设为 protected 防止直接使用
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //  需将 List<Authority> 转成 List<SimpleGrantedAuthority>，否则前端拿不到角色列表名称
        List<SimpleGrantedAuthority> simpleAuthorities = new ArrayList<>();
        for(GrantedAuthority authority : this.authorities){
            simpleAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
        return simpleAuthorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
