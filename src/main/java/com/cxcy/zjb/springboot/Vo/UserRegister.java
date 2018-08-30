/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: UserRegister
 * Author:   KOLO
 * Date:     2018/8/24 20:46
 * Description: 用户注册
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.Vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * 〈一句话功能简述〉<br> 
 * 〈用户注册〉
 *
 * @author KOLO
 * @create 2018/8/24
 * @since 1.0.0
 */
@Data
public class UserRegister {
    //注册页面中，姓名暂时可为空，在认证页面再填写
    @Size(min=2, max=20)
    private String name;  //用户真实姓名

    //账号
    @NotEmpty(message = "账号不能为空")
    private String username;
    //密码
    @NotEmpty(message = "密码不能为空")
    @Length(min = 6,message = "密码长度不能少于6位")
    private String password;
    //手机
    @NotEmpty(message = "手机号码不能为空")
    @Length(min = 11,max = 11,message = "手机号码长度是11位的")
    private String telephone;
    //身份
    @NotEmpty(message = "身份标识不能为空")
    private String style;
    //验证码
    @NotEmpty(message = "验证码不能为空")
    private String code;

}