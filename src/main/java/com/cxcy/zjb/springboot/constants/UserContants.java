package com.cxcy.zjb.springboot.constants;

/**
 * 学生常量
 * Created by LINWENHAO on 2019/3/8.
 */
public class UserContants {
    /**
     * 用户类型
     */
    public static Integer STYLE_STUDENT = 1;  //学生类型
    public static Integer STYLE_TEACHER = 2;  //教师类型
    public static Integer STYLE_COMAPNY = 3;  //企业类型
    public static Integer STYLE_ADMIN = 4;  //管理员类型
    /**
     * 用户状态
     */
    public static Integer STATE_IS_ACCEPT = 1; //已通过
    public static Integer STATE_IN_AUDIT = 2; //审核中
    public static Integer STATE_IS_REFUSE = 3; //认证失败

    /**
     * 角色id
     */
    public static Long ROLE_ADMIN_ID = 1L;
    public static Long ROLE_TEACHER_ID = 2L;
    public static Long ROLE_STUDENT_ID = 3L;
    public static Long ROLE_COMPANY_ID = 4L;
}
