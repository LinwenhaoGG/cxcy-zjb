package com.cxcy.zjb.springboot.dto;

import lombok.Data;

import java.util.Date;

/**
 * Created by LINWENHAO on 2018/8/28.
 */
@Data
public class TeacherEvent {
    private Long eId;//比赛项目的唯一标识

    private Long mId;//比赛的唯一标识

    private String eName;       //项目名称

    private String mName;       //比赛名称

    private Date startTime;  //比赛开始时间

    private Date overTime;   //比赛结束时间

    private Date lastsigntime;  //截止报名时间

    private Date lastsubmittime;   //截止提交时间
}
