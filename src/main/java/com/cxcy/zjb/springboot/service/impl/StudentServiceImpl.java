/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: StudentServiceImpl
 * Author:   KOLO
 * Date:     2018/8/20 10:41
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service.impl;

import com.cxcy.zjb.springboot.Vo.UserStudentVo;
import com.cxcy.zjb.springboot.domain.Student;
import com.cxcy.zjb.springboot.repository.StudentRepository;
import com.cxcy.zjb.springboot.service.StudentService;
import com.cxcy.zjb.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/20
 * @since 1.0.0
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    private UserService userService;

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Page<UserStudentVo> getUserStudentVoByStateAndStyle(Integer style, Integer state, Pageable pageable) {
        return studentRepository.findByStyleAndState(style, state, pageable);
    }

    @Override
    public UserStudentVo findById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    public List<UserStudentVo> findStudentList(String personName) {
        if (StringUtils.isEmpty(personName)) {
            return studentRepository.findStudentList();
        }
        return studentRepository.findStudentListByPersonName(personName);
    }
}