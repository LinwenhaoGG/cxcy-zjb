package com.cxcy.zjb.springboot.enums;

import lombok.Getter;

/**
 * 返回前端数据
 * Created by LINWENHAO on 2018/8/19.
 */
@Getter
public enum ResultEnum {
    SUCCESS(0,"成功"),
    PARAM_ERROR(1,"参数不正确"),
    NO_PRODUCTION(2,"没有此类作品"),
    ;



    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
