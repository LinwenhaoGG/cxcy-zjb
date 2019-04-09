/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: UserServiceImpl
 * Author:   KOLO
 * Date:     2018/8/16 17:55
 * Description: 用户service实现层
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service.impl;


import com.cxcy.zjb.springboot.Vo.UserChartsVo;
import com.cxcy.zjb.springboot.Vo.UserMessage;
import com.cxcy.zjb.springboot.constants.UserContants;
import com.cxcy.zjb.springboot.domain.Inmessage;
import com.cxcy.zjb.springboot.domain.User;
import com.cxcy.zjb.springboot.repository.InMessageRepository;
import com.cxcy.zjb.springboot.repository.UserRepository;
import com.cxcy.zjb.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 〈一句话功能简述〉<br> 
 * 〈用户service实现层〉
 *
 * @author KOLO
 * @create 2018/8/16
 * @since 1.0.0
 */
@Service
public class UserServiceImpl implements UserService,UserDetailsService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private InMessageRepository inMessageRepository;

    @Override
    public User findUser(User user) {
        return repository.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }

    @Override
    public User findUserbyUserId(String userId) {
        return repository.findOne(Long.parseLong(userId));
    }

    @Override
    public ArrayList<UserMessage> findMessageByUserId(String userId) {
        //1,根据userId查询所有未查阅的消息
        ArrayList<Inmessage> list = inMessageRepository.findByReceiverIdAndIsReadOrderByInsertTime(userId, 0);
        //2,有几个人的消息未查阅
        Set<String> UserIdSet = new HashSet<>();
        for (Inmessage inmessage:
             list) {
            UserIdSet.add(inmessage.getSenderId());
        }
        //3,遍历set
        ArrayList<UserMessage> messageArrayList  = new ArrayList<UserMessage>();

        for (String senderId:UserIdSet
             ) {
            int num = 0;
            User user = repository.findOne(Long.parseLong(senderId));

            for (Inmessage inmessage:list
                 ) {
                if (inmessage.getSenderId().equalsIgnoreCase(senderId)){
                    num++;
                }

            }
            UserMessage userMessage = new UserMessage();
            userMessage.setMessageNum(num);
            userMessage.setUserId(user.getId()+"");
            userMessage.setUserImage(user.getAvatar());
            userMessage.setUserName(user.getUsername());
            messageArrayList.add(userMessage);
        }

        return messageArrayList;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return repository.findByUsername(s);
    }

    @Override
    public User saveUserInfo(User userInfo) {
        return repository.save(userInfo);
    }

    /**
     * 根据用户账号来查询用户信息
     * @param uName
     * @return
     */
    @Override
    public User findUserInfo(String uName) {
        return repository.findByUsername(uName);
    }

    @Override
    public User findUserInfoByNameAndPwd(String username, String pwd) {
        return repository.findByUsernameAndPassword(username,pwd);
    }


    @Override
    public User findUserById(Long id) {
        return repository.findOne(id);
    }

    @Override
    public List<User> findUserListByStyle(Integer style) {
        return repository.findByStyle(style);
    }

    @Override
    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public Page<User> findByStyleAndState(Integer style, Integer state, Pageable pageable) {
        return repository.findByStyleAndState(style, state, pageable);
    }

    @Override
    public void giveUserAuthority(Long userId, Long auId) {
        repository.giveUserAuthority(userId, auId);
    }

    @Override
    public List<UserChartsVo> getUserChartsCount() {
        List<UserChartsVo> userChartsVos = repository.getUserChartsCount();
        //如果不为空，将style转换为名称
        if (null != userChartsVos) {
            Map<Integer, String> styleMap = new HashMap<>();
            styleMap.put(UserContants.STYLE_STUDENT, "学生");
            styleMap.put(UserContants.STYLE_TEACHER, "教师");
            styleMap.put(UserContants.STYLE_ADMIN, "管理员");
            styleMap.put(UserContants.STYLE_COMAPNY, "企业");
            for (UserChartsVo userCharts : userChartsVos) {
                userCharts.setName(styleMap.get(userCharts.getStyle()));
            }
        }
        return userChartsVos;
    }

    @Override
    public String getUsernameById(Long userId) {
        return repository.getUsernameById(userId);
    }

    @Override
    public Long getStudentIdById(Long userId) {
        return repository.getStudentIdById(userId);
    }

    @Override
    public Long getUserIdByUsername(String username) {
        return repository.getUserIdByUsername(username);
    }
}