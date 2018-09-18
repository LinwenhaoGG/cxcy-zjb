package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.ResultVO;
import com.cxcy.zjb.springboot.domain.Comment;
import com.cxcy.zjb.springboot.domain.Production;
import com.cxcy.zjb.springboot.domain.User;
import com.cxcy.zjb.springboot.service.CommentService;
import com.cxcy.zjb.springboot.service.ProductionService;
import com.cxcy.zjb.springboot.service.UserService;
import com.cxcy.zjb.springboot.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论控制层
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductionService productionService;
    @Autowired
    private CommentService commentService;

    /**
     * 根据访问的用户发表评论
     * @param pId
     * @param comment
     * @return
     */
    @PostMapping("/createComment/{pId}")
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
    public @ResponseBody ResultVO createComment(@PathVariable("pId") Long pId,@RequestParam("comment") String comment) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            productionService.createComment(pId,comment,user);
         } catch (Exception e) {
            return ResultUtils.error(1,"创建评论不成功");
        }
        return ResultUtils.success();
    }

    /**
     * 哪个用户访问哪个作品，显示该作品的所有评论,连带评论的所有信息
     * @param pId
     * @return
     */
    @GetMapping("/listAllComments")
    public @ResponseBody ResultVO listAllComments(@RequestParam("pId")Long pId){
//        根据pid获取作品
        List<Comment> comments ;
        List list = new ArrayList();
        User user1 = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user1.getUsername();
        try {
            Production production = productionService.findByPId(pId);
            comments = production.getComments();
            for(Comment comment:comments ){
                Map<String,Object> map1 = new HashMap<>();
                Map<String,Object> map = new HashMap<>();
                Long uId = commentService.findUserByCId(comment.getId());
                User user = userService.findUserById(uId);
                String username1 = user.getUsername();
                map1.put(username1,comment);
                //判断是否是自己的评论
                boolean flag = false;
                if(username.equals(username1)){
                    flag = true;
                }
                map.put("flag",flag);
                map.put("map",map1);
                list.add(map);
            }
            if(comments.size()==0){
                return ResultUtils.error(1,"未有人评论");
            }
        }catch(Exception e){
            return ResultUtils.error(1,"无法获取相应评论");
        }
        return ResultUtils.success(list);
    }

    /**
     * 根据cId删除相应的用户评论，判断点击删除的用户和评论的用户ID是否同一个
     * @param pId
     * @param cId
     * @return
     */
    @GetMapping("/deleteComment")
    public @ResponseBody ResultVO deleteComment(@RequestParam("pId")Long pId,@RequestParam("cId")Long cId) {
//        获取相应的用户id
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try{
            Long uId = commentService.findUserByCId(cId);
            Long uId1 = user.getId();
//        判断删除者是否所属者
            if(uId.equals(uId1)){
                productionService.removeComment(pId,cId);//外键关联，需要先删除作品的cid
                commentService.removeCommentById(cId);
            }else{
                return ResultUtils.error(1,"权限不符合");
            }
        }catch (Exception e){
            return ResultUtils.error(2,"删除失败");
        }
        return ResultUtils.success();
    }


    //------------------------------------------------------------管理员方法-------------------------------------------------------------------
    /**
     * 哪个用户访问哪个作品，显示该作品的所有评论,连带评论的所有信息
     * @param pId
     * @return
     */
    @GetMapping("/admin/listAllComments")
    public @ResponseBody ResultVO adlistAllComments(@RequestParam("pId")Long pId){
//        根据pid获取作品
        List<Comment> comments ;
        List list = new ArrayList();
        try {
            Production production = productionService.findByPId(pId);
            comments = production.getComments();
            for(Comment comment:comments ){
                Map<String,Object> map1 = new HashMap<>();
                Map<String,Object> map = new HashMap<>();
                Long uId = commentService.findUserByCId(comment.getId());
                User user = userService.findUserById(uId);
                String username1 = user.getUsername();
                map1.put(username1,comment);
                map.put("map",map1);
                list.add(map);
            }
            if(comments.size()==0){
                return ResultUtils.error(1,"未有人评论");
            }
        }catch(Exception e){
            return ResultUtils.error(1,"无法获取相应评论");
        }
        return ResultUtils.success(list);
    }

    /**
     * 管理员根据cId删除相应的用户评论，判断点击删除的用户和评论的用户ID是否同一个
     * @param pId
     * @param cId
     * @return
     */
    @GetMapping("/admin/deleteComment")
    public @ResponseBody ResultVO adeleteComment(@RequestParam("pId")Long pId,@RequestParam("cId")Long cId) {
        try{
                productionService.removeComment(pId,cId);//外键关联，需要先删除作品的cid
                commentService.removeCommentById(cId);
        }catch (Exception e){
            return ResultUtils.error(1,"删除失败");
        }
        return ResultUtils.success();
    }
}
