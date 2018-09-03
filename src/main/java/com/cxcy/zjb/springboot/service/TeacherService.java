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

import com.cxcy.zjb.springboot.domain.Teacher;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/27
 * @since 1.0.0
 */
public interface TeacherService {
    public Teacher saveTeacher(Teacher teacher);

    /**
     *查看未认证的老师信息
     * @param pageRequest
     * @return
     */
    public ArrayList findTeacherIdentificationList(PageRequest pageRequest);

    /**
     * 老师管理：查找已认证老师list集合
     */
    public ArrayList findCertifiedTeacher(PageRequest pageRequest);
}