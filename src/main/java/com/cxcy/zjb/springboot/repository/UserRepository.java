package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.Vo.UserChartsVo;
import com.cxcy.zjb.springboot.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 用户仓库.
 *
 * @since 1.0.0 2017年3月2日
 * @author <a href="https://waylau.com">Way Lau</a> 
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 根据用户姓名分页查询用户列表
     * @param name
     * @param pageable
     * @return
     */
   Page<User> findByNameLike(String name, Pageable pageable);

    /**
     * 根据用户账号查询用户
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 根据名称列表查询用户列表
     * @param usernames
     * @return
     */
    List<User> findByusernameIn(Collection<String> usernames);

    /**
     * 根据类型查询用户列表
     * @param style 用户类型
     * @return
     */
    List<User> findByStyle(Integer style);
    /**
     * 根据用户账号和密码查询用户信息
     * @return
     */
    public User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    /**
     * 根据类型和审核状态查询出用户列表
     * @param style
     * @param state
     * @return
     */
    Page<User> findByStyleAndState(Integer style, Integer state, Pageable pageable);

 /**
  * 给用户增加角色
  * @param userId
  * @param auId
  */
 @Transactional
 @Modifying
 @Query(value = "INSERT INTO user_authority VALUES(?1,?2)", nativeQuery = true)
    int giveUserAuthority(Long userId, Long auId);


 /**
  * 获取用户统计类型
  * @return
  */
 @Query(value = "select new com.cxcy.zjb.springboot.Vo.UserChartsVo(u.style, count(u.id))" +
         "from User u GROUP BY u.style")
 List<UserChartsVo> getUserChartsCount();
}
