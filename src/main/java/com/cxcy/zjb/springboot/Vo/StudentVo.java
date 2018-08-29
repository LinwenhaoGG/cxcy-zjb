/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: StudentVo
 * Author:   KOLO
 * Date:     2018/8/27 11:08
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.Vo;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.validation.constraints.Size;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/27
 * @since 1.0.0
 */
@Data
public class StudentVo {
    @Size(min=2, max=20)
    @NotEmpty(message = "真实姓名不能为空")
    private String name;  //用户真实姓名

    @NotEmpty(message = "年级信息不能为空")
    private String  edu;//年级

    @NotEmpty(message = "学院信息不能为空")
    @Size(min=2, max=30)
    private String college; //学院

    @NotEmpty(message = "专业班级不能为空")
    @Size(min=2, max=50)
    private String classes; //专业班级

    @NotEmpty(message = "学号不能为空")
    private String number;  // 学号

    @NotEmpty(message = "民族信息不能为空")
    private String nation; //民族

    @NotEmpty(message = "政治面貌信息不能为空")
    private String politicsstatus; //政治面貌

    @Lob  // 大对象，映射 MySQL 的 Long Text 类型
    @Basic(fetch=FetchType.LAZY) // 懒加载
    @NotEmpty(message = "个人简介不能为空")
    @Size(min=2)
    private String credential;           //个人简介

}