/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: ChatController
 * Author:   KOLO
 * Date:     2018/8/16 20:37
 * Description: 聊天
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.OutMessage;
import com.cxcy.zjb.springboot.Vo.Result;
import com.cxcy.zjb.springboot.domain.Inmessage;
import com.cxcy.zjb.springboot.domain.User;
import com.cxcy.zjb.springboot.service.InMessageService;
import com.cxcy.zjb.springboot.service.UserService;
import com.cxcy.zjb.springboot.service.impl.WebSocketService;
import com.cxcy.zjb.springboot.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

/**
 * 〈一句话功能简述〉<br> 
 * 〈聊天〉
 *
 * @author KOLO
 * @create 2018/8/16
 * @since 1.0.0
 */
@Controller
public class ChatController {

    @Autowired
    private UserService userService;
    @Autowired
    private WebSocketService ws;
    @Autowired
    private InMessageService inMessageService;

    /**
     * 进入聊天页面时，查询出用户的相关数据
     * @param userId
     * @param session
     * @return
     */
    @PostMapping("user/chat")
    @ResponseBody
    public Result userChat(@RequestParam("userId") String  userId, HttpSession session){
        //1，根据userId查询用户信息
        User user = userService.findUserbyUserId(userId);
        User personUser = (User)session.getAttribute("user");
        ArrayList<User> list = new ArrayList<>();
        list.add(user);
        list.add(personUser);
        Result result = ResultUtil.success(list);
        //2，返回对象，并已json格式返回
        return result;

    }

    /**
     *聊天接口
     * @param inMessage
     */
    @MessageMapping("user/single/chat")
    public void singleChat(Inmessage inMessage){

        Inmessage inMessage1 = inMessageService.saveInMessage(inMessage);

        ws.sendMessage(inMessage);

    }

    /**
     * 查询用户未接收信息
     * @param senderId
     * @param receiverId
     * @return
     */
    @RequestMapping("user/unreceivedInfo")
    @ResponseBody
    public Result unreceivedMessage(@RequestParam("senderId") String senderId, @RequestParam("receiverId") String receiverId){

        String temp;
        temp = senderId;
        senderId = receiverId;
        receiverId = temp;
        //1,查询未查阅消息
        ArrayList<OutMessage> list = inMessageService.findUnreceivedMessage(senderId,receiverId);
        //2,修改消息已读状态
        boolean state = inMessageService.updateMessageState(senderId, receiverId);
        //3,将数据返回
        Result result = ResultUtil.success(list);

        return result;

    }

    /**
     * 修改用户消息的已查阅
     * @param senderId
     * @param receiverId
     * @return
     */
    @RequestMapping("user/updateMessage")
    @ResponseBody
    public Result updateMessageState(@RequestParam("senderId") String senderId, @RequestParam("receiverId") String receiverId){
        String temp;
        temp = senderId;
        senderId = receiverId;
        receiverId = temp;

        //1，修改消息状态
        boolean state = inMessageService.updateMessageState(senderId, receiverId);
        //2，返回数据
        Result result = ResultUtil.success();

        return result;

    }


}