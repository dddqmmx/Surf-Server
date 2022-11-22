package com.dddqmmx.surf.server.mapper;

import com.dddqmmx.surf.server.pojo.GroupMember;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface GroupMemberMapper {
    List<GroupMember> getGroupMemberListByGroupId(int groupId);
}
