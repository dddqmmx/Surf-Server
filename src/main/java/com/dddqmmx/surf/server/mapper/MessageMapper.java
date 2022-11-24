package com.dddqmmx.surf.server.mapper;

import com.dddqmmx.surf.server.pojo.Message;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MessageMapper {
    List<Message> getGroupMessage(int groupId);
    int insertMessage(Message message);
}
