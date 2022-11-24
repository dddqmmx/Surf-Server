package com.dddqmmx.surf.server.service;

import com.dddqmmx.surf.server.pojo.Message;

import java.util.List;

public interface MessageService {
    List<Message> getGroupMessage(int groupId);
    boolean insertMessage(Message message);
}
