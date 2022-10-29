package com.dddqmmx.surf.server.mapper;

import com.dddqmmx.surf.server.pojo.Group;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface GroupMapper {
    List<Group> geGroupListByUserId(int userId);
    Group getGroupInfo(int groupId);
}
