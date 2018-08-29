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

import com.cxcy.zjb.springboot.domain.Teacher;
import com.cxcy.zjb.springboot.repository.TeacherRepository;
import com.cxcy.zjb.springboot.service.TeacherService;
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
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherRepository repository;
    @Override
    public Teacher saveTeacher(Teacher teacher) {
        return repository.save(teacher);
    }
}