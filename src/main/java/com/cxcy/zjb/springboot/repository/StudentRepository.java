/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: StudentRepository
 * Author:   KOLO
 * Date:     2018/8/27 15:15
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.Vo.UserStudentVo;
import com.cxcy.zjb.springboot.domain.Student;
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
public interface StudentRepository extends JpaRepository<Student,Long> {

    /**
     * 获取对应状态的用户学生信息
     * @param state
     * @param pageable
     * @return
     */
    @Query(value = "select new com.cxcy.zjb.springboot.Vo.UserStudentVo(u.id, u.name, u.email,u.telephone, u.username, u.avatar, u.state, u.style, u.isUse, " +
            "s.edu, s.college, s.classes, s.number, s.nation, s.politicsstatus, s.credential) " +
            " from User u,Student s where u.style = ?1 and u.state = ?2 and u.student = s.id")
    Page<UserStudentVo> findByStyleAndState(Integer style, Integer state, Pageable pageable);


    /**
     * 获取所有学生信息
     * @return
     */
    @Query(value = "select new com.cxcy.zjb.springboot.Vo.UserStudentVo(u.id, u.name, u.email,u.telephone, u.username, u.avatar, u.state, u.style, u.sex, u.isUse, " +
            "s.id, s.edu, s.college, s.classes, s.number, s.nation, s.politicsstatus, s.credential) " +
            " from User u,Student s where u.student = s.id")
    List<UserStudentVo> findStudentList();

    /**
     * 根据名称 获取所有学生信息
     * @return
     */
    @Query(value = "select new com.cxcy.zjb.springboot.Vo.UserStudentVo(u.id, u.name, u.email,u.telephone, u.username, u.avatar, u.state, u.style, u.sex, u.isUse, " +
            "s.id, s.edu, s.college, s.classes, s.number, s.nation, s.politicsstatus, s.credential) " +
            " from User u,Student s where u.student = s.id and u.name like %?1%")
    List<UserStudentVo> findStudentListByPersonName(String personName);

    /**
     * 根据id获取对应的用户学生信息
     * @return
     */
    @Query(value = "select new com.cxcy.zjb.springboot.Vo.UserStudentVo(u.id, u.name, u.email,u.telephone, u.username, u.avatar, u.state, u.style, u.sex, u.isUse, " +
            "s.id, s.edu, s.college, s.classes, s.number, s.nation, s.politicsstatus, s.credential) " +
            " from User u,Student s where u.id = ?1 and u.student = s.id")
    UserStudentVo findById(Long id);
}