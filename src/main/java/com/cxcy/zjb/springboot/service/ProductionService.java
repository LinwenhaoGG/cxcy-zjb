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
public interface ProductionService {

    /**
     * 通过类型分页查找已审核通过的作品
     * @param categoryId
     * @return
     */
    public List<Production> findProductionsByCategoryId(Long categoryId);

    /**
     * 查询已审核通过的作品
     * @param sort
     * @return
     */
    public List<Production> findAll(Sort sort);

    /**
     * 按时间降序查找前10条已审核通过的作品
     * @return
     */
    public List<Production> findTop10orderByTimeDesc();

    /**
     * 按时间降序查找已审核通过的作品
     * @return
     */
    public List<Production> findOrderByTimeDesc();

    /**
     * 按分类查询前7条已审核作品
     * @param catagoryId
     * @return
     */
    public List<Production> findFirst7ByCatagorysAndPCheck(Long catagoryId, Sort sort);
}
