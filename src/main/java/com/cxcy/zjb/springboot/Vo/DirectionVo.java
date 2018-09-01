package com.cxcy.zjb.springboot.Vo;

import com.cxcy.zjb.springboot.domain.Catagorys;
import com.cxcy.zjb.springboot.domain.Direction;
import lombok.Data;

import java.util.List;

/**
 * 添加num，用于存储一个方向下的分类
 */
@Data
public class DirectionVo {
    //方向
    private Direction direction;
    //分类
    private List<Catagorys> catagorys;

    public DirectionVo(Direction direction, List<Catagorys> catagorys) {
        this.direction = direction;
        this.catagorys = catagorys;
    }
    public DirectionVo() {

    }
}
