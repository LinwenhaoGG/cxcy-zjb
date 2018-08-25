/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: VoteService
 * Author:   KOLO
 * Date:     2018/8/20 10:44
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service;

import com.cxcy.zjb.springboot.domain.Vote;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/20
 * @since 1.0.0
 */
public interface VoteService {

//    根据id获取vote
    Vote findById(Long id);

//    根据id移除vote
    void removeByVId(Long id);

//    根据用户id查询是否有对应的vote
    Vote findByUser(Long uId);
}
