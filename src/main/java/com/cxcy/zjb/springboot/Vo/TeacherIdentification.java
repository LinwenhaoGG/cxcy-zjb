/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: TeacherIdentification
 * Author:   KOLO
 * Date:     2018/9/2 17:23
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.Vo;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/9/2
 * @since 1.0.0
 */
@Data
public class TeacherIdentification {

    private Long id;

    private String college; //学院或者办公室

    private String school; //在职学校

    private String specially; //负责哪个专业的导师

    private Long number;  // 教师工号

    private String nation; //民族

    private String position; //职位

    private String politicsstatus; //政治面貌

    private String credential;           //目前研究方向和指导学生要求

    private String name;  //用户真实姓名

    private String email;   //用户邮箱

    private String telephone;

    private String username; // 用户账号，用户登录时的唯一标识

    private String avatar = "611.jpg"; // 默认头像图片地址


}