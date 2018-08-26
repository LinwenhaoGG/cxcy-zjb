package com.cxcy.zjb.springboot.Json;

import lombok.Data;

import java.util.List;

/**
 * Created by LINWENHAO on 2018/8/24.
 */
@Data
public class EventJson  {

    private String name;

    private List<String> userList;
}
