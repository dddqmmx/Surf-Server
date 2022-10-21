package com.dddqmmx.surf.server.service;

import com.dddqmmx.surf.server.mapper.UserMapper;
import com.dddqmmx.surf.server.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService{
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User getUserById(String userName, String userPass) {
        return userMapper.getUserById(userName, userPass);
    }
}
