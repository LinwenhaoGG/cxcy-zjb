package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *GroupMember 仓库
 *
 * Created by LINWENHAO on 2018/8/6.
 */
public interface GroupMemberRepository extends JpaRepository<GroupMember,Long> {
}
