/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: UserIdentification
 * Author:   KOLO
 * Date:     2018/9/2 16:14
 * Description: 用户认证信息
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.Vo;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br> 
 * 〈用户认证信息〉
 *
 * @author KOLO
 * @create 2018/9/2
 * @since 1.0.0
 */
@Data
public class UserIdentification {

    private Long id;

    private String name;  //用户真实姓名

    private String email;   //用户邮箱

    private String telephone; //手机号码

    private String username; // 用户账号，用户登录时的唯一标识

    private String avatar = "611.jpg"; // 默认头像图片地址

    private Integer state=0;  //账号状态，0为未认证，1为已认证，2为审核中，3为认证失败

    private String edu;//年级

    private String college; //学院

    private String classes; //专业班级

    private Long number;  // 学号

    private String nation; //民族

    private String politicsstatus; //政治面貌

    private String credential;           //个人简介

}