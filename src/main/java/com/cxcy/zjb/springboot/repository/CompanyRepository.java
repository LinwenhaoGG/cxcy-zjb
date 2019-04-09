/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: CompanyRepository
 * Author:   KOLO
 * Date:     2018/8/27 19:50
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.repository;
import com.cxcy.zjb.springboot.Vo.UserCompanyVo;
import com.cxcy.zjb.springboot.Vo.UserTeacherVo;
import com.cxcy.zjb.springboot.domain.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/27
 * @since 1.0.0
 */
public interface CompanyRepository extends JpaRepository<Company,Long> {
    /**
     * 获取对应状态的用户公司信息
     * @param state
     * @param pageable
     * @return
     */
    @Query(value = "select new com.cxcy.zjb.springboot.Vo.UserCompanyVo(u.id, u.name, u.email,u.telephone, u.username, u.avatar, u.state, u.style, " +
            "c.companyName, c.number, c.boss, c.license, c.type,c.contacts, c.phone) " +
            " from User u,Company c where u.style = 3 and u.state = ?1 and u.company = c.id")
    Page<UserCompanyVo> findUserCompanyByState(Integer state, Pageable pageable);

    /**
     * 查找所有用户公司信息
     * @return
     */
    @Query(value = "select new com.cxcy.zjb.springboot.Vo.UserCompanyVo(u.id, u.name, u.email,u.telephone, u.username, u.avatar, u.state, u.style, u.sex, u.isUse,  " +
            "c.id, c.companyName, c.number, c.boss, c.license, c.type,c.contacts, c.phone) " +
            " from User u,Company c where  u.company = c.id")
    List<UserCompanyVo> findCompanyList();

    /**
     * 根据公司名查找用户公司信息
     * @return
     */
    @Query(value = "select new com.cxcy.zjb.springboot.Vo.UserCompanyVo(u.id, u.name, u.email,u.telephone, u.username, u.avatar, u.state, u.style, u.sex, u.isUse,  " +
            "c.id, c.companyName, c.number, c.boss, c.license, c.type,c.contacts, c.phone) " +
            " from User u,Company c where  u.company = c.id and c.companyName like %?1%")
    List<UserCompanyVo> findCompanyListByName(String name);

    /**
     * 根据用户id查找用户公司信息
     * @param id
     * @return
     */
    @Query(value = "select new com.cxcy.zjb.springboot.Vo.UserCompanyVo(u.id, u.name, u.email,u.telephone, u.username, u.avatar, u.state, u.style, u.sex, u.isUse,  " +
            "c.id, c.companyName, c.number, c.boss, c.license, c.type,c.contacts, c.phone) " +
            " from User u,Company c where u.id = ?1 and  u.company = c.id")
    UserCompanyVo findById(Long id);
}