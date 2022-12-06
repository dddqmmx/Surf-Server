package com.dddqmmx.surf.server.service;

import com.dddqmmx.surf.server.mapper.UserMapper;
import com.dddqmmx.surf.server.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService{
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public boolean hasUserName(String userName) {
        return userMapper.hasUserName(userName) > 0;
    }

    @Override
    public User login(String userName, String userPass) {
        return userMapper.login(userName, userPass);
    }

    @Override
    public boolean register(String userName, String password) {
        return userMapper.register(userName,password) > 0;
    }

    @Override
    public User getUserById(int id) {
        return userMapper.getUserById(id);
    }

    @Override
    public List<User> getUserFriendList(int userId) {
        return userMapper.getUserFriendList(userId);
    }
}
