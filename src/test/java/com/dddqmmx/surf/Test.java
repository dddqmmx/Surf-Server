package com.dddqmmx.surf;

import com.dddqmmx.surf.server.SurfServerApplication;
import com.dddqmmx.surf.server.pojo.User;
import com.dddqmmx.surf.server.service.UserService;
import com.dddqmmx.surf.server.service.UserServiceImpl;
import com.dddqmmx.surf.server.util.BeanUtil;
import com.dddqmmx.surf.server.util.FileUtil;
import org.springframework.boot.SpringApplication;

public class Test {
    public static void main(String[] args) {
        SpringApplication.run(SurfServerApplication.class, args);
        System.out.println(FileUtil.getFileName(5).length());
    }
}
