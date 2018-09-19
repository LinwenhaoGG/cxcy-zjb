/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: TeacherServiceImpl
 * Author:   KOLO
 * Date:     2018/8/27 19:12
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service.impl;

import com.cxcy.zjb.springboot.Vo.TeacherIdentification;
import com.cxcy.zjb.springboot.domain.Teacher;
import com.cxcy.zjb.springboot.domain.User;
import com.cxcy.zjb.springboot.repository.TeacherRepository;
import com.cxcy.zjb.springboot.repository.UserRepository;
import com.cxcy.zjb.springboot.service.TeacherService;
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
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public Teacher saveTeacher(Teacher teacher) {
        return repository.save(teacher);
    }

    /**
     * 查看未认证的老师信息
     * @param pageRequest
     * @return
     */
    @Override
    public ArrayList findTeacherIdentificationList(PageRequest pageRequest) {
       Page<User> teacherList = userRepository.findByStateAndStyle(2, 2, pageRequest);
        //遍历集合，将其老师详细信息查询出来
        ArrayList<TeacherIdentification> list = new ArrayList<TeacherIdentification>();
        if (teacherList != null) {
            for (User user : teacherList
                    ) {
                TeacherIdentification teacherIdentification = new TeacherIdentification();
                BeanUtils.copyProperties(user, teacherIdentification);
                Long teacherId = user.getTeacher();
                Teacher teacher = repository.findOne(teacherId);
                BeanUtils.copyProperties(teacher, teacherIdentification);
                teacherIdentification.setId(user.getId());
                list.add(teacherIdentification);
            }
        }
        //存放数据和数据总条数的集合
        ArrayList resultList = new ArrayList();
        resultList.add(teacherList.getTotalElements());
        resultList.add(list);
        return resultList;
    }

    /**
     * 老师管理：查找已认证老师list集合
     */
    @Override
    public ArrayList findCertifiedTeacher(PageRequest pageRequest) {
        Page<User> teacherList = userRepository.findByStateAndStyle(1, 2, pageRequest);
        //遍历集合，将其老师详细信息查询出来
        ArrayList<TeacherIdentification> list = new ArrayList<TeacherIdentification>();
        if (teacherList != null) {
            for (User user : teacherList
                    ) {
                TeacherIdentification teacherIdentification = new TeacherIdentification();
                BeanUtils.copyProperties(user, teacherIdentification);
                Long teacherId = user.getTeacher();
                Teacher teacher = repository.findOne(teacherId);
                BeanUtils.copyProperties(teacher, teacherIdentification);
                teacherIdentification.setId(user.getId());
                list.add(teacherIdentification);
            }
        }
        //存放学生数据和数据总条数的集合
        ArrayList resultList = new ArrayList();
        resultList.add(teacherList.getTotalElements());
        resultList.add(list);
        return resultList;
    }
}