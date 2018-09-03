package com.cxcy.zjb.springboot.service.impl;

import com.cxcy.zjb.springboot.domain.Browse;
import com.cxcy.zjb.springboot.repository.BrowseRepository;
import com.cxcy.zjb.springboot.service.BrowseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrowseServiceImpl implements BrowseService {

    @Autowired
    private BrowseRepository browseRepository;

    @Override
    public Browse findCatagoryByUserId(Long userId) {
        return browseRepository.findByUser(userId);
    }

    @Override
    public Browse saveLastBrowse(Browse browse) {
        return browseRepository.save(browse);
    }
}
