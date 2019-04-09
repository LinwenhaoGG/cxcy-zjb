package com.cxcy.zjb.springboot.Vo;

import lombok.Data;

/**
 * 前端显示用户学生信息
 * Created by LINWENHAO on 2019/3/8.
 */
@Data
public class UserStudentVo {

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

    private Long sId;  //学生id

    private String edu;//年级

    private String college; //学院

    private String classes; //专业班级

    private Long number;  // 学号

    private String nation; //民族

    private String politicsstatus; //政治面貌

    private String credential;           //个人简介


    public UserStudentVo(Long id, String name, String email, String telephone, String username, String avatar, Integer state, Integer style, Integer isUse, String edu, String college, String classes, Long number, String nation, String politicsstatus, String credential) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.username = username;
        this.avatar = avatar;
        this.state = state;
        this.style = style;
        this.isUse = isUse;
        this.edu = edu;
        this.college = college;
        this.classes = classes;
        this.number = number;
        this.nation = nation;
        this.politicsstatus = politicsstatus;
        this.credential = credential;
    }

    public UserStudentVo(Long id, String name, String email, String telephone, String username, String avatar, Integer state, Integer style, Integer sex, Integer isUse, Long sId, String edu, String college, String classes, Long number, String nation, String politicsstatus, String credential) {
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
        this.sId = sId;
        this.edu = edu;
        this.college = college;
        this.classes = classes;
        this.number = number;
        this.nation = nation;
        this.politicsstatus = politicsstatus;
        this.credential = credential;
    }
}
