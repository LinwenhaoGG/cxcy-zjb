/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: InMessage
 * Author:   KOLO
 * Date:     2018/8/17 11:55
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service;

import com.cxcy.zjb.springboot.Vo.OutMessage;
import com.cxcy.zjb.springboot.domain.Inmessage;

import java.util.ArrayList;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/17
 * @since 1.0.0
 */
public interface InMessageService {
    public Inmessage saveInMessage(Inmessage inMessage);

    public ArrayList<OutMessage> findUnreceivedMessage(String senderId, String receiverId);

    public boolean updateMessageState(String senderId, String receiverId);

}
