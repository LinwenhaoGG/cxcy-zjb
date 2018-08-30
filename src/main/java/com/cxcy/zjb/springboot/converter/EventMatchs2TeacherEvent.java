package com.cxcy.zjb.springboot.converter;

import com.cxcy.zjb.springboot.domain.Event;
import com.cxcy.zjb.springboot.domain.Matchs;
import com.cxcy.zjb.springboot.dto.TeacherEvent;

/**
 * 将项目跟比赛的一些内容包装其阿里
 * Created by LINWENHAO on 2018/8/28.
 */
public class EventMatchs2TeacherEvent {
    /**
     * 将对象match和event的数据封装到eventSignUp
     * @param match
     * @param event
     * @return
     */
    public static TeacherEvent conver(Matchs match, Event event) {
        TeacherEvent teacherEvent = new TeacherEvent();

        //match数据封装到teacherEvent
        teacherEvent.setLastsigntime(match.getLastsigntime());
        teacherEvent.setLastsubmittime(match.getLastsubmittime());
        teacherEvent.setMName(match.getName());
        teacherEvent.setStartTime(match.getStartTime());
        teacherEvent.setOverTime(match.getOverTime());
        teacherEvent.setMId(match.getId());

        //event数据封装到teacherevent
        teacherEvent.setEId(event.getId());
        teacherEvent.setEName(event.getName());

        return teacherEvent;
    }
}
