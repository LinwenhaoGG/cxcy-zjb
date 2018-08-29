/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: WebSocketService
 * Author:   KOLO
 * Date:     2018/8/17 10:38
 * Description: websocket发送消息模板
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service.impl;

import com.cxcy.zjb.springboot.Vo.OutMessage;
import com.cxcy.zjb.springboot.domain.Inmessage;
import com.cxcy.zjb.springboot.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * 〈一句话功能简述〉<br> 
 * 〈websocket发送简单消息模板〉
 *
 * @author KOLO
 * @create 2018/8/17
 * @since 1.0.0
 */
@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private UserService userService;

    public  void sendMessage(Inmessage inMessage){
        OutMessage outMessage = new OutMessage();
        BeanUtils.copyProperties(inMessage,outMessage);
        template.convertAndSend("/channel/single/chat/"+inMessage.getReceiverId(),outMessage);
    }

}