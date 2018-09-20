/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: CompanyIdentification
 * Author:   KOLO
 * Date:     2018/9/2 17:49
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
public class CompanyIdentification {

    private Long id;

    private String name;  //用户真实姓名

    private String email;   //用户邮箱

    private String telephone;

    private String username; // 用户账号，用户登录时的唯一标识

    private String avatar = "611.jpg"; // 默认头像图片地址

    private String companyName; //公司名称

    private String number; //企业社会代码

    private String boss; //公司法定代表人

    private String license; //营业执照

    private String type; //公司类型

    private String contacts; //联系人

    private String phone; //联系电话

    private int state;//认证状态

}