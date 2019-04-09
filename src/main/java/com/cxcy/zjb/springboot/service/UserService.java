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

import com.cxcy.zjb.springboot.Vo.UserChartsVo;
import com.cxcy.zjb.springboot.Vo.UserMessage;
import com.cxcy.zjb.springboot.Vo.UserStudentVo;
import com.cxcy.zjb.springboot.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * 根据用户的名字查找用户
     * @param username
     * @return
     */
    User findByUsername(String username) ;

    /**
     * 通过userId查找
     * @param userId
     * @return
     */
    public User findUserById(Long userId);

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

    /**
     * 根据类型和审核状态查询出用户列表
     * @param style
     * @param state
     * @return
     */
    Page<User> findByStyleAndState(Integer style, Integer state, Pageable pageable);

    /**
     * 给用户添加对应的角色
     * @param userId
     * @param auId
     */
    public void giveUserAuthority(Long userId, Long auId);

    /**
     * 获取用户统计类型
     * @return
     */
    List<UserChartsVo> getUserChartsCount();
}