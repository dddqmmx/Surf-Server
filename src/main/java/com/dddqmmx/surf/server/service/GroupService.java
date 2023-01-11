package com.dddqmmx.surf.server.service;

import com.dddqmmx.surf.server.pojo.Group;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupService {
    List<Group> geGroupListByUserId(int userId);
    Group getGroupInfo(int groupId);
}
