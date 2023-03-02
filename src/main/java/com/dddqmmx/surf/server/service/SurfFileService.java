package com.dddqmmx.surf.server.service;

import com.dddqmmx.surf.server.pojo.SurfFile;

public interface SurfFileService {
    SurfFile getFileByMD5(String md5);
    SurfFile getFileById(int id);
}
