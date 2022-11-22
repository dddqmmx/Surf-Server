package com.dddqmmx.surf.server.service;

import com.dddqmmx.surf.server.pojo.GroupMember;
import org.springframework.stereotype.Service;

import java.util.List;


public interface GroupMemberService {
    List<GroupMember> getGroupMemberListByGroupId(int groupId);
}
