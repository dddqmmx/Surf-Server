package com.dddqmmx.surf.server.service;

import com.dddqmmx.surf.server.mapper.SurfFileMapper;
import com.dddqmmx.surf.server.pojo.SurfFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("surfFileService")
public class SurfFileServiceImpl implements SurfFileService {

    @Autowired
    SurfFileMapper surfFileMapper;

    @Override
    public SurfFile getFileByMD5(String md5) {
        return surfFileMapper.getFileByMD5(md5);
    }

    @Override
    public SurfFile getFileById(int id) {
        return surfFileMapper.getFileById(id);
    }
}
