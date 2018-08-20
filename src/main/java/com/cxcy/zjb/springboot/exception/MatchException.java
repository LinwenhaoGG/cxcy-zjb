package com.cxcy.zjb.springboot.exception;

import com.cxcy.zjb.springboot.enums.ResultEnum;

/**
 * 比赛系统异常异常
 * Created by LINWENHAO on 2018/8/19.
 */

public class MatchException extends RuntimeException {

    private Integer code;

    public MatchException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());

        this.code = resultEnum.getCode();
    }

    public MatchException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
