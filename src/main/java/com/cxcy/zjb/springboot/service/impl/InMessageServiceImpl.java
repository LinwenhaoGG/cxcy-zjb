/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: InMessageServiceImpl
 * Author:   KOLO
 * Date:     2018/8/17 11:56
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service.impl;

import com.cxcy.zjb.springboot.Vo.OutMessage;
import com.cxcy.zjb.springboot.domain.Inmessage;
import com.cxcy.zjb.springboot.repository.InMessageRepository;
import com.cxcy.zjb.springboot.service.InMessageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/17
 * @since 1.0.0
 */
@Service
public class InMessageServiceImpl implements InMessageService {

    @Autowired
    private InMessageRepository repository;


    @Override
    public Inmessage saveInMessage(Inmessage inMessage) {
        return repository.save(inMessage);
    }

    /**
     * 查找未查阅信息
     * @param senderId
     * @param receiverId
     * @return
     */
    @Override
    public ArrayList<OutMessage> findUnreceivedMessage(String senderId, String receiverId) {
        //1,查询未查阅消息
        ArrayList<Inmessage> inMessageList = repository.findBySenderIdAndReceiverIdAndIsReadOrderByInsertTime(senderId, receiverId, 0);
        //2,转成outmessage
        ArrayList<OutMessage> outMessageArrayList = new ArrayList<OutMessage>();
        for (Inmessage inmessage:inMessageList
             ) {
            OutMessage outMessage = new OutMessage();
            BeanUtils.copyProperties(inmessage,outMessage);
            outMessageArrayList.add(outMessage);
        }

        return outMessageArrayList;
    }

    /**
     * 修改信息的查阅状态
     * @return
     */
    public boolean updateMessageState(String senderId, String receiverId ){
        //1,修改信息状态
        ArrayList<Inmessage> inMessageList = repository.findBySenderIdAndReceiverIdAndIsReadOrderByInsertTime(senderId, receiverId, 0);
        for (Inmessage inmessage: inMessageList
             ) {
            inmessage.setIsRead(1);
            repository.save(inmessage);
        }

        return true;
    }

}