package com.cxcy.zjb.springboot.Vo;

import com.cxcy.zjb.springboot.domain.Production;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class ProductionVo {

    //方向
    private String direction;
    //分类
    private String catagorys;
    //作者名字
    private String userName;
    //作品
    private Production production;

    @Override
    public String toString() {
        return "ProductionVo{" +
                "direction='" + direction + '\'' +
                ", catagorys='" + catagorys + '\'' +
                ", userName='" + userName + '\'' +
                ", production=" + production +
                '}';
    }
}
