package com.cxcy.zjb.springboot.service;

import com.cxcy.zjb.springboot.domain.Browse;

public interface BrowseService {

    //通过用户id查找该用户最后一次浏览的记录
    Browse findCatagoryByUserId(Long userId);
}
