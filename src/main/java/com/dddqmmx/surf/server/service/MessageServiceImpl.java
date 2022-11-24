package com.dddqmmx.surf.server.service;

import com.dddqmmx.surf.server.mapper.MessageMapper;
import com.dddqmmx.surf.server.mapper.UserMapper;
import com.dddqmmx.surf.server.pojo.Message;
import com.dddqmmx.surf.server.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("messageService")
public class MessageServiceImpl implements MessageService{

    final MessageMapper messageMapper;

    public MessageServiceImpl(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Override
    public List<Message> getGroupMessage(int groupId) {
        return messageMapper.getGroupMessage(groupId);
    }

    @Override
    public boolean insertMessage(Message message) {
        return messageMapper.insertMessage(message)>0;
    }
}
