package com.dddqmmx.surf.server.socket.tcp;

import com.dddqmmx.surf.server.mapper.GroupMemberMapper;
import com.dddqmmx.surf.server.pojo.*;
import com.dddqmmx.surf.server.service.*;
import com.dddqmmx.surf.server.socket.connect.ConnectList;
import com.dddqmmx.surf.server.socket.connect.SocketSession;
import com.dddqmmx.surf.server.util.BeanUtil;
import com.dddqmmx.surf.server.util.NumberUtil;
import com.dddqmmx.surf.server.util.RandomCharacters;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class TCPServerThread extends Thread{

    private UserService userService;
    private GroupService groupService;
    private MessageService messageService;
    private GroupMemberService groupMemberService;
    private RelationService relationService;

    protected String sessionId;
    private final Socket socket;

    public TCPServerThread(Socket socket) {
        this.socket = socket;
        userService = BeanUtil.getBean(UserServiceImpl.class);
        groupService = BeanUtil.getBean(GroupServiceImpl.class);
        messageService = BeanUtil.getBean(MessageServiceImpl.class);
        messageService = BeanUtil.getBean(MessageServiceImpl.class);
        groupMemberService = BeanUtil.getBean(GroupMemberService.class);
        relationService = BeanUtil.getBean(RelationService.class);
    }

    @Override
    public void run() {
        try {
            //从socket通信管道中得到一个字节输入流
            InputStream is = socket.getInputStream();
            //把字节输入流转换成字符输入流
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            //把字符输入流包装为缓冲字符输入流
            BufferedReader br = new BufferedReader(isr);
            //按照行读取消息
            String line;
            while ((line = br.readLine())!= null){
                //消息处理
                System.out.println("TCP : "+line);

                JSONObject jsonObject = new JSONObject(line);
                //获取客户端提交json的命令
                String command = jsonObject.getString("command");

                if ("connect".equals(command)){
                    //获得ip地址
                    String system = jsonObject.getString("system");
                    if (system.equals("android")){
                        /*随机生成32位数的sessionId*/
                        sessionId = RandomCharacters.random(32);
                        while (ConnectList.hasSessionId(sessionId)) {
                            sessionId = RandomCharacters.random(32);
                        }
                        SocketSession socketSession = new SocketSession();
                        ConnectList.setSocketSession(sessionId,socketSession);
                        ConnectList.setThread(sessionId,this);
                        JSONObject comeBackJson = new JSONObject();
                        comeBackJson.put("command",command);
                        comeBackJson.put("connect","true");
                        comeBackJson.put("sessionId",sessionId);
                        System.out.println(sessionId);
                        send(comeBackJson);
                    }
                } else if ("login".equals(command)){
                    String userName = jsonObject.getString("userName");
                    String password = jsonObject.getString("password");
                    SocketSession socketSession = ConnectList.getSocketSession(sessionId);
                    User user = userService.login(userName,password);
                    JSONObject comeBackJson = new JSONObject();
                    if (user != null){
                        ConnectList.userSessionMap.put(user.getId(),sessionId);
                        socketSession.set("user",user);
                        comeBackJson.put("login","true");
                        comeBackJson.put("id",user.getId());
                        comeBackJson.put("message","登录成功");
                    }else {
                        boolean hasUserName = userService.hasUserName(userName);
                        System.out.println(hasUserName);
                        if (hasUserName) {
                            System.out.println("false");
                            comeBackJson.put("login","false");
                            comeBackJson.put("message","密码错误");
                        }else {
                            userService.register(userName,password);
                            user = userService.login(userName,password);
                            socketSession.set("user",user);
                            comeBackJson.put("login","true");
                            comeBackJson.put("id",user.getId());
                            comeBackJson.put("message","注册成功");
                        }
                    }
                    comeBackJson.put("command",command);
                    send(comeBackJson);
                } else if ("getUserInfo".equals(command)){
                    JSONObject comeBackJson = new JSONObject();
                    User user = null;
                    if (!jsonObject.has("userId")){
                        SocketSession socketSession = ConnectList.getSocketSession(sessionId);
                        user = (User) socketSession.get("user");
                    } else {
                        System.out.println("error");
                    }
                    comeBackJson.put("command",command);
                    if (user != null) {
                        comeBackJson.put("userName",user.getUserName());
                        comeBackJson.put("name",user.getName());
                    }
                    send(comeBackJson);
                } else if ("getGroupList".equals(command)) {
                    JSONObject comeBackJson = new JSONObject();
                    SocketSession socketSession = ConnectList.getSocketSession(sessionId);
                    User user = (User) socketSession.get("user");
                    List<Group> groupList = groupService.geGroupListByUserId(user.getId());
                    JSONArray groupListArray = new JSONArray();
                    for(Group group : groupList){
                        JSONObject json = new JSONObject();
                        json.put("id",group.getId());
                        json.put("groupName",group.getGroupName());
                        json.put("groupHead",group.getGroupHead());
                        groupListArray.put(json);
                    }
                    comeBackJson.put("groupList",groupListArray);
                    comeBackJson.put("command",command);
                    send(comeBackJson);
                } else if ("getUserFriendList".equals(command)){
                    JSONObject comeBackJson = new JSONObject();
                    SocketSession socketSession = ConnectList.getSocketSession(sessionId);
                    User user = (User) socketSession.get("user");
                    List<User> userFriendList = userService.getUserFriendList(user.getId());
                    JSONArray groupListArray = new JSONArray();
                    for(User userFriend : userFriendList){
                        groupListArray.put(userFriend.getId());
                    }
                    comeBackJson.put("userList",groupListArray);
                    comeBackJson.put("command",command);
                    send(comeBackJson);
                } else if ("getGroupInfo".equals(command)) {
                    int groupId = jsonObject.getInt("groupId");
                    Group groupInfo = groupService.getGroupInfo(groupId);
                    JSONObject comeBackJson = new JSONObject();
                    comeBackJson.put("groupName",groupInfo.getGroupName());
                    comeBackJson.put("command",command);
                    send(comeBackJson);
                } else if ("getGroupMessage".equals(command)){
                    int groupId = jsonObject.getInt("groupId");
                    List<Message> groupMessage = messageService.getGroupMessage(groupId);
                    if (groupMessage.size() != 0){
                        JSONObject comeBackJson = new JSONObject();
                        JSONArray messageArray = new JSONArray();
                        for(Message message : groupMessage){
                            JSONObject json = new JSONObject();
                            json.put("id",message.getId());
                            json.put("senderId",message.getSenderId());
                            json.put("contactType",message.getContactType());
                            json.put("contactId",message.getContactId());
                            json.put("messageType",message.getMessageType());
                            json.put("message",message.getMessage());
                            messageArray.put(json);
                        }
                        comeBackJson.put("command",command);
                        comeBackJson.put("messageList",messageArray);
                        send(comeBackJson);
                    }
                } else if ("getUserInfoById".equals(command)){
                    int userId = jsonObject.getInt("userId");
                    User userById = userService.getUserById(userId);
                    JSONObject comeBackJson = new JSONObject();
                    comeBackJson.put("command",command);
                    comeBackJson.put("id",userById.getId());
                    comeBackJson.put("userName",userById.getUserName());
                    comeBackJson.put("name",userById.getName());
                    comeBackJson.put("personalProfile",userById.getPersonalProfile());
                    send(comeBackJson);
                } else if ("sendTextMessage".equals(command)){
                    SocketSession socketSession = ConnectList.getSocketSession(sessionId);
                    User user = (User) socketSession.get("user");
                    int contactType = jsonObject.getInt("contactType");
                    int contactId = jsonObject.getInt("contactId");
                    String message = jsonObject.getString("message");
                    JSONObject comeBackJson = new JSONObject();
                    comeBackJson.put("command","processMessage");
                    comeBackJson.put("sender",user.getId());
                    comeBackJson.put("message",message);
                    comeBackJson.put("contactType",contactType);
                    comeBackJson.put("contactId",contactId);
                    if (contactType == 1) {
                        Message saveMessage = new Message();
                        saveMessage.setSenderId(user.getId());
                        saveMessage.setContactType(contactType);
                        saveMessage.setContactId(contactId);
                        saveMessage.setMessageType(1);
                        saveMessage.setMessage(message);
                        messageService.insertMessage(saveMessage);
                        for (GroupMember groupMember : groupMemberService.getGroupMemberListByGroupId(contactId)) {
                            int userId = groupMember.getUserId();
                            if (ConnectList.userSessionMap.containsKey(userId) && userId != user.getId()){
                                String sessionId = ConnectList.userSessionMap.get(userId);
                                TCPServerThread thread = ConnectList.getThread(sessionId);
                                thread.send(comeBackJson);
                            }
                        }
                    }
                } else if ("addFriendRequest".equals(command)){
                    int userId = jsonObject.getInt("userId");
                    SocketSession socketSession = ConnectList.getSocketSession(sessionId);
                    User user = (User) socketSession.get("user");
                    int code = relationService.addFriendRequest(userId, user.getId());
                    JSONObject comeBackJson = new JSONObject();
                    comeBackJson.put("command","addFriendRequest");
                    comeBackJson.put("code",code);
                    send(comeBackJson);
                } else if ("getFriendRequest".equals(command)){
                    SocketSession socketSession = ConnectList.getSocketSession(sessionId);
                    User user = (User) socketSession.get("user");
                    List<Relation> relationList = relationService.getFriendRequestByUserId(user.getId());
                    JSONObject comeBackJson = new JSONObject();
                    JSONArray relationArray = new JSONArray();
                    for(Relation relation : relationList){;
                        relationArray.put(relation.getOtherSideId());
                    }
                    comeBackJson.put("relationArray",relationArray);
                    comeBackJson.put("command",command);
                    send(comeBackJson);
                } else if ("agreeFriendRequest".equals(command)){
                    int userId = jsonObject.getInt("userId");
                    SocketSession socketSession = ConnectList.getSocketSession(sessionId);
                    User user = (User) socketSession.get("user");
                    int code = relationService.agreeFriendRequest(user.getId(), userId);
                    JSONObject comeBackJson = new JSONObject();
                    comeBackJson.put("command",command);
                    comeBackJson.put("id",userId);
                    comeBackJson.put("code",code);
                    send(comeBackJson);
                } else if ("selectGroup".equals(command)){
                    String condition = jsonObject.getString("condition");
                    boolean isNumeric = NumberUtil.isNumeric(condition);
                    JSONArray jsonArray = new JSONArray();
                    if (isNumeric){
                        Group group = groupService.getGroupInfo(Integer.parseInt(condition));
                        if (group != null){
                            jsonArray.put(group.getId());
                        }
                    }else {
                    }
                    JSONObject comeBackJson = new JSONObject();
                    comeBackJson.put("command",command);
                    comeBackJson.put("groupList",jsonArray);
                    send(comeBackJson);
                }else if ("selectUser".equals(command)){
                    String condition = jsonObject.getString("condition");
                    boolean isNumeric = NumberUtil.isNumeric(condition);
                    JSONArray jsonArray = new JSONArray();
                    if (isNumeric){
                        User user = userService.getUserById(Integer.parseInt(condition));
                        if (user != null){
                            jsonArray.put(user.getId());
                        }
                    }else {

                    }
                    JSONObject comeBackJson = new JSONObject();
                    comeBackJson.put("command",command);
                    comeBackJson.put("userList",jsonArray);
                    send(comeBackJson);
                }else if ("getGroupInfoById".equals(command)) {
                    int groupId = jsonObject.getInt("groupId");
                    Group groupById = groupService.getGroupInfo(groupId);
                    JSONObject comeBackJson = new JSONObject();
                    comeBackJson.put("command", command);
                    comeBackJson.put("id", groupById.getId());
                    comeBackJson.put("groupName", groupById.getGroupName());
                    comeBackJson.put("groupHead", groupById.getGroupHead());
                    send(comeBackJson);
                } else if ("addGroupRequest".equals(command)){
                    int groupId = jsonObject.getInt("groupId");
                    SocketSession socketSession = ConnectList.getSocketSession(sessionId);
                    User user = (User) socketSession.get("user");
                    int code = groupMemberService.addGroupRequest(user.getId(),groupId);
                    JSONObject comeBackJson = new JSONObject();
                    comeBackJson.put("command","addGroupRequest");
                    comeBackJson.put("id",groupId);
                    comeBackJson.put("code",code);
                    send(comeBackJson);
                } else if ("getGroupRequest".equals(command)){
                    SocketSession socketSession = ConnectList.getSocketSession(sessionId);
                    User user = (User) socketSession.get("user");
                    List<GroupMember> groupRequestList = groupMemberService.getAddGroupRequestListByUserId(user.getId());
                    JSONObject comeBackJson = new JSONObject();
                    JSONArray relationArray = new JSONArray();
                    for (GroupMember groupMember : groupRequestList){
                        JSONObject groupRequest = new JSONObject();
                        groupRequest.put("groupId",groupMember.getGroupId());
                        groupRequest.put("userId",groupMember.getUserId());
                        relationArray.put(groupRequest);
                    }
                    comeBackJson.put("relationArray",relationArray);
                    comeBackJson.put("command",command);
                    send(comeBackJson);
                }
            }
            socket.close();
            System.out.println("over");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("客户端"+socket.getRemoteSocketAddress()+"下线了。");
        }
        SocketSession socketSession = ConnectList.getSocketSession(sessionId);
        User user = (User) socketSession.get("user");
        if (user != null) {
            ConnectList.userSessionMap.remove(user.getId());
        }
    }

    public void send(String json){
        System.out.println("TCPSend : "+ json);
        try {
            OutputStreamWriter outputStream = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
            PrintWriter printWriter = new PrintWriter(outputStream,true);
            printWriter.println(json);
            /*printWriter.flush();*/
            /*PrintStream ps = new PrintStream(outputStream,true);
            ps.println(json);
            ps.flush();*/
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void send(Object object){
        send(object.toString());
    }
}
