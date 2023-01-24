package com.dddqmmx.surf.server.mapper;

import com.dddqmmx.surf.server.pojo.GroupMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface GroupMemberMapper {
    List<GroupMember> getGroupMemberListByGroupId(int groupId);
    int insertGroupRequest(@Param("userId") int userId,@Param("groupId") int groupId,@Param("permissionId") int permissionId);
    int hasRequest(@Param("userId") int userId,@Param("groupId") int groupId);
    List<GroupMember> getAddGroupRequestListByUserId(int userId);
    int updatePermissionId(@Param("groupMemberId") int groupMemberId,@Param("permissionId") int permissionId);
}
