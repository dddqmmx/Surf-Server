package com.dddqmmx.surf.server.service;

import com.dddqmmx.surf.server.mapper.RelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
