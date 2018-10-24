package com.hjzddata.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
//访问服务端的url地址
@ServerEndpoint(value = "/websocket/{id}")
public class WebSocketServer {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private static int onlineCount = 0;
    private static ConcurrentHashMap<String, WebSocketServer> webSocketSet = new ConcurrentHashMap<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private String id = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam(value = "id") String id, Session session) {
        this.session = session;
        if (webSocketSet.get(id) == null || webSocketSet.get(id).session.isOpen() != true) {
            this.id = id;//接收到发送消息的人员编号
            webSocketSet.put(id, this);     //加入set中
            addOnlineCount();           //在线数加1
            log.info("用户[" + id + "] sessionId:[" + session.getId() + "}加入！当前在线人数为" + webSocketSet.mappingCount());
            printSocks();
            try {
                sendMessage("连接成功");
            } catch (IOException e) {
                log.info("websocket IO异常:" + e.getMessage());
            }
        } else { // 如果坐席登陆状态下，再次登陆，拒绝该次连接请求，返回
            try {
                sendMessage("您已登陆，无须再次连接");
                printSocks();
            } catch (IOException io) {
                if (log.isInfoEnabled()) {
                    log.info("服务端发送消息异常:" + io.getMessage());
                }
            }

        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (this.id != null && webSocketSet.get(this.id) != null) {
            webSocketSet.remove(this.id);   //从set中删除
            subOnlineCount();               //在线数减1
            log.info("用户[" + id + "] sessionId:[" + session.getId() + "]主动关闭连接！当前在线人数为" + webSocketSet.mappingCount());
            printSocks();
        } else {
            // 未建立连接的客户端，后端不做任何处理
            log.info("未建立连接的客户端，后端不做任何处理");
        }
    }

    private void printSocks() {
        Set st = webSocketSet.keySet();
        Iterator ik = st.iterator();
        log.info("---------目前在线情况如下：-------------");
        while (ik.hasNext()) {
            String key = ik.next().toString();
            log.info("-------->key[" + key + "],session:" + webSocketSet.get(key).toString());
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        this.session = session;

        if (log.isInfoEnabled()) {
            log.info("来自客户端用户[" + this.id + "] sessionId[" + this.session.getId() + "]的消息:" + message);
        }
        printSocks();

        //可以自己约定字符串内容，比如 内容|0 表示信息群发，内容|X 表示信息发给id为X的用户
        String sendMessage = message.split("[|]")[0];
        String sendUserId = message.split("[|]")[1];

        try {
            if (sendUserId.equals("0"))
                sendtoAll(sendMessage);
            else {
                if (sendMessage.equals("ping")) {
                    sendtoUser("pong", sendUserId);
                } else {
                    sendtoUser(sendMessage, sendUserId);
                }
            }
        } catch (IOException e) {
            if (log.isInfoEnabled()) {
                log.info("服务端-》" + sendUserId + "客户端发送失败:" + e.getMessage());
            }
            e.printStackTrace();
        }

    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        this.session = session;
        if (log.isInfoEnabled()) {
            log.info("发生错误,用户[" + this.id + "],sessionId[" + this.session.getId() + "]," +
                    "异常信息:" + error.getMessage());
        }
        printSocks();
        error.printStackTrace();
    }


    public void sendMessage(String message) throws IOException {
        synchronized (session) {
            if (session.isOpen()) {
                session.getBasicRemote().sendText(message);
            }
        }
//        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 发送信息给指定ID用户，如果用户不在线则返回不在线信息给自己
     *
     * @param message
     * @param sendUserId
     * @throws IOException
     */
    public void sendtoUser(String message, String sendUserId) throws IOException {
        if (webSocketSet.get(sendUserId) != null && webSocketSet.get(sendUserId).session.isOpen() == true) {
            if (!id.equals(sendUserId)) {
                try {
                    webSocketSet.get(sendUserId).sendMessage(message);
                } catch (IOException e) {
                    if (log.isInfoEnabled()) {
                        log.info("服务端-》" + sendUserId + "客户端消息发送失败:" + e.getMessage());
                    }
                    e.printStackTrace();
                }
            } else {
                try {
                    webSocketSet.get(sendUserId).sendMessage(message);
                } catch (IOException e) {
                    log.info("服务端-》" + sendUserId + "客户端消息发送失败:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            //如果用户不在线则返回不在线信息给自己
            try {
                sendtoUser("当前用户不在线", id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送信息给指定ID用户，如果用户不在线则返回不在线信息给自己
     *
     * @param message
     * @param sendUserId
     * @throws IOException
     */
    public void sendtoCcUser(String message, String sendUserId, String custom_id, String name, String sessionID) throws IOException {
        if (webSocketSet.get(sendUserId) != null && webSocketSet.get(sendUserId).session.isOpen() == true) {
            if (!id.equals(sendUserId)) {
                try {
                    webSocketSet.get(sendUserId).sendMessage(message + "|" + custom_id + "|" + name + "|" + sessionID);
                } catch (IOException e) {
                    log.info("服务端-》" + sendUserId + "客户端消息发送失败:" + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                try {
                    webSocketSet.get(sendUserId).sendMessage(message + "|" + custom_id + "|" + name + "|" + sessionID);
                } catch (IOException e) {
                    log.info("服务端-》" + sendUserId + "客户端消息发送失败:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 发送信息给所有人
     *
     * @param message
     * @throws IOException
     */
    public void sendtoAll(String message) throws IOException {
        for (String key : webSocketSet.keySet()) {
            try {
                webSocketSet.get(key).sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        if (WebSocketServer.onlineCount > 0) {
            WebSocketServer.onlineCount--;
        }
    }
}

