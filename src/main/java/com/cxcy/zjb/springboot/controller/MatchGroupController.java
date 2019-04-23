package com.cxcy.zjb.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cxcy.zjb.springboot.Vo.MatchGroupVo;
import com.cxcy.zjb.springboot.Vo.ResultVO;
import com.cxcy.zjb.springboot.converter.MatchGroup2EventSignUp;
import com.cxcy.zjb.springboot.domain.*;
import com.cxcy.zjb.springboot.dto.EventSignUp;
import com.cxcy.zjb.springboot.service.EventService;
import com.cxcy.zjb.springboot.service.MatchGroupService;
import com.cxcy.zjb.springboot.service.MatchService;
import com.cxcy.zjb.springboot.utils.LongUtils;
import com.cxcy.zjb.springboot.utils.ResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 比赛小组控制层
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
@RequestMapping("/group")
public class MatchGroupController {
    @Autowired
    private MatchService matchService;

    @Autowired
    private MatchGroupService matchGroupService;

    @Autowired
    private EventService eventService;

    @Value("${video.VideoPath}")
    private String VideoPath;
     /**
     * 跳转报名页面
     * @param map
     * @return
     */
    @RequestMapping("/signUp/{mid}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER','ROLE_TEACHER')")  // 指定角色权限才能操作方法
    public ModelAndView signUp(@PathVariable("mid") Long mid,Map map) {
        Matchs matchs = matchService.getMatchById(mid);


        map.put("matchs",matchs);
        return new ModelAndView("matchs/matchGroup/EventsignUp", map);
    }

    @GetMapping("/submit")
    public ModelAndView upload(@RequestParam Long gid,  Map map) {
        //通过id获取该小组情况
        MatchGroup matchGroup = matchGroupService.getMatchGroupByid(gid);
        //获取当前登录的账号
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //判断登录用户是否该作者
        if (!loginUser.getId().equals(matchGroup.getUser())) {
            return new ModelAndView("/matchs/matchGroup/signUpList",map);
        }

        map.put("matchGroup",matchGroup);

        return new ModelAndView("/matchs/matchGroup/groupUpload",map);
    }

