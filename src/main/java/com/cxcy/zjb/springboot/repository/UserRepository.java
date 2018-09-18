package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

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
     * 学生认证：查看未认证的学生信息
     * @param state
     * @param style
     * @param pageable
     * @return
     */
    public Page<User> findByStateAndStyle(@Param("state") int state,@Param("style") int style,Pageable pageable);

    /**
     * 根据用户真实姓名查找用户
     * @param name
     * @return
     */
    public Page<User> findByNameAndStyleAndState(@Param("name") String name,@Param("style")int style,@Param("state")int state, Pageable pageable);
    /**
     *根据手机号码查询用户
     */
    public Page<User> findByTelephoneAndStyleAndState(@Param("telephone") String telephone,@Param("style")int style,@Param("state")int state, Pageable pageable);
    /**
     * 根据用户账号，查找是否存在已认证成功的用户
     */
    public User findByUsernameAndState(@Param("username")String username,@Param("state")int state);

    /**
     * 查找已认证商家
     * @param style
     * @param state
     * @param cId
     * @return
     */
    public User findByStyleAndStateAndCompany(@Param("style")int style,@Param("state")int state,@Param("cId")Long cId);

}
