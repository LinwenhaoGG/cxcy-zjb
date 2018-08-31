package com.cxcy.zjb.springboot.Vo;

import com.cxcy.zjb.springboot.domain.InformationCategory;
import com.cxcy.zjb.springboot.domain.User;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Data
public class InformationVo {

    private Long id;//资讯分类的唯一标识

    private String title;  //标题

    private Timestamp createTime;




}
