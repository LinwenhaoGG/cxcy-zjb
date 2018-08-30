package com.cxcy.zjb.springboot.dto;

import lombok.Data;

/**
 * 比赛项目成绩
 * Created by LINWENHAO on 2018/8/30.
 */
@Data
public class EventAchievement {
    private Integer ranking;  //排名

    private String eventName;       //项目名称

    private String groupName;  //小组名称

    private double achievement;//成绩

    private String captainName; //队长姓名
}
