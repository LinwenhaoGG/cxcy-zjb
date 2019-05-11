/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: CompanyService
 * Author:   KOLO
 * Date:     2018/8/27 19:51
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service;

import com.cxcy.zjb.springboot.Vo.UserCompanyVo;
import com.cxcy.zjb.springboot.Vo.UserTeacherVo;
import com.cxcy.zjb.springboot.domain.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/27
 * @since 1.0.0
 */
public interface CompanyService {
    /**
     * 保存company
     * @param company
     * @return
     */
    public Company saveCompany(Company company);

    /**
     * 获取company
     * @param id
     * @return
     */
    public Company getCompany(Long id);

    /**
     * 通过用户状态获取企业信息
     * @param state
     * @return
     */
    Page<UserCompanyVo> findUserCompanyByState(Integer state, Pageable pageable);

    /**
     * 根据公司名称查找所有
     * @param name
     * @return
     */
    List<UserCompanyVo> findCompanyListByName(String name);

    /**
     * 根据id获取用户公司信息
     * @param id
     * @return
     */
    UserCompanyVo findUserCompanyById(Long id);
}