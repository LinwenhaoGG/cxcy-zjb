/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: InformationService
 * Author:   KOLO
 * Date:     2018/8/20 10:37
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service;

import com.cxcy.zjb.springboot.domain.Information;
import com.cxcy.zjb.springboot.domain.InformationCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/20
 * @since 1.0.0
 */
public interface InformationService {

    /**
     * 根据id获取资讯
     *
     * @param id
     * @return
     */
    Information getInformationById(Long id);

    /**
     * 保存资讯
     *
     * @param information
     */
    void saveInformation(Information information);

    /**
     * 阅读量递增
     *
     * @param id
     */
    void readingIncrease(Long id);

    /**
     * 删除资讯
     *
     * @param id
     */
    void removeInformation(Long id);

    /**
     * 按分类分页查询
     * @param informationCategory
     * @param pageable
     * @return
     */
    Page<Information> listInformationByCategory(Long userId,InformationCategory informationCategory, Pageable pageable);

    /**
     * 根据用户与标题模糊分类查询（最热）
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Information> listInformationByTitleVoteAndSort(Long user, String title, Pageable pageable);

    /**
     * 根据用户与标题模糊查询（最新）
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Information> listInformationByTitleVote(Long user, String title, Pageable pageable);

    /**
     *
      * @param pageable
     * @return
     */
    Page<Information> listInformationOrderByCreateTimeDesc(InformationCategory informationCategory,Pageable pageable);

    /**
     * 通过分页查找所有资讯
     * @param pageable
     * @return
     */
    Page<Information> findAllByPage(Pageable pageable);
}
