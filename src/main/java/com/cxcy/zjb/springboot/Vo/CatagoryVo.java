package com.cxcy.zjb.springboot.Vo;

import com.cxcy.zjb.springboot.domain.Catagorys;
import lombok.Data;

@Data
public class CatagoryVo {
    //分类
    private Catagorys catagorys;
    //分类下作品的数量
    private Integer proNum;

    public CatagoryVo(Catagorys catagorys, Integer proNum) {
        this.catagorys = catagorys;
        this.proNum = proNum;
    }
    public CatagoryVo(){

    }
}

