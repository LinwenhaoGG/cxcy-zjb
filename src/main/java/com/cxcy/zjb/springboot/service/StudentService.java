/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: StudentService
 * Author:   KOLO
 * Date:     2018/8/27 15:37
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service;

import com.cxcy.zjb.springboot.Vo.UserStudentVo;
import com.cxcy.zjb.springboot.domain.Student;
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
public interface StudentService {
    /**
     * 保存学生信息
     * @param student
     * @return
     */
    public Student saveStudent(Student student);

    /**
     * 获取学生信息
     * @param id
     * @return
     */
    public Student getStudent(Long id);

    /**
     * 通过用户状态获取学生信息
     * @param style
     * @param state
     * @return
     */
    Page<UserStudentVo> getUserStudentVoByStateAndStyle(Integer style, Integer state, Pageable pageable);

    /**
     *通过用户id获取学生信息
     * @param id
     * @return
     */
    UserStudentVo findById(Long id);

    /**
     * 根据用户姓名查找出对应的信息
     * @return
     */
    List<UserStudentVo> findStudentList(String personName);

}
