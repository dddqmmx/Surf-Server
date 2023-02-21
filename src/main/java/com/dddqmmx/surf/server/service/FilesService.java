package com.dddqmmx.surf.server.service;

import com.dddqmmx.surf.server.pojo.Files;

public interface FilesService {
    Files getFileByMD5(String md5);
}
