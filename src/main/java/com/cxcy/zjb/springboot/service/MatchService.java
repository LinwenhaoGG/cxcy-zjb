/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: MatchService
 * Author:   KOLO
 * Date:     2018/8/20 10:40
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service;

import com.cxcy.zjb.springboot.domain.Matchs;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/20
 * @since 1.0.0
 */
public interface MatchService {
    /*
    保存比赛
     */
    Matchs saveMatch(Matchs matchs);

    /*
    通过id查找
     */
    Matchs getMatchById(Long id);
}
