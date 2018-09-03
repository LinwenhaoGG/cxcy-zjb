package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class AdminControllerTest {

    @Autowired
    private AdminController adminController;

    @Test
    public void studentIdentificationList() {

        Result result = adminController.studentIdentificationList(1, 2);
        log.info(result.toString());

    }
    @Test
    public void teacherIdentification(){
        Result result = adminController.teacherIdentification(1,2);
        log.info(result.toString());
    }

    @Test
    public void companyIdentification(){
         Result result = adminController.companyIdentification(1,2);
        log.info(result.toString());
    }

    @Test
    public void adminIdentification(){
        Result result = adminController.adminIdentification(1, 2);

        log.info(result.toString());
    }

    @Test
    public void passIdentification(){
        Result result = adminController.passIdentification((long) 1);
        log.info(result.toString());
    }

    @Test
    public void findCertifiedStudent(){
        Result result = adminController.findCertifiedStudent(1, 5);
        log.info(result.toString());
    }
    @Test
    public void findSingleStudent(){
        Result result = adminController.findSingleStudent(3, "yiyi51", 1, 5);
        log.info(result.toString());
    }

    @Test
    public void findCertifiedTeacher(){
        Result result = adminController.findCertifiedTeacher(1, 5);
        log.info(result.toString());
    }

    @Test
    public void findTeacher(){
        Result result = adminController.findTeacher(3,"eiei0",1, 5);
        log.info(result.toString());
    }
     @Test
    public void findCertifiedCompany(){
        Result result = adminController.findCertifiedCompany(1, 5);
        log.info(result.toString());
    }

    @Test
    public void findCompany(){
        Result result = adminController.findCompany(3,"261542361",1, 5);
        log.info(result.toString());
    }
    @Test
    public void findCertifiedAdmin(){
        Result result = adminController.findCertifiedAdmin(1, 5);
        log.info(result.toString());
    }

    @Test
    public void findAdmin(){
        Result result = adminController.findAdmin(3,"rrr",1, 5);
        log.info(result.toString());
    }
}