package com.dddqmmx.surf.server.service;

import org.apache.ibatis.annotations.Param;

public interface RelationService {
    int addFriendRequest(@Param("userId") int userId,@Param("otherSideId") int otherSideId);
}
