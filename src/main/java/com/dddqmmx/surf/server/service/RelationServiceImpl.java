package com.dddqmmx.surf.server.service;

import com.dddqmmx.surf.server.mapper.RelationMapper;
import com.dddqmmx.surf.server.pojo.Relation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("relationService")
public class RelationServiceImpl implements RelationService{

    private final RelationMapper relationMapper;

    public RelationServiceImpl(RelationMapper relationMapper) {
        this.relationMapper = relationMapper;
    }

    @Override
    public int addFriendRequest(int userId, int otherSideId) {
        /*relationMapper.insertRelation()*/
        if (relationMapper.hasRequest(userId, otherSideId) == 0){
            if (relationMapper.insertRelation(userId, otherSideId,0) == 1){
                return 0;
            }
            return 2;
        }
        return 1;
    }

    @Override
    public List<Relation> getFriendRequestByUserId(int userId) {
        return relationMapper.getFriendRequestByUserId(userId);
    }

    @Override
    public int agreeRequest(@Param("userId") int userId, @Param("otherSideId") int otherSideId) {
        if (relationMapper.updateRelation(userId,otherSideId,1) > 0){
            Relation friendRequest = relationMapper.getFriendRequestByUserIdAndOtherSideId(otherSideId, userId);
            if (friendRequest != null){
                if (relationMapper.updateRelation(otherSideId,userId,1) > 0){
                    return 0;
                }
            }else {
                if (relationMapper.insertRelation(otherSideId,userId,1) > 0){
                    return 0;
                }
            }
        }
        return 1;
    }

}
