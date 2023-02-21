package com.dddqmmx.surf.server.mapper;

import com.dddqmmx.surf.server.pojo.Files;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface FilesMapper {
    Files getFileByMD5(String md5);
}
