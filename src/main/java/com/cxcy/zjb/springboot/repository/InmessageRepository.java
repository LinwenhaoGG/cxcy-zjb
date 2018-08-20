package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.Inmessage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *Inmessage 仓库
 *
 * Created by LINWENHAO on 2018/8/6.
 */
public interface InmessageRepository extends JpaRepository<Inmessage,Long> {
}
