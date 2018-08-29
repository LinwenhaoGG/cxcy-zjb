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
import com.cxcy.zjb.springboot.domain.Student;
import com.cxcy.zjb.springboot.domain.User;

import java.util.ArrayList;

/**
 * 〈一句话功能简述〉<br> 
 * 〈用户service层〉
 *
 * @author KOLO
 * @create 2018/8/16
 * @since 1.0.0
 */
public interface UserService {

    public User findUser(User user);

    public User findUserbyUserId(String userId);

    public ArrayList<UserMessage> findMessageByUserId(String userId);

     public User saveUserInfo(User userInfo);

    public User findUserInfo(String uName);

    public User findUserInfoByNameAndPwd(String username, String pwd);

    public Student saveStudent(Student student);

}