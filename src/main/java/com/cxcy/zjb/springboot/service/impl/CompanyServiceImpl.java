/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: CompanyServiceImpl
 * Author:   KOLO
 * Date:     2018/8/27 19:51
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service.impl;

import com.cxcy.zjb.springboot.domain.Company;
import com.cxcy.zjb.springboot.repository.CompanyRepository;
import com.cxcy.zjb.springboot.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/27
 * @since 1.0.0
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository repository;
    @Override
    public Company saveCompany(Company company) {
        return repository.save(company);
    }
}