/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: InformationServiceImpl
 * Author:   KOLO
 * Date:     2018/8/20 10:37
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service.impl;

import com.cxcy.zjb.springboot.Vo.ChartsValueCountVo;
import com.cxcy.zjb.springboot.domain.Information;
import com.cxcy.zjb.springboot.domain.InformationCategory;
import com.cxcy.zjb.springboot.repository.InformationRepository;
import com.cxcy.zjb.springboot.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/20
 * @since 1.0.0
 */
@Service
 class InformationServiceImpl implements InformationService {

    @Autowired
    private InformationRepository informationRepository;


    @Override
    public Page<Information> findAllByPage(Pageable pageable) {
        return informationRepository.findAll(pageable);
    }
    /**
     * 根据标题按照创建时间降序分页查询
     * @param title
     * @param pageable
     * @return
     */
    @Override
    public Page<Information> findByTitleLike(String title, Pageable pageable) {
        title = "%" + title +"%";
        return informationRepository.findByTitleLike(title, pageable);
    }

    /**
     * 根据id查找资讯
     * @param id
     * @return
     */
    @Override
    public Information getInformationById(Long id) {
        return informationRepository.findOne(id);
    }

    /**
     * 保存资讯
     * @param information
     */
    @Override
    public void saveInformation(Information information) {
        informationRepository.save(information);
    }

    /**
     * 阅读量+1
     * @param id
     */
    @Override
    public void readingIncrease(Long id) {
        Information information = informationRepository.findOne(id);
        information.setReadSize(information.getReadSize()+1);
        this.saveInformation(information);
    }

    @Override
    public void removeInformation(Long id) {
        informationRepository.delete(id);
    }

    /**
     * 根据分类查询
     * @param informationCategory
     * @param pageable
     * @return
     */
    @Override
    public Page<Information> listInformationByCategory(Long userId,InformationCategory informationCategory, Pageable pageable) {
        Page<Information> page = informationRepository.findByUserAndInformationCategory(userId,informationCategory,pageable);
        return page;
    }

    /**
     * 最热查询
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    @Override
    public Page<Information> listInformationByTitleVoteAndSort(Long user, String title, Pageable pageable) {
        title = "%" + title +"%";
        Page<Information> page = informationRepository.findByUserAndTitleLike(user,title,pageable);
        return page;
    }

    /**
     * 最新查询
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    @Override
    public Page<Information> listInformationByTitleVote(Long user, String title, Pageable pageable) {
        title = "%" + title + "%";
        String tags = title;
        Page<Information> page = informationRepository.findByTitleLikeAndUserOrderByCreateTimeDesc(title,user,pageable);
        return page;
    }

    @Override
    public Page<Information> listInformationOrderByCreateTimeDesc(InformationCategory informationCategory,Pageable pageable) {
        Page<Information> page = informationRepository.findByInformationCategoryOrderByCreateTimeDesc(informationCategory,pageable);
        return page;
    }

    @Override
    public List<Information> findByTitleLike(String string) {
        string = "%" + string + "%";
        return informationRepository.findByTitleLike(string);
    }

    @Override
    public List<ChartsValueCountVo> informationCountByStyle() {
        return informationRepository.informationCountByStyle();
    }

    @Override
    public List<ChartsValueCountVo> informationCountByHot(Pageable pageable) {
        return informationRepository.informationCountByHot(pageable);
    }
}