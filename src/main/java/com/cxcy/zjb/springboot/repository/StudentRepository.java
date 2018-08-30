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

import com.cxcy.zjb.springboot.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/27
 * @since 1.0.0
 */
public interface StudentRepository extends JpaRepository<Student,Long> {

}