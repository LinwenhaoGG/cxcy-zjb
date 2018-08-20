/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: UUIDUtil
 * Author:   KOLO
 * Date:     2018/8/18 21:27
 * Description: 获取UUID
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.utils;

import java.util.UUID;

/**
 * 〈一句话功能简述〉<br> 
 * 〈获取UUID〉
 *
 * @author KOLO
 * @create 2018/8/18
 * @since 1.0.0
 */
public class UUIDUtil {
    /**
     * 获取UUID
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
}