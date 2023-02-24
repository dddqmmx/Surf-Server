package com.dddqmmx.surf;

import com.dddqmmx.surf.server.SurfServerApplication;
import com.dddqmmx.surf.server.pojo.User;
import com.dddqmmx.surf.server.service.UserService;
import com.dddqmmx.surf.server.service.UserServiceImpl;
import com.dddqmmx.surf.server.util.BeanUtil;
import com.dddqmmx.surf.server.util.FileUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.util.DigestUtils;

import java.io.FileInputStream;
import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        SpringApplication.run(SurfServerApplication.class, args);
        int id = 12;
        System.out.println(FileUtil.getFileName(id).length());
        try {
            System.out.println(DigestUtils.md5DigestAsHex(new FileInputStream(FileUtil.getFileName(id))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
