/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: GrowthService
 * Author:   KOLO
 * Date:     2018/8/20 10:37
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service;

import com.cxcy.zjb.springboot.domain.Growth;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/20
 * @since 1.0.0
 */
public interface GrowthService {

    Growth findByUser(Long uId);

    Growth save(Growth growth);

    List<Growth> findAll();

    List<Growth> findAll(Sort sort);
}
