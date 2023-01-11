package com.dddqmmx.surf;

import com.dddqmmx.surf.server.SurfServerApplication;
import com.dddqmmx.surf.server.pojo.User;
import com.dddqmmx.surf.server.service.UserService;
import com.dddqmmx.surf.server.service.UserServiceImpl;
import com.dddqmmx.surf.server.util.BeanUtil;
import org.springframework.boot.SpringApplication;

public class Test {
    public static void main(String[] args) {
        SpringApplication.run(SurfServerApplication.class, args);
         UserService userService = BeanUtil.getBean(UserServiceImpl.class);
        User userById = userService.getUserById(5);
        System.out.println(userById);
    }
}
