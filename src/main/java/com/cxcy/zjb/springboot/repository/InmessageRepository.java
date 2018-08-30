/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: InMessageRepository
 * Author:   KOLO
 * Date:     2018/8/17 11:57
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.Inmessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/17
 * @since 1.0.0
 */
public interface InMessageRepository extends JpaRepository<Inmessage,String> {


    public ArrayList<Inmessage> findByReceiverIdAndIsReadOrderByInsertTime(@Param("receiverId") String receiverId,
                                                                           @Param("isRead") int read);

    public ArrayList<Inmessage> findBySenderIdAndReceiverIdAndIsReadOrderByInsertTime(@Param("senderId") String senderId,
                                                                                      @Param("receiverId") String receiverId,
                                                                                      @Param("isRead") int isRead);



}