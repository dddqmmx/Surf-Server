package com.dddqmmx.surf.server.service;

import com.dddqmmx.surf.server.pojo.Relation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RelationService {
    int addFriendRequest(@Param("userId") int userId,@Param("otherSideId") int otherSideId);
    List<Relation> getFriendRequestByUserId(int userId);
    int agreeRequest(@Param("userId") int userId,@Param("otherSideId") int otherSideId);

}
