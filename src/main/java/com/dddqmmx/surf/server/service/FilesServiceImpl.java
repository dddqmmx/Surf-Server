package com.dddqmmx.surf.server.service;

import com.dddqmmx.surf.server.mapper.FilesMapper;
import com.dddqmmx.surf.server.pojo.Files;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("fileService")
public class FilesServiceImpl implements FilesService {

    @Autowired
    FilesMapper fileMapper;

    @Override
    public Files getFileByMD5(String md5) {
        return fileMapper.getFileByMD5(md5);
    }
}
