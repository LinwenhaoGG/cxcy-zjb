package com.cxcy.zjb.springboot.utils;

import com.cxcy.zjb.springboot.Vo.ResultVO;

/**
 * date:   2018/7/26.0:48
 * author: zpr
 */
public class ResultUtils {

    public static ResultVO success(Object object){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMessage("成功");
        resultVO.setData(object);
        return resultVO;
    }

    public static ResultVO error(Integer code,String message){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMessage(message);
        return resultVO;
    }

    public static ResultVO success(){
        return success(null);
    }

}
