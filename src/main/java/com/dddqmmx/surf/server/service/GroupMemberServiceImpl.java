package com.dddqmmx.surf.server.service;

import com.dddqmmx.surf.server.mapper.GroupMemberMapper;
import com.dddqmmx.surf.server.pojo.GroupMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("groupMemberService")
public class GroupMemberServiceImpl implements GroupMemberService{

    final
    GroupMemberMapper groupMemberMapper;

    public GroupMemberServiceImpl(GroupMemberMapper groupMemberMapper) {
        this.groupMemberMapper = groupMemberMapper;
    }

    @Override
    public List<GroupMember> getGroupMemberListByGroupId(int groupId) {
        return groupMemberMapper.getGroupMemberListByGroupId(groupId);
    }

    @Override
    public int addGroupRequest(int userId, int groupId) {
        if (groupMemberMapper.hasRequest(userId, groupId) == 0){
            if (groupMemberMapper.insertGroupRequest(userId, groupId,0) == 1){
                return 0;
            }
            return 2;
        }
        return 1;
    }

    @Override
    public List<GroupMember> getAddGroupRequestListByUserId(int userId) {
        return groupMemberMapper.getAddGroupRequestListByUserId(userId);
    }
}
