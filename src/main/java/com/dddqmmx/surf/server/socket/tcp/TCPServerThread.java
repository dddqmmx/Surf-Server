package com.dddqmmx.surf.server.socket.tcp;

import com.dddqmmx.surf.server.pojo.*;
import com.dddqmmx.surf.server.service.*;
import com.dddqmmx.surf.server.socket.connect.ConnectList;
import com.dddqmmx.surf.server.socket.connect.SocketSession;
import com.dddqmmx.surf.server.util.MessageUtil;
import com.dddqmmx.surf.server.util.BeanUtil;
import com.dddqmmx.surf.server.util.NumberUtil;
import com.dddqmmx.surf.server.util.RandomCharacters;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class TCPServerThread extends Thread{

    //被占用的的messageId
    public byte[] occupiedMessageId = new byte[256];
    /*字节的最大上限如果发送消息的时候messageId等于maxMessageId则把message设置为minMessageId
    因为以后可能会改成int,long啥的就把max和min写上了
     */
    public byte maxMessageId = 127;
    //字节的最小上限;
    public byte minMessageId = -128;
    //存储下个message的
    public byte messageId = -128;

    private BufferedOutputStream bufferedOutputStream = null;
    private BufferedInputStream bufferedInputStream = null;

    private UserService userService;
    private GroupService groupService;
    private MessageService messageService;
    private GroupMemberService groupMemberService;
    private RelationService relationService;

    public static Map<Byte,ByteArrayOutputStream> byteArrayOutputStreamMap = new HashMap<>();

    protected String sessionId;
    private final Socket socket;

    public TCPServerThread(Socket socket,String sessionId) {
        this.socket = socket;
        this.sessionId = sessionId;
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
            bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
            bufferedInputStream = new BufferedInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int length;
            byte[] messageBytes = new byte[0];
            while ((length = inputStream.read(buffer)) != -1) {
                // 将读取到的字节流拼接到之前的消息字节数组中
                messageBytes = Arrays.copyOf(messageBytes, messageBytes.length + length);
                System.arraycopy(buffer, 0, messageBytes, messageBytes.length - length, length);

                // 如果读取到的字节流中包含完整的消息，则将其分离出来并处理
                while (messageBytes.length >= 4) {
                    byte[] messageHeader = Arrays.copyOfRange(messageBytes, 0, 4); // 取前 4 个字节作为消息头
                    ByteBuffer buffer1 = ByteBuffer.wrap(messageHeader); // 使用 ByteBuffer 对消息头进行解析
                    int messageLength = buffer1.getInt(); // 解析消息头中的消息长度
                    //byte[] messageData = Arrays.copyOfRange(messageBytes, 4, 4 + messageLength);
                    // 如果读取到的字节流中包含完整的消息，则将其分离出来并处理
                    if (messageBytes.length >= 4 + messageLength)
                    {
                        byte[] message = Arrays.copyOfRange(messageBytes, 4, 4 + messageLength);
                        processMessage(message);
                        messageBytes = Arrays.copyOfRange(messageBytes, 4 + messageLength, messageBytes.length);
                    } else {
                        break;
                    }
                }
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
        //send("烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希烂裤裆後藤希123");
        int o = 0;
        while(true) {
            byte[] by = new byte[1024+3];        //保存包里的字节
            int res = 0;                         //字节长度
            byte messageId;                      //消息id
            ByteArrayOutputStream byteArrayOutputStream;    //把分段发送的字节存到这里,用来读取
            String command;                                 //消息发完消息字符串放在这里
            byte transferCompleteFlag;                      //消息是否发完的标记
            try {
                res = bufferedInputStream.read(by);         //获得一个消息一共有多少字节
                messageId = by[0];                          //获得消息id
                transferCompleteFlag = by[2];               //获得消息是否发送完的标记
                if (by[1] == 1) {
                    // 利用String构造方法的形式，将字节数组转化成字符串打印出来
                    if (byteArrayOutputStreamMap.containsKey(messageId)) {
                        byteArrayOutputStream = byteArrayOutputStreamMap.get(messageId);
                        byteArrayOutputStream.write(by, 3, res - 3);
                        byteArrayOutputStream.flush();
                        byteArrayOutputStream.close();
                    } else {
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        byteArrayOutputStream.write(by, 3, res - 3);
                        byteArrayOutputStream.flush();
                        byteArrayOutputStream.close();
                        byteArrayOutputStreamMap.put(messageId, byteArrayOutputStream);
                    }
                    if (transferCompleteFlag == 1) {
                        System.out.println("Server get : "+messageId+" : " + byteArrayOutputStreamMap.get(messageId).toString(StandardCharsets.UTF_8));

                    }

                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*try {
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


            }
            socket.close();
            System.out.println("over");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("客户端"+socket.getRemoteSocketAddress()+"下线了。");
        }*/

    }


    private void processMessage(byte[] bytes) throws IOException, JSONException {
        byte messageId = bytes[0];
        byte transferCompleteFlag = bytes[2];
        if (bytes[1] == 1) {
            if (byteArrayOutputStreamMap.containsKey(messageId)) {
                ByteArrayOutputStream byteArrayOutputStream = byteArrayOutputStreamMap.get(messageId);
                byteArrayOutputStream.write(bytes, 3, bytes.length - 3);
                byteArrayOutputStream.flush();
                byteArrayOutputStream.close();
            } else {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byteArrayOutputStream.write(bytes, 3, bytes.length - 3);
                byteArrayOutputStream.flush();
                byteArrayOutputStream.close();
                byteArrayOutputStreamMap.put(messageId, byteArrayOutputStream);
            }
            if (transferCompleteFlag == 1) {
                System.out.println(Arrays.toString(byteArrayOutputStreamMap.get(messageId).toByteArray()));
                String msg = byteArrayOutputStreamMap.get(messageId).toString("UTF-8");
                System.out.println(msg);
                JSONObject jsonObject = new JSONObject(byteArrayOutputStreamMap.get(messageId).toString(StandardCharsets.UTF_8));
                //获取客户端提交json的命令
                String command = jsonObject.getString("command");
                if ("connect".equals(command)){
                    //获得ip地址
                    String system = jsonObject.getString("system");
                    if (system.equals("android")){
                        /* *//*随机生成32位数的sessionId*//*
                                sessionId = RandomCharacters.random(32);
                                while (ConnectList.sessionList.containsKey(sessionId)) {
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
                                send(comeBackJson);*/
                    }
                } else if ("login".equals(command)){
                    String userName = jsonObject.getString("userName");
                    String password = jsonObject.getString("password");
                    SocketSession socketSession = ConnectList.sessionList.get(sessionId);

                    User user = userService.login(userName,password);
                    JSONObject comeBackJson = new JSONObject();
                    if (user != null){
                        ConnectList.userSession.put(user.getId(),sessionId);
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
                        SocketSession socketSession = ConnectList.sessionList.get(sessionId);
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
                    SocketSession socketSession = ConnectList.sessionList.get(sessionId);
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
                    SocketSession socketSession = ConnectList.sessionList.get(sessionId);
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
                    SocketSession socketSession = ConnectList.sessionList.get(sessionId);
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
                            if (ConnectList.userSession.containsKey(userId) && userId != user.getId()){
                                String sessionId = ConnectList.userSession.get(userId);
                                TCPServerThread thread = ConnectList.connectList.get(sessionId).getTcpServerThread();
                                thread.send(comeBackJson);
                            }
                        }
                    }
                } else if ("addFriendRequest".equals(command)){
                    int userId = jsonObject.getInt("userId");
                    SocketSession socketSession = ConnectList.sessionList.get(sessionId);
                    User user = (User) socketSession.get("user");
                    int code = relationService.addFriendRequest(userId, user.getId());
                    JSONObject comeBackJson = new JSONObject();
                    comeBackJson.put("command","addFriendRequest");
                    comeBackJson.put("code",code);
                    send(comeBackJson);
                } else if ("getFriendRequest".equals(command)){
                    SocketSession socketSession = ConnectList.sessionList.get(sessionId);
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
                    SocketSession socketSession = ConnectList.sessionList.get(sessionId);
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
                    SocketSession socketSession = ConnectList.sessionList.get(sessionId);
                    User user = (User) socketSession.get("user");
                    int code = groupMemberService.addGroupRequest(user.getId(),groupId);
                    JSONObject comeBackJson = new JSONObject();
                    comeBackJson.put("command","addGroupRequest");
                    comeBackJson.put("id",groupId);
                    comeBackJson.put("code",code);
                    send(comeBackJson);
                } else if ("getGroupRequest".equals(command)){
                    SocketSession socketSession = ConnectList.sessionList.get(sessionId);
                    User user = (User) socketSession.get("user");
                    List<GroupMember> groupRequestList = groupMemberService.getAddGroupRequestListByUserId(user.getId());
                    JSONObject comeBackJson = new JSONObject();
                    JSONArray relationArray = new JSONArray();
                    for (GroupMember groupMember : groupRequestList){
                        JSONObject groupRequest = new JSONObject();
                        groupRequest.put("id",groupMember.getId());
                        groupRequest.put("groupId",groupMember.getGroupId());
                        groupRequest.put("userId",groupMember.getUserId());
                        relationArray.put(groupRequest);
                    }
                    comeBackJson.put("relationArray",relationArray);
                    comeBackJson.put("command",command);
                    send(comeBackJson);
                } else if ("agreeGroupRequest".equals(command)){
                    int groupMemberId = jsonObject.getInt("groupMemberId");
                    boolean bool = groupMemberService.agreeGroupRequest(groupMemberId);
                    JSONObject comeBackJson = new JSONObject();
                    comeBackJson.put("command",command);
                    comeBackJson.put("groupMemberId",groupMemberId);
                    comeBackJson.put("bool",bool);
                    send(comeBackJson);
                } else if ("getGroupHead".equals(command)) {
                    int groupId = jsonObject.getInt("groupId");
                    String encode = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get("surf/image/group/"+groupId+".png")));
                    JSONObject comeBackJson = new JSONObject();
                    comeBackJson.put("command",command);
                    comeBackJson.put("encode",encode);
                    comeBackJson.put("groupId",groupId);
                    send(comeBackJson);
                }
            }
        }
    }



    public void send(JSONObject jsonObject){
        send(jsonObject.toString());
    }

    public void send(String message){
        byte messageId = getMessageId();
        System.out.println("Server send : "+messageId+" : "+message);
        byte[] sb = message.getBytes(); // 转化为字节数组
        ArrayList<byte[]> newByteArr = MessageUtil.reviseArr(sb, messageId);
        for (byte[] bytes : newByteArr) {
            try {
                BufferedOutputStream ps = new BufferedOutputStream(socket.getOutputStream());
                System.out.println(Arrays.toString(bytes));
                //
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                dataOutputStream.writeInt(bytes.length);
                System.out.println(bytes.length);
                dataOutputStream.write(bytes);
                dataOutputStream.flush();
                byte[] messageBytes = byteArrayOutputStream.toByteArray();
                //
                ps.write(messageBytes);   // 写入输出流，将内容发送给客户端的输入流
                ps.flush();
                //ps.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }*/
        }
    }

    public byte getMessageId(){
        synchronized (this) {
            if (messageId == maxMessageId){
                messageId =  minMessageId;
            }
            return messageId++;
        }
    }
}
