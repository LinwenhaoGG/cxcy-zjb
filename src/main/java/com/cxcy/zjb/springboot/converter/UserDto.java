/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: UserDto
 * Author:   KOLO
 * Date:     2018/8/25 22:56
 * Description: 用户信息传输类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.converter;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br> 
 * 〈用户信息传输类〉
 *
 * @author KOLO
 * @create 2018/8/25
 * @since 1.0.0
 */
@Data
public class UserDto {
    private Long id;
    private String username; // 用户账号，用户登录时的唯一标识
    private Integer state=0;  //账号状态，0为未认证，1为已认证，2为认证中，3为认证失败
    private Integer style=0;  //账号类型，1为学生，2为老师，3为企业，4为管理员
}