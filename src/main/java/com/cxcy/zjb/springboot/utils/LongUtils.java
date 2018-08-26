package com.cxcy.zjb.springboot.utils;

/**
 * Long类型判断工具
 * Created by LINWENHAO on 2018/8/25.
 */
public class LongUtils {
    public static boolean  IsNone(Long text){
        if (text == null || text.equals(0)) {
            return true;
        } else {
            return false;
        }
    }
}
