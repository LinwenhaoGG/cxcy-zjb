/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: UserServiceImpl
 * Author:   KOLO
 * Date:     2018/8/16 17:55
 * Description: 用户service实现层
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service.impl;

import com.cxcy.zjb.springboot.Vo.*;
import com.cxcy.zjb.springboot.domain.*;
import com.cxcy.zjb.springboot.repository.*;
import com.cxcy.zjb.springboot.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * 〈一句话功能简述〉<br> 
 * 〈用户service实现层〉
 *
 * @author KOLO
 * @create 2018/8/16
 * @since 1.0.0
 */
@Service
public class UserServiceImpl implements UserService,UserDetailsService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private InMessageRepository inMessageRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public User findUser(User user) {
        return repository.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }

    @Override
    public User findUserbyUserId(String userId) {
        return repository.findOne(Long.parseLong(userId));
    }

    @Override
    public ArrayList<UserMessage> findMessageByUserId(String userId) {
        //1,根据userId查询所有未查阅的消息
        ArrayList<Inmessage> list = inMessageRepository.findByReceiverIdAndIsReadOrderByInsertTime(userId, 0);
        //2,有几个人的消息未查阅
        Set<String> UserIdSet = new HashSet<>();
        for (Inmessage inmessage:
             list) {
            UserIdSet.add(inmessage.getSenderId());
        }
        //3,遍历set
        ArrayList<UserMessage> messageArrayList  = new ArrayList<UserMessage>();

        for (String senderId:UserIdSet
             ) {
            int num = 0;
            User user = repository.findOne(Long.parseLong(senderId));

            for (Inmessage inmessage:list
                 ) {
                if (inmessage.getSenderId().equalsIgnoreCase(senderId)){
                    num++;
                }

            }
            UserMessage userMessage = new UserMessage();
            userMessage.setMessageNum(num);
            userMessage.setUserId(user.getId()+"");
            userMessage.setUserImage(user.getAvatar());
            userMessage.setUserName(user.getUsername());
            messageArrayList.add(userMessage);
        }


        return messageArrayList;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return repository.findByUsername(s);
    }

    @Override
    public User saveUserInfo(User userInfo) {
        return repository.save(userInfo);
    }

    /**
     * 根据用户账号来查询用户信息
     * @param uName
     * @return
     */
    @Override
    public User findUserInfo(String uName) {
        return repository.findByUsername(uName);
    }

    @Override
    public User findUserInfoByNameAndPwd(String username, String pwd) {
        return repository.findByUsernameAndPassword(username,pwd);
    }


    @Override
    public User findUserById(Long id) {
        return repository.findOne(id);
    }

    @Override
    public List<User> findUserListByStyle(Integer style) {
        return repository.findByStyle(style);
    }

    /**
     * //学生认证：查看未认证的学生信息
     * @param pageRequest
     * @return
     */
    @Override
    public ArrayList findStudentIdentificationList(PageRequest pageRequest) {
        Page<User> userList = repository.findByStateAndStyle(2, 1, pageRequest);
        //遍历集合，将其学生详细信息查询出来
        ArrayList<UserIdentification> list = new ArrayList<UserIdentification>();
        if (userList != null) {
            for (User user : userList
                    ) {
                UserIdentification userIdentification = new UserIdentification();
                BeanUtils.copyProperties(user, userIdentification);
                Long studentId = user.getStudent();
                Student student = studentRepository.findOne(studentId);
                BeanUtils.copyProperties(student, userIdentification);
                userIdentification.setId(user.getId());
                list.add(userIdentification);
            }
        }
        //存放学生数据和数据总条数的集合
        ArrayList resultList = new ArrayList();
        resultList.add(userList.getTotalPages());
        resultList.add(list);
        return resultList;
    }


    /**
     * 管理员：查看未认证的管理员信息
     * @param pageRequest
     * @return
     */
    @Override
    public ArrayList findAdminIdentification(PageRequest pageRequest) {
        Page<User> userList = repository.findByStateAndStyle(2, 4, pageRequest);
        //遍历集合，将其管理员详细信息查询出来
        ArrayList<AdminIdentification> list = new ArrayList<AdminIdentification>();
        if (userList != null){
            for (User user: userList
                    ) {
                AdminIdentification adminIdentification = new AdminIdentification();
                BeanUtils.copyProperties(user,adminIdentification);
                list.add(adminIdentification);
            }
        }
        //存放学生数据和数据总条数的集合
        ArrayList resultList = new ArrayList();
        resultList.add(userList.getTotalPages());
        resultList.add(list);
        return resultList;
    }

    /**
     * 管理员通过认证功能
     * @param id
     * @return
     */
    @Override
    public User passUserIdentification(Long id) {
        User user = repository.findOne(id);
        //通过认证
        user.setState(1);
        return repository.save(user);
    }

     /**
     * 学生管理：查找已认证学生list集合
     */
    @Override
    public ArrayList findCertifiedStudent(PageRequest pageRequest) {
      Page<User> userList = repository.findByStateAndStyle(1, 1, pageRequest);
        //遍历集合，将其学生详细信息查询出来
        ArrayList<UserIdentification> list = new ArrayList<UserIdentification>();
        if (userList != null){
            for (User user: userList
                    ) {
                UserIdentification userIdentification = new UserIdentification();
                BeanUtils.copyProperties(user,userIdentification);
                Long studentId = user.getStudent();
                Student student = studentRepository.findOne(studentId);
                BeanUtils.copyProperties(student,userIdentification);
                userIdentification.setId(user.getId());
                list.add(userIdentification);
            }
        }
        //存放学生数据和数据总条数的集合
        ArrayList resultList = new ArrayList();
        resultList.add(userList.getTotalPages());
        resultList.add(list);
        return resultList;
    }

    /**
     * 根据关键字查找指定用户
     * @param selectState
     * @param keyword
     * @return
     */
    @Override
    public ArrayList findSingleStudent(int selectState, String keyword,PageRequest pageRequest) {
        //根据selectState
        Page<User> userList = null;
        ArrayList resultList  = new ArrayList();
        switch (selectState){
            case 1 :
                //根据真实姓名来查(可能会有多个用户)
               userList =  repository.findByNameAndStyleAndState(keyword,1,1,pageRequest);
                break;
            case 2 :
                //根据手机号码来查(可能会有多个用户)
               userList =  repository.findByTelephoneAndStyleAndState(keyword,1,1,pageRequest) ;
                break;
            case 3 :
                //根据用户账号来查(肯定会是单一的用户)
               User user = repository.findByUsernameAndState(keyword,1);
               resultList.add(1);
                   UserIdentification userIdentification = new UserIdentification();
                    BeanUtils.copyProperties(user,userIdentification);
                    Long studentId = user.getStudent();
                    Student student = studentRepository.findOne(studentId);
                    BeanUtils.copyProperties(student,userIdentification);
                    userIdentification.setId(user.getId());
               resultList.add(userIdentification);
                break;
        }

        //获取学生的全部信息
        if (selectState != 3 && userList!= null) {  if (userList != null){
            for (User user: userList
                    ) {
                UserIdentification userIdentification = new UserIdentification();
                BeanUtils.copyProperties(user,userIdentification);
                Long studentId = user.getStudent();
                Student student = studentRepository.findOne(studentId);
                BeanUtils.copyProperties(student,userIdentification);
                userIdentification.setId(user.getId());
                resultList.add(userIdentification);
            }
            }
         }
         return resultList;
    }

    /**
     * 根据关键字查找指定老师
     * @param selectState
     * @param keyword
     * @return
     */
    @Override
    public ArrayList findTeacher(int selectState, String keyword, PageRequest pageRequest) {
         //根据selectState
        Page<User> userList = null;
        ArrayList resultList  = new ArrayList();
        switch (selectState){
            case 1 :
                //根据真实姓名来查(可能会有多个用户)
               userList =  repository.findByNameAndStyleAndState(keyword,2,1,pageRequest);
                break;
            case 2 :
                //根据手机号码来查(可能会有多个用户)
               userList =  repository.findByTelephoneAndStyleAndState(keyword,2,1,pageRequest) ;
                break;
            case 3 :
                //根据用户账号来查(肯定会是单一的用户)
               User user = repository.findByUsernameAndState(keyword,1);
               resultList.add(1);
                   TeacherIdentification teacherIdentification = new TeacherIdentification();
                    BeanUtils.copyProperties(user,teacherIdentification);
                    Long teacherId = user.getTeacher();
                    Teacher teacher = teacherRepository.findOne(teacherId);
                    BeanUtils.copyProperties(teacher,teacherIdentification);
                    teacherIdentification.setId(user.getId());
               resultList.add(teacherIdentification);
                break;
        }

        //获取学生的全部信息
        if (selectState != 3 && userList!= null) {  if (userList != null){
           for (User user : userList
                    ) {
                TeacherIdentification teacherIdentification = new TeacherIdentification();
                BeanUtils.copyProperties(user, teacherIdentification);
                Long teacherId = user.getTeacher();
                Teacher teacher = teacherRepository.findOne(teacherId);
                BeanUtils.copyProperties(teacher, teacherIdentification);
                teacherIdentification.setId(user.getId());
                resultList.add(teacherIdentification);
            }
            }
         }
         return resultList;
    }
    /**
     * 根据关键字查找指定企业
     * @param selectState
     * @param keyword
     * @return
     */
    @Override
    public ArrayList findCompany(int selectState, String keyword, PageRequest pageRequest) {
      //根据selectState
        Page<User> userList = null;
        ArrayList resultList  = new ArrayList();
        switch (selectState){
            case 1 :
                //根据企业名称来查
               List<Company>  companyList =  companyRepository.findByName(keyword);
               Set<Long> idSet = new HashSet<Long>();
                for (Company company: companyList
                     ) {
                    idSet.add(company.getId());
                }
                //寻找该用户其他信息
                User user = null;
                for (Long id: idSet
                     ) {
                    user = repository.findByStyleAndStateAndCompany(3,1,id);
                    if (user!=null){
                        break;
                    }
                }
               resultList.add(1);
                   CompanyIdentification companyIdentification = new CompanyIdentification();
                    BeanUtils.copyProperties(user,companyIdentification);
                    Long companyId = user.getCompany();
                    Company company = companyRepository.findOne(companyId);
                    BeanUtils.copyProperties(company,companyIdentification);
                    companyIdentification.setId(user.getId());
                    companyIdentification.setName(user.getName());
                    companyIdentification.setCompanyName(company.getName());
               resultList.add(companyIdentification);
                break;
            case 2 :
                //根据手机号码来查(可能会有多个用户)
               userList =  repository.findByTelephoneAndStyleAndState(keyword,3,1,pageRequest) ;
                break;
            case 3 :
                //根据企业社会代号来查(肯定会是单一的用户)
                ArrayList<Company> companyList1 = companyRepository.findByNumber(keyword);
                Set<Long> idSet1 = new HashSet<Long>();
                for (Company company1: companyList1
                     ) {
                    idSet1.add(company1.getId());
                }
                User user1 = null;
                for (Long id: idSet1
                     ) {
                    user1 = repository.findByStyleAndStateAndCompany(3,1,id);
                    if (user1!=null){
                        break;
                    }
                }
                resultList.add(1);
                   CompanyIdentification companyIdentification1 = new CompanyIdentification();
                    BeanUtils.copyProperties(user1,companyIdentification1);
                    Long companyId1 = user1.getCompany();
                    Company company1 = companyRepository.findOne(companyId1);
                    BeanUtils.copyProperties(company1,companyIdentification1);
                    companyIdentification1.setId(user1.getId());
                    companyIdentification1.setName(user1.getName());
                    companyIdentification1.setCompanyName(company1.getName());
               resultList.add(companyIdentification1);
                break;
        }

        //获取学生的全部信息
        if (selectState== 2 && userList!= null) {  if (userList != null){
           for (User user : userList
                    ) {
                CompanyIdentification companyIdentification = new CompanyIdentification();
                BeanUtils.copyProperties(user, companyIdentification);
                Long companyId = user.getCompany();
                Company company = companyRepository.findOne(companyId);
                BeanUtils.copyProperties(company, companyIdentification);
                companyIdentification.setId(user.getId());
                companyIdentification.setName(user.getName());
                companyIdentification.setCompanyName(company.getName());
                resultList.add(companyIdentification);
            }
            }
         }
         return resultList;
    }
     /**
     * 管理员管理：查找已认证管理员list集合
     */
    @Override
    public ArrayList findCertifiedAdmin(PageRequest pageRequest) {
        Page<User> userList = repository.findByStateAndStyle(1, 4, pageRequest);
        //遍历集合，将其学生详细信息查询出来
        ArrayList<BriefUser> list = new ArrayList<BriefUser>();
        if (userList != null){
            for (User user: userList
                    ) {
                BriefUser briefUser = new BriefUser();
                BeanUtils.copyProperties(user,briefUser);
                list.add(briefUser);
            }
        }
        //存放学生数据和数据总条数的集合
        ArrayList resultList = new ArrayList();
        resultList.add(userList.getTotalPages());
        resultList.add(list);
        return resultList;
    }

    /**
     * 根据关键字查找指定管理员
     * @param selectState
     * @return
     */
    @Override
    public ArrayList findAdmin(int selectState, String keyword, PageRequest pageRequest) {
       //根据selectState
        Page<User> userList = null;
        ArrayList resultList  = new ArrayList();
        switch (selectState){
            case 1 :
                //根据真实姓名来查(可能会有多个用户)
               userList =  repository.findByNameAndStyleAndState(keyword,4,1,pageRequest);
                break;
            case 2 :
                //根据手机号码来查(可能会有多个用户)
               userList =  repository.findByTelephoneAndStyleAndState(keyword,4,1,pageRequest) ;
                break;
            case 3 :
                //根据用户账号来查(肯定会是单一的用户)
               User user = repository.findByUsernameAndState(keyword,1);
               resultList.add(1);
                    BriefUser briefUser = new BriefUser();
                    BeanUtils.copyProperties(user,briefUser);
               resultList.add(briefUser);
                break;
        }

        //获取学生的全部信息
        if (selectState != 3 && userList!= null) {  if (userList != null){
            for (User user: userList
                    ) {
                BriefUser briefUser = new BriefUser();
                BeanUtils.copyProperties(user,briefUser);
                resultList.add(briefUser);
            }
            }
         }
         return resultList;
    }
}