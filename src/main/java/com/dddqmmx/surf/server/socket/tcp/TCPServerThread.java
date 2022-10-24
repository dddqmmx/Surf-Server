package com.dddqmmx.surf.server.socket.tcp;

import com.dddqmmx.surf.server.pojo.Group;
import com.dddqmmx.surf.server.pojo.User;
import com.dddqmmx.surf.server.service.GroupService;
import com.dddqmmx.surf.server.service.UserService;
import com.dddqmmx.surf.server.socket.connect.ConnectList;
import com.dddqmmx.surf.server.socket.connect.SocketSession;
import com.dddqmmx.surf.server.util.BeanUtil;
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

    protected String sessionId;
    private final Socket socket;

    public TCPServerThread(Socket socket) {
        this.socket = socket;
        userService = BeanUtil.getBean(UserService.class);
        groupService = BeanUtil.getBean(GroupService.class);
    }

    @Override
    public void run() {
        try {
            //从socket通信管道中得到一个字节输入流
            InputStream is = socket.getInputStream();
            //把字节输入流转换成字符输入流
            InputStreamReader isr = new InputStreamReader(is);
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
                    User user = userService.getUserById(userName,password);
                    JSONObject comeBackJson = new JSONObject();
                    if (user != null){
                        socketSession.set("user",user);
                        comeBackJson.put("login","true");
                    }else {
                        comeBackJson.put("login","false");
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
                        JSONObject json = new JSONObject();
                        json.put("id",userFriend.getId());
                        json.put("userName",userFriend.getUserName());
                        json.put("name",userFriend.getName());
                        groupListArray.put(json);
                    }
                    comeBackJson.put("userList",groupListArray);
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
    }

    public void send(String json){
        System.out.println("TCPSend : " + json);
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
