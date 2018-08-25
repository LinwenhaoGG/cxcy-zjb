/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: ProductionService
 * Author:   KOLO
 * Date:     2018/8/20 10:41
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service;

import com.cxcy.zjb.springboot.domain.Production;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/20
 * @since 1.0.0
 */
public interface ProductionService {

    /**
     * 通过类型分页查找已审核通过的作品
     * @param categoryId
     * @return
     */
    public Page<Production> findProductionsByCategoryId(Long categoryId,Pageable pageable);

    /**
     * 查询已审核通过的作品
     * @param pageable
     * @return
     */
    public Page<Production> findAll(Pageable pageable);

    /**
     * 按时间降序查找已审核通过的作品
     * @param pageable
     * @return
     */
    public Page<Production> findOrderByTimeDesc(Pageable pageable);
}
