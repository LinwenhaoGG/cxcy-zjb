/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: Result
 * Author:   KOLO
 * Date:     2018/8/17 8:59
 * Description: 返回对象
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.Vo;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br> 
 * 〈返回对象〉
 *
 * @author KOLO
 * @create 2018/8/17
 * @since 1.0.0
 */
@Data
public class Result {
    private String  msg;
    //0为错误，1位正常
    private String code;
    private Object object;
    public Result(){}

    public Result(String msg, String code, Object object) {
        this.msg = msg;
        this.code = code;
        this.object = object;
    }
}