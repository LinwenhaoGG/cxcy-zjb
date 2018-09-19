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

import com.cxcy.zjb.springboot.Vo.CompanyIdentification;
import com.cxcy.zjb.springboot.domain.Company;
import com.cxcy.zjb.springboot.domain.User;
import com.cxcy.zjb.springboot.repository.CompanyRepository;
import com.cxcy.zjb.springboot.repository.UserRepository;
import com.cxcy.zjb.springboot.service.CompanyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
    @Autowired
    private UserRepository userRepository;
    @Override
    public Company saveCompany(Company company) {
        return repository.save(company);
    }

     /**
     * 查看未认证的企业信息
     * @param pageRequest
     * @return
     */
    @Override
    public ArrayList findCompanyIdentification(PageRequest pageRequest) {
       Page<User> companyList = userRepository.findByStateAndStyle(2, 3, pageRequest);
        //遍历集合，将其老师详细信息查询出来
        ArrayList<CompanyIdentification> list = new ArrayList<CompanyIdentification>();
         if (companyList != null) {
             for (User user : companyList
                     ) {
                 CompanyIdentification companyIdentification = new CompanyIdentification();
                 BeanUtils.copyProperties(user, companyIdentification);
                 Long companyId = user.getCompany();
                 Company company = repository.findOne(companyId);
                 BeanUtils.copyProperties(company, companyIdentification);
                 companyIdentification.setId(user.getId());
                 companyIdentification.setCompanyName(company.getName());
                 companyIdentification.setName(user.getName());
                 list.add(companyIdentification);
             }
         }
        //存放学生数据和数据总条数的集合
        ArrayList resultList = new ArrayList();
        resultList.add(companyList.getTotalElements());
        resultList.add(list);
        return resultList;
    }

     /**
     * 根据查找已认证企业
     * @return
     */
    @Override
    public ArrayList findCertifiedTeacher(PageRequest pageRequest) {
       Page<User> companyList = userRepository.findByStateAndStyle(1, 3, pageRequest);
        //遍历集合，将其老师详细信息查询出来
        ArrayList<CompanyIdentification> list = new ArrayList<CompanyIdentification>();
        if (companyList != null) {
            for (User user : companyList
                    ) {
                CompanyIdentification companyIdentification = new CompanyIdentification();
                BeanUtils.copyProperties(user, companyIdentification);
                Long companyId = user.getCompany();
                Company company = repository.findOne(companyId);
                BeanUtils.copyProperties(company, companyIdentification);
                companyIdentification.setId(user.getId());
                 companyIdentification.setCompanyName(company.getName());
                 companyIdentification.setName(user.getName());
                list.add(companyIdentification);
            }
        }
        //存放学生数据和数据总条数的集合
        ArrayList resultList = new ArrayList();
        resultList.add(companyList.getTotalElements());
        resultList.add(list);
         return resultList;
    }
}