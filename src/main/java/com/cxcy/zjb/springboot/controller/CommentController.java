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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
     * @param username
     * @param pId
     * @param pContent
     * @return
     */
    @PostMapping("/{username}/createComment")
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
    public @ResponseBody ResultVO createComment(@PathVariable("username")String username,
                                                @RequestParam("pId") Long pId,@RequestParam("pContent") String pContent) {
        try {
            productionService.createComment(pId,pContent,username);
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
    @GetMapping("/{username}/listAllComments")
    public @ResponseBody ResultVO listAllComments(@PathVariable("username")String username,@RequestParam("pId")Long pId){
//        根据pid获取作品
        List<Comment> comments ;
        try {
            Production production = productionService.findByPId(pId);
            comments = production.getComments();
            if(comments.size()==0){
                return ResultUtils.error(1,"未有人评论");
            }
        }catch(Exception e){
            return ResultUtils.error(1,"无法获取相应评论");
        }
        return ResultUtils.success(comments);
    }

    /**
     * 根据cId删除相应的用户评论，判断点击删除的用户和评论的用户ID是否同一个
     * @param username
     * @param pId
     * @param cId
     * @return
     */
    @GetMapping("/{username}/deleteComment")
    public @ResponseBody ResultVO deleteComment(@PathVariable("username")String username,
                                                @RequestParam("pId")Long pId,@RequestParam("cId")Long cId) {
//        获取相应的用户id
        try{
            Long uId = commentService.findUserByCId(cId);
            User user = userService.findByUsername(username);
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
}
