/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: ResultUtil
 * Author:   KOLO
 * Date:     2018/8/17 9:01
 * Description: 返回工具
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.utils;

import com.cxcy.zjb.springboot.Vo.Result;

/**
 * 〈一句话功能简述〉<br> 
 * 〈返回工具〉
 *
 * @author KOLO
 * @create 2018/8/17
 * @since 1.0.0
 */
public class ResultUtil {
    public static Result success(Object object){
        Result result = new Result();
        result.setCode("1");
        result.setMsg("操作成功！");
        result.setObject(object);
        return result;

    }
    public static Result success(String msg){
        Result result = new Result();
        result.setCode("1");
        result.setMsg(msg);
        return result;

    }
    public static Result success(){
        Result result = new Result();
        result.setCode("1");
        result.setMsg("操作成功！");
        return result;

    }

    public static Result error(){
        Result result = new Result();
        result.setCode("0");
        result.setMsg("操作失败！");
        return result;
    }
    public static Result error(String msg){
        Result result = new Result();
        result.setCode("0");
        result.setMsg(msg);
        return result;
    }

}