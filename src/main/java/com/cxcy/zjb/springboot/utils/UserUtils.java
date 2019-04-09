package com.cxcy.zjb.springboot.utils;

import com.cxcy.zjb.springboot.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {

    public static User getUser() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!object.toString().equals("anonymousUser")) {
            return (User) object;
        } else {
            return null;
        }
    }
}
