/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: TeacherRepository
 * Author:   KOLO
 * Date:     2018/8/27 19:11
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.Vo.UserTeacherVo;
import com.cxcy.zjb.springboot.domain.Teacher;
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
public interface TeacherRepository extends JpaRepository<Teacher,Long> {

    /**
     * 获取对应状态的用户教师信息
     * @param state
     * @param pageable
     * @return
     */
    @Query(value = "select new com.cxcy.zjb.springboot.Vo.UserTeacherVo(u.id, u.name, u.email,u.telephone, u.username, u.avatar, u.state, u.style, " +
            "t.college, t.school, t.specially, t.number, t.nation,t.position, t.politicsstatus, t.credential) " +
            " from User u,Teacher t where u.style = 2 and u.state = ?1 and u.teacher = t.id")
    Page<UserTeacherVo> findUserTeacherByState(Integer state, Pageable pageable);

    /**
     * 获取所有的用户教师信息
     * @return
     */
    @Query(value = "select new com.cxcy.zjb.springboot.Vo.UserTeacherVo(u.id, u.name, u.email,u.telephone, u.username, u.avatar, u.state, u.style, u.sex, u.isUse, " +
            "t.id, t.college, t.school, t.specially, t.number, t.nation,t.position, t.politicsstatus, t.credential) " +
            " from User u,Teacher t where u.teacher = t.id")
    List<UserTeacherVo> findTeacherList();

    /**
     * 根据教师姓名获取所有的用户教师信息
     * @return
     */
    @Query(value = "select new com.cxcy.zjb.springboot.Vo.UserTeacherVo(u.id, u.name, u.email,u.telephone, u.username, u.avatar, u.state, u.style, u.sex, u.isUse, " +
            "t.id, t.college, t.school, t.specially, t.number, t.nation,t.position, t.politicsstatus, t.credential) " +
            " from User u,Teacher t where u.teacher = t.id and u.name like %?1%")
    List<UserTeacherVo> findTeacherListByName(String personName);


    /**
     * 根据用户id查找对应的教师信息
     * @param id
     * @return
     */
    @Query(value = "select new com.cxcy.zjb.springboot.Vo.UserTeacherVo(u.id, u.name, u.email,u.telephone, u.username, u.avatar, u.state, u.style, u.sex, u.isUse, " +
            "t.id, t.college, t.school, t.specially, t.number, t.nation,t.position, t.politicsstatus, t.credential) " +
            " from User u,Teacher t where u.teacher = t.id and u.id = ?1")
    UserTeacherVo findById(Long id);
}
