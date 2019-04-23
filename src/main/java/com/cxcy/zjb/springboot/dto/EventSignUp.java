package com.cxcy.zjb.springboot.dto;

import com.cxcy.zjb.springboot.domain.Event;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 小组报名项目列表包装
 * Created by LINWENHAO on 2018/8/27.
 */
@Data
public class EventSignUp {

    private static final long serialVersionUID = 1L;


    private Long id;//小组的唯一标识

    private String name;  //小组名称

    private Timestamp signtime; //报名时间

    private Date lastsubmittime;   //截止提交时间

    private double achievement=0.00;//成绩

    private Event event;   //参加的项目

    private String matchName; //比赛名称

    private Long user;   //报名参加的用户id
}
