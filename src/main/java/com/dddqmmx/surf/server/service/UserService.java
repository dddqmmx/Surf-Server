package com.dddqmmx.surf.server.service;

import com.dddqmmx.surf.server.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    User login(String userName, String userPass);
    User getUserById(int id);
    List<User> getUserFriendList(int userId);
}
