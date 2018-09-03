package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.Production;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *Production 仓库
 *
 * Created by LINWENHAO on 2018/8/6.
 */
public interface ProductionRepository extends JpaRepository<Production,Long> {
    /**
     * 模糊查询（去重）
     * @param title
     * @param summary
     * @param pageable
     * @return
     */
    Page<Production> findByPtitleContainingOrPsummaryContainingAndPCheck(String title,String summary,Integer pCheck, Pageable pageable);


    /**
     * 保存作品
     * @param production
     * @return
     */
    Production save(Production production);

    /**
     * 根据用户的id查找到对应的所有作品
     * @param user
     * @return
     */
    Page<Production> findByUserAndPCheck(Long user,Integer pCheck,Pageable pageable);

    //按类别分页查找已审核的作品
    List<Production> findByCatagorysAndPCheck(Long categoryId,Integer pCheck);
    //查找最热的已审核的前10条作品
    List<Production> findTop10ByPCheck(Integer pCheck,Sort sort);
    //按上传时间降序查找前10条
    List<Production> findTop10ByPCheckOrderByPuploadTimeDesc(Integer pCheck);
    //按上传时间降序查找
    List<Production> findByPCheckOrderByPuploadTimeDesc(Integer pCheck);

    //按分类查询出前7条已审核作品
    List<Production> findFirst7ByCatagorysAndPCheck(Long categoryId, Integer pCheck, Sort sort);
    //按用户名分页查询所有作品
    Page<Production> findByUser(Long userId,Pageable pageable);
    //按分类查找作品
    List<Production> findByCatagorys(Long catagory);
    /**
     * 根据审核类型查找作品
     * @param i
     * @return
     */
    List<Production> findByPCheck(int i);

    /**
     * 通过作品标题模糊查询
     * @param ptitle
     * @return
     */
    Page<Production> findByPtitleContaining(String ptitle,Pageable pageable);

    /**
     * 通过用户id标题模糊查询
     * @param userId
     * @return
     */
    Page<Production> findByUserContaining(Long userId,Pageable pageable);

    //按上传时间降序分页查找
    Page<Production> findByPCheckOrderByPuploadTimeDesc(Integer pCheck,Pageable pageable);
}
