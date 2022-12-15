package com.dddqmmx.surf.server.mapper;

import com.dddqmmx.surf.server.pojo.Relation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface RelationMapper {
    int insertRelation(@Param("userId") int userId,@Param("otherSideId") int otherSideId,@Param("relationType") int relationType);
    List<Relation> getFriendRequestByUserId(int userId);
    int hasRequest(@Param("userId") int userId,@Param("otherSideId") int otherSideId);
    int updateRelation(@Param("userId") int userId,@Param("otherSideId") int otherSideId,@Param("relationType") int relationType);
    Relation getFriendRequestByUserIdAndOtherSideId(@Param("userId") int userId,@Param("otherSideId") int otherSideId);
}
