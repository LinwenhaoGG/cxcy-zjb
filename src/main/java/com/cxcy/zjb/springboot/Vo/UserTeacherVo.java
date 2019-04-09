package com.cxcy.zjb.springboot.Vo;

import lombok.Data;

/**
 * 前端显示用户教师信息
 * Created by LINWENHAO on 2019/3/10.
 */
@Data
public class UserTeacherVo {
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

    private Long tId;  //教师id

    private String college; //学院或者办公室

    private String school; //在职学校

    private String specially; //负责哪个专业的导师

    private Long number;  // 教师工号

    private String nation; //民族

    private String position; //职位

    private String politicsstatus; //政治面貌

    private String credential;           //目前研究方向和指导学生要求

    public UserTeacherVo(Long id, String name, String email, String telephone, String username, String avatar, Integer state, Integer style, String college, String school, String specially, Long number, String nation, String position, String politicsstatus, String credential) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.username = username;
        this.avatar = avatar;
        this.state = state;
        this.style = style;
        this.college = college;
        this.school = school;
        this.specially = specially;
        this.number = number;
        this.nation = nation;
        this.position = position;
        this.politicsstatus = politicsstatus;
        this.credential = credential;
    }

    public UserTeacherVo(Long id, String name, String email, String telephone, String username, String avatar, Integer state, Integer style, Integer sex, Integer isUse, Long tId, String college, String school, String specially, Long number, String nation, String position, String politicsstatus, String credential) {
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
        this.tId = tId;
        this.college = college;
        this.school = school;
        this.specially = specially;
        this.number = number;
        this.nation = nation;
        this.position = position;
        this.politicsstatus = politicsstatus;
        this.credential = credential;
    }
}
