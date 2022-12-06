package com.dddqmmx.surf.server.service;

import com.dddqmmx.surf.server.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    boolean hasUserName(String userName);
    User login(String userName, String userPass);
    boolean register(String userName,String password);
    User getUserById(int id);
    List<User> getUserFriendList(int userId);
}
