package com.dddqmmx.surf.server.service;

import com.dddqmmx.surf.server.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;


public interface UserService {
    User getUserById(String userName, String userPass);
}
