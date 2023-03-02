package com.dddqmmx.surf.server.mapper;

import com.dddqmmx.surf.server.pojo.SurfFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SurfFileMapper {
    SurfFile getFileByMD5(String md5);
    SurfFile getFileById(int id);
}
