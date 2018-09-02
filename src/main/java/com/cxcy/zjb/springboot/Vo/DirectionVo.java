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
    private Integer catagorysNum;

    public DirectionVo(Direction direction, Integer catagorysNum) {
        this.direction = direction;
        this.catagorysNum = catagorysNum;
    }

    public DirectionVo() {

    }
}
