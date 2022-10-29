package com.dddqmmx.surf.server.service;

import com.dddqmmx.surf.server.mapper.GroupMapper;
import com.dddqmmx.surf.server.mapper.RelationMapper;
import com.dddqmmx.surf.server.pojo.Group;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("groupService")
public class GroupServiceImpl implements GroupService{
    private final GroupMapper groupMapper;

    public GroupServiceImpl(GroupMapper groupMapper) {
        this.groupMapper = groupMapper;
    }

    @Override
    public List<Group> geGroupListByUserId(int userId) {
        return groupMapper.geGroupListByUserId(userId);
    }

    @Override
    public Group getGroupInfo(int groupId) {
        return groupMapper.getGroupInfo(groupId);
    }
}
