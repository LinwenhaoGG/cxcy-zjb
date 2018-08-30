/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: companyVo
 * Author:   KOLO
 * Date:     2018/8/27 19:34
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.Vo;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

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
public class CompanyVo {
    @Size(min=2, max=20)
    @NotEmpty(message = "真实姓名不能为空")
    private String realName;  //用户真实姓名

    @NotEmpty(message = "公司名称不能为空")
    @Size(min=2, max=30)
    private String name; //公司名称

    @NotEmpty(message = "企业社会代码不能为空")
    private String number; //企业社会代码

    @NotEmpty(message = "公司法定代表人不能为空")
    private String boss; //公司法定代表人

    /*@NotEmpty(message = "营业执照不能为空")*/
    @Size(min=2, max=100)
    private String license; //营业执照

    @NotEmpty(message = "公司类型不能为空")
    private String type; //公司类型

    @NotEmpty(message = "联系人不能为空")
    private String contacts; //联系人

    @NotEmpty(message = "联系电话不能为空")
    private String phone; //联系电话
}