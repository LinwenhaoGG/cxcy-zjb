package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.Information;
import com.cxcy.zjb.springboot.domain.InformationCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *Information 仓库
 *
 * Created by LINWENHAO on 2018/8/6.
 */
public interface InformationRepository extends JpaRepository<Information,Long> {

    /**
     * 根据分类分页查询
     * @param informationCategory
     * @param pageable
     * @return
     */
    Page<Information> findByUserAndInformationCategory(Long UserId,InformationCategory informationCategory, Pageable pageable);

    /**
     * 根据用户与标题模糊分类查询
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Information> findByUserAndTitleLike(Long user,String title,Pageable pageable);

    /**
     * 根据用户与标题按照创建时间降序分页查询
     * @param title
     * @param user
     * @param pageable
     * @return
     */
    Page<Information> findByTitleLikeAndUserOrderByCreateTimeDesc(String title, Long user,Pageable pageable);

    /**
     * 根据标题按照创建时间降序分页查询
     * @param title
     * @param pageable
     * @return
     */
    Page<Information> findByTitleLike(String title,Pageable pageable);


    Page<Information> findByInformationCategoryOrderByCreateTimeDesc(InformationCategory informationCategory,Pageable pageable);

    /**
     * 根据标题模糊查询
     * @param string
     * @return
     */
    List<Information> findByTitleLike(String string);
 }
