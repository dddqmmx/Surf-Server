package com.dddqmmx.surf.server.mapper;

import com.dddqmmx.surf.server.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {
    User getUserById(@Param("userName") String userName,@Param("password") String password);

    List<User> getUserFriendList(int userId);
}
