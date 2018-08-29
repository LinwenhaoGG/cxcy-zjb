/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: MatchGroupService
 * Author:   KOLO
 * Date:     2018/8/20 10:40
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service;

import com.cxcy.zjb.springboot.domain.MatchGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *  比赛队伍service层
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/20
 * @since 1.0.0
 */
public interface MatchGroupService {

    /**
     * 保存信息
     * @param matchGroup
     * @return
     */
    public MatchGroup saveMatchGroup(MatchGroup matchGroup);

    /**
     *根据用户id查询报名的比赛
     * @param uid
     * @return
     */
    public Page<MatchGroup> getMatchGroupByUid(Long uid, Pageable pageable);
}
