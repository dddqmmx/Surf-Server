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
}
