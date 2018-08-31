package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.InformationCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *InformationCategory 仓库
 *
 * Created by LINWENHAO on 2018/8/6.
 */
public interface InformationCategoryRepository extends JpaRepository<InformationCategory,Long> {

    /**
     * 根据名称查找分类
     * @param name
     * @return
     */
    List<InformationCategory> findByName(String name);

}
