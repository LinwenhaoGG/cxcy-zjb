package com.cxcy.zjb.springboot.Vo;

import lombok.Data;


@Data
public class UserChartsVo {
    Integer style;
    String name;
    Long count;

    public UserChartsVo(Integer style, Long count) {
        this.style = style;
        this.count = count;
    }
}
