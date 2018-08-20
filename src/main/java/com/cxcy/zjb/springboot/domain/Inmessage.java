/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: InMessage
 * Author:   KOLO
 * Date:     2018/8/17 10:06
 * Description: 接收消息
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.domain;

import com.cxcy.zjb.springboot.utils.UUIDUtil;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 〈一句话功能简述〉<br> 
 * 〈接收消息〉
 *
 * @author KOLO
 * @create 2018/8/17
 * @since 1.0.0
 */
@Data
@Entity
@DynamicUpdate
public class Inmessage {

    //消息主键
    @Id
    private String messageId = UUIDUtil.getUUID();

    //发送者ID
    @Column(nullable = false) // 映射为字段，值不能为空
    private String senderId;

    //接受者ID
    @Column(nullable = false) // 映射为字段，值不能为空
    private String receiverId;

    //消息内容
    @Column(nullable = false) // 映射为字段，值不能为空
    private String content;

    //插入时间
    @CreationTimestamp  // 由数据库自动创建时间
    private Date insertTime;

    //消息已读状态，0为未读，1为已读
    private int isRead = 0;

    public Inmessage(){}

    public Inmessage(String senderId, String receiverId, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
    }
}