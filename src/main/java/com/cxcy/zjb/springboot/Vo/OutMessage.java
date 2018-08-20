/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: OutMessage
 * Author:   KOLO
 * Date:     2018/8/17 10:07
 * Description: 发送出去的消息
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.Vo;

import lombok.Data;

import java.util.Date;

/**
 * 〈一句话功能简述〉<br> 
 * 〈发送出去的消息〉
 *
 * @author KOLO
 * @create 2018/8/17
 * @since 1.0.0
 */
@Data
public class OutMessage {
    //发送者ID
    private String senderId;
    //接受者ID
    private String receiverId;
    //消息内容
    private String content;
    //当前系统时间
    private Date time = new Date();
    public OutMessage(){}

    public OutMessage(String senderId, String receiverId, String content,Date time) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.time = time;
    }
}