package com.cxcy.zjb.springboot.converter;

import com.cxcy.zjb.springboot.domain.Event;
import com.cxcy.zjb.springboot.domain.MatchGroup;
import com.cxcy.zjb.springboot.domain.Matchs;
import com.cxcy.zjb.springboot.dto.EventSignUp;

/**
 * MatchGroup实体转换成EventSignUp
 * Created by LINWENHAO on 2018/8/27.
 */
public class MatchGroup2EventSignUp {

    /**
     * 将对象matchgroup和event的数据封装到eventSignUp
     * @param matchGroup
     * @param event
     * @return
     */
    public static EventSignUp conver(MatchGroup matchGroup, Event event, Matchs matchs) {
        EventSignUp eventSignUp = new EventSignUp();

        //matchgroup数据封装到eventSignUp
        eventSignUp.setUser(matchGroup.getUser());
        eventSignUp.setId(matchGroup.getId());
        eventSignUp.setName(matchGroup.getName());
        eventSignUp.setSigntime(matchGroup.getSigntime());
        eventSignUp.setAchievement(matchGroup.getAchievement());
        eventSignUp.setLastsubmittime(matchs.getLastsubmittime());
        eventSignUp.setMatchName(matchs.getName());

        //event数据封装到eventSignUp
        eventSignUp.setEvent(event);
        return eventSignUp;
    }
}
