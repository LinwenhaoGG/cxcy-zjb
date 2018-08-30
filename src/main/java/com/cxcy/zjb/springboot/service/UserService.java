/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: UserService
 * Author:   KOLO
 * Date:     2018/8/20 10:42
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service;

import com.cxcy.zjb.springboot.domain.User;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/20
 * @since 1.0.0
 */
public interface UserService {

    /**
     * 根据id返回用户
     * @param id
     * @return
     */
    public User findUserById(Long id);

    /**
     * 根据类型查询用户列表
     * @param style 用户类型
     * @return
     */
    List<User> findUserListByStyle(Integer style);
}