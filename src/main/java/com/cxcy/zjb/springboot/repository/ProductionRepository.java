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
    public List<Production> findByCatagorysAndPCheck(Long categoryId,Integer pCheck);
    //查找最热的已审核的前10条作品
    public List<Production> findTop10ByPCheck(Integer pCheck,Sort sort);
    //按上传时间降序查找前10条
    public List<Production> findTop10ByPCheckOrderByPuploadTimeDesc(Integer pCheck);
    //按上传时间降序查找
    public List<Production> findByPCheckOrderByPuploadTimeDesc(Integer pCheck);
    //按分类查询出前7条已审核作品
    public List<Production> findFirst7ByCatagorysAndPCheck(Long categoryId, Integer pCheck, Sort sort);


}
