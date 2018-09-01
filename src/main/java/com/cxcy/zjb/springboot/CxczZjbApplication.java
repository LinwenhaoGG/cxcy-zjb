package com.cxcy.zjb.springboot;

import com.cxcy.zjb.springboot.domain.User;
import com.cxcy.zjb.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpSession;

@SpringBootApplication
@Configuration
public class CxczZjbApplication {

	public static void main(String[] args) {

		SpringApplication.run(CxczZjbApplication.class, args);
	}

}
