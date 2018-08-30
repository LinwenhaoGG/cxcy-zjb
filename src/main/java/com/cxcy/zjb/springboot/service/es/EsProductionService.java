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
package com.cxcy.zjb.springboot.service.es;

import com.cxcy.zjb.springboot.domain.Catagorys;
import com.cxcy.zjb.springboot.domain.Production;
import com.cxcy.zjb.springboot.domain.es.EsProduction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 学生业余作品的相关操作
 *
 *
 * @author KOLO
 * @create 2018/8/20
 * @since 1.0.0
 */
public interface EsProductionService {
    /**
     * 保存作品
     * @param esProduction
     * @return
     */
    EsProduction save(EsProduction esProduction);

    /**
     * 根据id查找对应的作品
     * @param pId
     * @return
     */
    EsProduction findByPId(Long pId);

    /**
     * 根据pId删除作品
     * @param id
     */
    void deleteByPId(String id);


    /**
     * 最热作品列表，分页
     * @param keyword
     * @param pageable
     * @return
     */
    Page<EsProduction> listHotestEsProductions(String keyword, Pageable pageable);

    /**
     * 作品列表，分页
     * @param pageable
     * @return
     */
    Page<EsProduction> listEsProductions(Pageable pageable);

}
