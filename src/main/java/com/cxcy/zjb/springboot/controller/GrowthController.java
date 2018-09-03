package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.domain.Growth;
import com.cxcy.zjb.springboot.service.GrowthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * 成长体系控制层
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
@RequestMapping("/growth")
@EnableScheduling       //开启定时任务
public class GrowthController {

    @Autowired
    private GrowthService growthService;



 /*   //测试一个定时方法
    @Scheduled(cron = "0/25 * * * * ?")
    private void configureTasks() {
        System.err.println("执行定时任务1: " + LocalDateTime.now());
    }*/

    //更新所有用户的积分情况以及用户的排名
    @Scheduled(cron = "* * 5 * * ?")
    public void updateRank(){
        //1.对用户成长表中的点赞和评论和浏览量进行累加得到积分
        //1.1获取所有用户的growth
        List<Growth> growthList = growthService.findAll();
        //1.2遍历所有的成长记录，对他们的点赞总量和评论总量以及浏览总量进行累加得到积分
        for (Growth growth:growthList) {
            growth.setGIntegration(growth.getGComment()+growth.getGVote()+growth.getGReadSize());
            growthService.save(growth);
        }
        //1.3根据积分进行排序查找，然后进行排名
        Sort sort = new Sort(DESC,"gIntegration");
        //1.4根据sort去进行查找所有growth,进行排名设置
        List<Growth> list = growthService.findAll(sort);
        int i = 1;
        for (Growth growth:list) {
            growth.setGRank(i);
            i++;
            growthService.save(growth);
        }
    }


}
