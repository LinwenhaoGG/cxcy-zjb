package com.cxcy.zjb.springboot.converter;

import com.cxcy.zjb.springboot.domain.Event;
import com.cxcy.zjb.springboot.domain.GroupMember;
import com.cxcy.zjb.springboot.domain.MatchGroup;
import com.cxcy.zjb.springboot.dto.EventAchievement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LINWENHAO on 2018/8/30.
 */
public class EventMatchGroup2EventAchievement {
    public static List<EventAchievement> conver(List<MatchGroup> matchGroups, Event event) {
        List<EventAchievement> eventAchievementList = new ArrayList<>();
        //排名
        Integer ranking = 0;
        for (MatchGroup matchGroup : matchGroups) {
            ranking++;
            EventAchievement eventAchievement = new EventAchievement();
            eventAchievement.setRanking(ranking);
            eventAchievement.setEventName(event.getName());
            eventAchievement.setAchievement(matchGroup.getAchievement());
            eventAchievement.setGroupName(matchGroup.getName());
            for (GroupMember groupMember : matchGroup.getGroupMemberList()) {
                if (groupMember.getStyle().equals(1)) {
                    eventAchievement.setCaptainName(groupMember.getName());
                    break;
                }
            }
            eventAchievementList.add(eventAchievement);
        }

        return eventAchievementList;
    }
}
