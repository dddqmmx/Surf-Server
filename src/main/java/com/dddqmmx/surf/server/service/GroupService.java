package com.dddqmmx.surf.server.service;

import com.dddqmmx.surf.server.pojo.Group;

import java.util.List;

public interface GroupService {
    List<Group> geGroupListByUserId(int userId);
}
