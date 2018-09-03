package com.cxcy.zjb.springboot.config;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MyVideoConfigurer extends WebMvcConfigurerAdapter {
    // 获取配置文件中视频的路径
    @Value("${video.VideoPath}")
    private String VideoPath;


    // 访问视频的方法
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (VideoPath.equals("") || VideoPath.equals("${video.VideoPath}")) {
            String imagesPath = MyVideoConfigurer.class.getClassLoader().getResource("").getPath();
            if (imagesPath.indexOf(".jar") > 0) {
                imagesPath = imagesPath.substring(0, imagesPath.indexOf(".jar"));
            } else if (imagesPath.indexOf("classes") > 0) {
                imagesPath = "file:" + imagesPath.substring(0, imagesPath.indexOf("classes"));
            }
            imagesPath = imagesPath.substring(0, imagesPath.lastIndexOf("/")) + "/temp/";
            VideoPath = imagesPath;
            System.out.println(VideoPath);
        }
        LoggerFactory.getLogger(MyVideoConfigurer.class).info("imagesPath=" + VideoPath);
        registry.addResourceHandler("/temp/**").addResourceLocations(VideoPath);
        super.addResourceHandlers(registry);
    }
}
