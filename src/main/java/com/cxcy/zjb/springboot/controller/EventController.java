package com.cxcy.zjb.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cxcy.zjb.springboot.Vo.ResultVO;
import com.cxcy.zjb.springboot.converter.EventMatchs2TeacherEvent;
import com.cxcy.zjb.springboot.domain.Event;
import com.cxcy.zjb.springboot.domain.Matchs;
import com.cxcy.zjb.springboot.domain.User;
import com.cxcy.zjb.springboot.dto.TeacherEvent;
import com.cxcy.zjb.springboot.service.EventService;
import com.cxcy.zjb.springboot.service.MatchService;
import com.cxcy.zjb.springboot.service.UserService;
import com.cxcy.zjb.springboot.utils.ConstraintViolationExceptionHandler;
import com.cxcy.zjb.springboot.utils.LongUtils;
import com.cxcy.zjb.springboot.utils.ResultUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 项目控制层
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private UserService userService;

    /**
     * 通过用户id获得对方比赛管理
     *
     * @param map
     * @return
     */
    @RequestMapping("/teacherEvent")
    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER')")  // 指定角色权限才能操作方法
    public ModelAndView addMatchs( @RequestParam(value = "page", defaultValue = "1") Integer page, //页数
                                  @RequestParam(value = "size", defaultValue = "5") Integer size,//一页个数
                                  Map map) {
        //获取当前登录的老师账号
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long uid = loginUser.getId();
        //分页类
        PageRequest pageRequest = new PageRequest(page-1, size);

        //获取个数
        Integer total = eventService.getEventCountByUid(uid);
        Integer totalPage = 1;

        if (total % size == 0) {
            totalPage = total / size;
        } else {
            totalPage = (total / size) + 1;
        }

        //通过负责人id获得管理的比赛项目
        List<Event> eventList = eventService.getEventByUser(uid,pageRequest);
        List<TeacherEvent> teacherEventList = new ArrayList<>();
        TeacherEvent teacherEvent;
        for (Event event : eventList) {
            teacherEvent = EventMatchs2TeacherEvent.conver(matchService.getMatchById(event.getMatchId()),event);
            teacherEventList.add(teacherEvent);
        }
        map.put("teacherEventList", teacherEventList);
        map.put("size", size);
        map.put("page", page);
        map.put("totalPage", totalPage);
        return new ModelAndView("matchs/teacher/teacherEventList", map);
    }

    /**
     * 测试接口
     * @param map
     * @return
     */
    @RequestMapping("/test")
    @ResponseBody
    public List<Event> test(Long uid,Map map) {
        //分页类
        PageRequest pageRequest = new PageRequest(0, 3);

        //通过负责人id获得管理的比赛项目
        List<Event> eventList = eventService.getEventByUser(uid,pageRequest);
        return eventList;
    }


    /**
     * 删除项目
     * @return
     */
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    public ResponseEntity<ResultVO> deleteEvent(@PathVariable("id") Long id, Long matchsId) {
        try {
            matchService.deleteEvent(matchsId,id);
            eventService.removeEvent(id);
        } catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(ResultUtils.error(1,ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(ResultUtils.error(1,e.getMessage()));
        }

        return ResponseEntity.ok().body(ResultUtils.success());
    }

//    /**
//     * 获取比赛项目列表
//     * @param matchsId
//     * @param model
//     * @return
//     */
//    @GetMapping("/list")
//    @ResponseBody
//    public ResultVO listEvents(@RequestParam(value="matchsId",required=true) Long matchsId, Model model) {
//        Matchs matchs = matchService.getMatchById(matchsId);
//        List<Event> events = matchs.getEventList();
//
//        // 判断操作用户是否是评论的所有者
//        String commentOwner = "";
//        if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
//                &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
//            User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            if (principal !=null) {
//                commentOwner = principal.getUsername();
//            }
//        }
//        model.addAttribute("events", events);
//        return ResultUtils.success(events);
//    }

    /**
     * 保存比赛的项目
     * @param object  json数据
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER','ROLE_ADMIN')")  // 指定角色权限才能操作方法
    //Matchs matchs, @RequestBody EventJson[] itemList
    public ResultVO saveEvents(@RequestBody JSONObject object) {
        String data=object.toJSONString();
        //解析json数据
        JSONObject json = JSON.parseObject(data);
        //获取json里的字符串内容
        String createArr=json.getString("eventList");
        Long mid=json.getLong("mid");
        //即将保存的eventList
        List<Event> eventList = new ArrayList<>();
        //将json数据转换成list
        if(StringUtils.isNotEmpty(createArr)){//判断字符串是否为空
            JSONArray createArray=JSONArray.parseArray(createArr);
            for(int i=0;i<createArray.size();i++){
                Event event = new Event();
                //项目的负责人列表
                List<User> users = new ArrayList<>();
                //得到项目名称
                String event_name=JSONObject.parseObject(JSONObject.toJSONString(createArray.get(i))).getString("item_name");
                String teacher_list=JSONObject.parseObject(JSONObject.toJSONString(createArray.get(i))).getString("teacher_list");
                Integer event_number=JSONObject.parseObject(JSONObject.toJSONString(createArray.get(i))).getInteger(    "event_number");
                //解析teacher_list
                if (StringUtils.isNotEmpty(teacher_list)) { //判断老师列表是否为空
                    JSONArray teacher_Array=JSONArray.parseArray(teacher_list);
                    for(int j=0;j<teacher_Array.size();j++){//遍历获取每个负责人的id
                        Long teacher_id=Long.parseLong(teacher_Array.get(j).toString());
                        if (!LongUtils.IsNone(teacher_id)){ //判断是否为空
                            User user = userService.findUserById(teacher_id);//得到该教师的信息
                            users.add(user);//加入user
                        }
                    }
                }
                event.setName(event_name);//设置名称
                event.setUserList(users);//设置负责人列表
                event.setPNumber(event_number);
                event.setMatchId(mid);
                eventList.add(event); //将项目加入项目列表
            }
            if (!LongUtils.IsNone(mid)) { //判断mid是否存在
                Matchs matchs = matchService.getMatchById(mid);
                matchs.setEventList(eventList);
                matchService.saveMatch(matchs);
            }
        }
        return ResultUtils.success();
    }
}
