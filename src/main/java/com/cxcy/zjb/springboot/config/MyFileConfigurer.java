package com.cxcy.zjb.springboot.config;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MyFileConfigurer extends WebMvcConfigurerAdapter {
    // 获取配置文件中文件的路径
    @Value("${file.FilePath}")
    private String FilePath;


    // 访问文件的方法
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (FilePath.equals("") || FilePath.equals("${file.FilePath}")) {
            String filePath = MyFileConfigurer.class.getClassLoader().getResource("").getPath();
            if (filePath.indexOf(".jar") > 0) {
                filePath = filePath.substring(0, filePath.indexOf(".jar"));
            } else if (filePath.indexOf("classes") > 0) {
                filePath = "file:" + filePath.substring(0, filePath.indexOf("classes"));
            }
            filePath = filePath.substring(0, filePath.lastIndexOf("/")) + "/file/";
            FilePath = filePath;
            System.out.println(FilePath);
        }
        LoggerFactory.getLogger(MyFileConfigurer.class).info("filePath=" + FilePath);
        registry.addResourceHandler("/file/**").addResourceLocations(FilePath);
        super.addResourceHandlers(registry);
    }
}