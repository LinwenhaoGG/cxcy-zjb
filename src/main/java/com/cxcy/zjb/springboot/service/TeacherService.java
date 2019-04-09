/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: TeacherService
 * Author:   KOLO
 * Date:     2018/8/27 19:12
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service;

import com.cxcy.zjb.springboot.Vo.UserTeacherVo;
import com.cxcy.zjb.springboot.domain.Teacher;
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
public interface TeacherService {

    /**
     * 保存教师信息
     * @param teacher
     * @return
     */
    public Teacher saveTeacher(Teacher teacher);

    /**
     * 通过用户状态获取教师信息
     * @param state
     * @return
     */
    Page<UserTeacherVo> findUserTeacherByState(Integer state, Pageable pageable);

    /**
     * 根据id查找对应的所有信息
     * @param id   用户id
     * @return
     */
    UserTeacherVo findTeacherById(Long id);

    /**
     * 根据用户姓名查找出列表
     * @param personName
     * @return
     */
    List<UserTeacherVo> findTeacherListByName(String personName);
}