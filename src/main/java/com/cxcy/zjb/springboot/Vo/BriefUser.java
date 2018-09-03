/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: BriefUser
 * Author:   KOLO
 * Date:     2018/9/3 20:33
 * Description: 去掉密码的user类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.Vo;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br> 
 * 〈去掉密码的user类〉
 *
 * @author KOLO
 * @create 2018/9/3
 * @since 1.0.0
 */
@Data
public class BriefUser {
    private Long id;

    private String name;  //用户真实姓名

    private String email;   //用户邮箱

    private String telephone;

    private String username; // 用户账号，用户登录时的唯一标识

    private String avatar = "611.jpg"; // 默认头像图片地址
}