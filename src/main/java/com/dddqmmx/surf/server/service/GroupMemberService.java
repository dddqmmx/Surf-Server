package com.dddqmmx.surf.server.service;

import com.dddqmmx.surf.server.pojo.GroupMember;

import java.util.List;


public interface GroupMemberService {
    List<GroupMember> getGroupMemberListByGroupId(int groupId);
    int addGroupRequest(int userId,int groupId);
    List<GroupMember> getAddGroupRequestListByUserId(int userId);
    boolean agreeGroupRequest(int groupMemberId);
}
