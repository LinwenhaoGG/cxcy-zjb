package com.cxcy.zjb.springboot.Vo;

import lombok.Data;

/**
 * Created by LINWENHAO on 2018/12/19.
 */
@Data
public class ChartsValueCountVo {
    String name;
    Long value;


    public ChartsValueCountVo(String name, Long value) {
        this.name = name;
        this.value = value;
    }

    public ChartsValueCountVo(String name, int value) {
        this.name = name;
        this.value = (long)value;
    }
}
