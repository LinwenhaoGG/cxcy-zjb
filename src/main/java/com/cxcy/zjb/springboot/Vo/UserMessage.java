/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: UserMessage
 * Author:   KOLO
 * Date:     2018/8/19 8:45
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.Vo;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/19
 * @since 1.0.0
 */
@Data
public class UserMessage {
    //用户id
    private String userId;
    //用户昵称
    private String userName;
    //用户头像
    private String userImage;
    //未读消息数
    private int messageNum;

    public UserMessage() {
    }

    public UserMessage(String userId, String userName, String userImage, int messageNum) {
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
        this.messageNum = messageNum;
    }
}