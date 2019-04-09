package com.cxcy.zjb.springboot.Vo;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

/**
 * 前端显示用户企业信息
 * Created by LINWENHAO on 2019/3/10.
 */
@Data
public class UserCompanyVo {
    private Long id;

    private String name;  //用户真实姓名

    private String email;   //用户邮箱

    private String telephone;

    private String username; // 用户账号，用户登录时的唯一标识

    private String avatar ; // 默认头像图片地址

    private Integer state;  //账号状态，0为未认证，1为已认证，2为审核中，3为认证失败

    private Integer style;  //账号类型，0为未确定身份，1为学生，2为老师，3为企业，4为管理员

    private Integer sex = 1;  //性别，0为女生，1为男生；

    private Integer isUse = 1;  //是否可用,1可用，0不可用

    private Long cId;  //公司id

    private String companyName; //公司名称

    private String number; //企业社会代码

    private String boss; //公司法定代表人

    private String license; //营业执照

    private String type; //公司类型

    private String contacts; //联系人

    private String phone; //联系电话

    public UserCompanyVo(Long id, String name, String email, String telephone, String username, String avatar, Integer state, Integer style, String companyName, String number, String boss, String license, String type, String contacts, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.username = username;
        this.avatar = avatar;
        this.state = state;
        this.style = style;
        this.companyName = companyName;
        this.number = number;
        this.boss = boss;
        this.license = license;
        this.type = type;
        this.contacts = contacts;
        this.phone = phone;
    }

    public UserCompanyVo(Long id, String name, String email, String telephone, String username, String avatar, Integer state, Integer style, Integer sex, Integer isUse, Long cId, String companyName, String number, String boss, String license, String type, String contacts, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.username = username;
        this.avatar = avatar;
        this.state = state;
        this.style = style;
        this.sex = sex;
        this.isUse = isUse;
        this.cId = cId;
        this.companyName = companyName;
        this.number = number;
        this.boss = boss;
        this.license = license;
        this.type = type;
        this.contacts = contacts;
        this.phone = phone;
    }
}
