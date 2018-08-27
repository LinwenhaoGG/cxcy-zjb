package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.Production;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.method.P;

import java.util.List;

/**
 *Production 仓库
 *
 * Created by LINWENHAO on 2018/8/6.
 */
public interface ProductionRepository extends JpaRepository<Production,Long> {

    //按类别分页查找已审核的作品
    public Page<Production> findByCatagorysAndPCheck(Long categoryId,Integer pCheck, Pageable pageable);
    //分页查找已审核的所有作品
    public Page<Production> findByPCheck(Integer pCheck, Pageable pageable);
    //按上传时间降序分页查找
    public Page<Production> findByPCheckOrderByPuploadTimeDesc(Integer pCheck,Pageable pageable);
    //按上传时间降序查找
    public List<Production> findByPCheckOrderByPuploadTimeDesc(Integer pCheck);
    //按分类查询出前7条已审核作品
    public List<Production> findFirst7ByCatagorysAndPCheck(Long categoryId, Integer pCheck, Sort sort);


}
