/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: UserService
 * Author:   KOLO
 * Date:     2018/8/16 17:54
 * Description: 用户service层
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service;

import com.cxcy.zjb.springboot.Vo.UserMessage;
import com.cxcy.zjb.springboot.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈用户service层〉
 *
 * @author KOLO
 * @create 2018/8/16
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

    /**
     *登录查看用户是否存在
     * @param user
     * @return
     */
    public User findUser(User user);

    /**
     * 通过用户id查找user
     * @param userId
     * @return
     */
    public User findUserbyUserId(String userId);

    /**
     * 通过用户id查找相应的聊天信息
     * @param userId
     * @return
     */
    public ArrayList<UserMessage> findMessageByUserId(String userId);

    /**
     * 保存用户信息
     * @param userInfo
     * @return
     */
    public User saveUserInfo(User userInfo);

    /**
     * 根据用户账号查找用户
     * @param uName
     * @return
     */
    public User findUserInfo(String uName);

    /**
     * 根据用户名跟密码
     * @param username
     * @param pwd
     * @return
     */
    public User findUserInfoByNameAndPwd(String username, String pwd);


}