    /**
     * 保存对比赛的报名信息
     * @param object  json数据
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    //Matchs matchs, @RequestBody EventJson[] itemList
    public ResultVO saveGroup(@RequestBody JSONObject object) {
        String data=object.toJSONString();
        //解析json数据
        JSONObject json = JSON.parseObject(data);
        //获取json里的字符串内容
        String GmemberList=json.getString("memberList");
        Long eventId=json.getLong("eventId");
        String team_name=json.getString("team_name");
        //获取当前登录人ID
        Long uid = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        //memberList
        List<GroupMember> memberList = new ArrayList<>();
        //比赛队伍对象
        MatchGroup matchGroup = new MatchGroup();
        //将json数据转换成list
        if(StringUtils.isNotEmpty(GmemberList)){//判断字符串是否为空
            JSONArray menberArray=JSONArray.parseArray(GmemberList);
            //遍历获取报名的每个队员信息
            for(int i=0;i<menberArray.size();i++){
                GroupMember groupMember = new GroupMember();
                String name= JSONObject.parseObject(JSONObject.toJSONString(menberArray.get(i))).getString("name");
                Integer grade=JSONObject.parseObject(JSONObject.toJSONString(menberArray.get(i))).getInteger("grade");
                Long number=JSONObject.parseObject(JSONObject.toJSONString(menberArray.get(i))).getLong("id");
                String college = JSONObject.parseObject(JSONObject.toJSONString(menberArray.get(i))).getString("college");
                String classes = JSONObject.parseObject(JSONObject.toJSONString(menberArray.get(i))).getString("major");
                String phone = JSONObject.parseObject(JSONObject.toJSONString(menberArray.get(i))).getString("phone");
                Integer style = JSONObject.parseObject(JSONObject.toJSONString(menberArray.get(i))).getInteger("post");

                //给对象赋值
                groupMember.setName(name);
                groupMember.setEdu(grade);
                groupMember.setNumber(number);
                groupMember.setClasses(classes);
                groupMember.setCollege(college);
                groupMember.setPhone(phone);
                groupMember.setStyle(style);
                //将对象加入数组
                memberList.add(groupMember);
            }
            if (!LongUtils.IsNone(eventId)) { //判断项目id是否为空
                matchGroup.setEvent(eventId);
            }
            matchGroup.setName(team_name);
            matchGroup.setGroupMemberList(memberList);
            matchGroup.setUser(uid);
            matchGroupService.saveMatchGroup(matchGroup);
        }
        return ResultUtils.success();
    }

    /**
     * 查看报名的信息
     * @param map
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER','ROLE_TEACHER')")  // 指定角色权限才能操作方法
    public ModelAndView listSignUp(@RequestParam(value = "page", defaultValue = "1") Integer page, //页数
                                   @RequestParam(value = "size", defaultValue = "5") Integer size,//一页个数
                                   Map map) {
        //获取当前登录的账号
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long uid = loginUser.getId();
        //根据开始时间排序
        Sort sort = new Sort(org.springframework.data.domain.Sort.Direction.DESC,"Signtime");
        PageRequest request = new PageRequest(page - 1, size,sort);

        //返回前端报名项目封装对象
        List<EventSignUp> eventSignUpList = new ArrayList<>();
        //查找该用户报名的队伍
        Page<MatchGroup> matchGroupPage = matchGroupService.getMatchGroupByUid(uid,request);

        for (MatchGroup matchGroup : matchGroupPage.getContent()) {
            //获取该队伍报名的项目
            Event event = eventService.getEventById(matchGroup.getEvent());
            Matchs matchs = matchService.getOnlyMatchByid(event.getMatchId());
            //通过转换器转换后添加进去
            eventSignUpList.add(MatchGroup2EventSignUp.conver(matchGroup, event, matchs));
        }
        map.put("eventSignUpList", eventSignUpList);
        map.put("page", page);
        map.put("size", size);
        map.put("matchGroupPage", matchGroupPage);
        if (matchGroupPage.getTotalPages() == 0) {
            map.put("isnull", 1);
        }else {
            map.remove("isnull");
        }
        return new ModelAndView("matchs/matchGroup/signUpList", map);
    }

    /**
     * 新建或者修改作品，成功则返回对应的作品路径
     * @param pContent
     * @param videoFile
     * @return
     */
    @RequestMapping(value="/uploadProduction")
    public @ResponseBody ResultVO uploadProduction(
            @RequestParam(value="groupId") Long groupId,
            @RequestParam("pContent") MultipartFile pContent,
            @RequestParam(value="videoFile",required = false) MultipartFile videoFile) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            MatchGroup matchGroup = matchGroupService.getMatchGroupByid(groupId);
            //保存文件
            String filePath =new  ProductionController().saveFile(pContent);
            //保存视频
            if(videoFile!=null){
                String video = new  ProductionController().saveVideo(videoFile,"video");
                if(video!=null){
                    matchGroup.setMvAddress(video);
                }
            }
            matchGroup.setDocAddress(filePath);
            //保存小组情况
            MatchGroup returnMatchGroup = matchGroupService.saveMatchGroup(matchGroup);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String redirectUrl = "/group/list";
        return ResultUtils.success(redirectUrl);
    }

    /**
     * 保存评审后的数组
     * @param groupList
     * @return
     */
//    ,consumes = "application/json"
    @RequestMapping(value = "/saveGroupList",method = RequestMethod.POST)
    @ResponseBody
    public ResultVO saveGroupList(@RequestBody MatchGroupVo[] groupList) {
        MatchGroup savegroup;
        try {
            for (MatchGroupVo matchGroup : groupList) {
                savegroup = matchGroupService.getMatchGroupByid(matchGroup.getId());
                savegroup.setAchievement(matchGroup.getAchievement());
                matchGroupService.saveMatchGroup(savegroup);
            }
        } catch (Exception e) {
            return ResultUtils.error(1, "上传失败！");
        }
        return ResultUtils.success();
    }

}
