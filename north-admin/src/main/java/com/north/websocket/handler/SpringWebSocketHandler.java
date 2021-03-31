package com.north.websocket.handler;

import com.north.websocket.entity.WebSocketUserInfo;
import com.north.websocket.protobuf.NotificationMessageOuterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-15
 */
public class SpringWebSocketHandler extends TextWebSocketHandler {

    private static Logger logger = LoggerFactory.getLogger(SpringWebSocketHandler.class);

    //Map来存储WebSocketSession，key用USER_ID 即在线用户列表
    private static final Map<String, WebSocketSession> users = new HashMap<String, WebSocketSession>();

    //用户标识
    //对应监听器从的key
    private static final String USER_ID = "WEBSOCKET_USERID";

    public SpringWebSocketHandler() {
    }

    /**
     * 开始连接时触发
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (session.getAttributes().get("userInfo") == null) {
            return;
        }
        WebSocketUserInfo userInfo = (WebSocketUserInfo) session.getAttributes().get("userInfo");
        users.put(userInfo.getUserId(), session);
        logger.debug("{},成功建立websocket连接!", userInfo.getNickname());
        logger.debug("当前线上用户数量:{}", users.size());

        //这块会实现自己业务，比如，当用户登录后，会把离线消息推送给用户
        //TextMessage returnMessage = new TextMessage("成功建立socket连接，你将收到的离线");
        //session.sendMessage(returnMessage);
    }

    /**
     * 关闭连接时触发
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        if (session.getAttributes().get("userInfo") == null) {
            return;
        }
        WebSocketUserInfo userInfo = (WebSocketUserInfo) session.getAttributes().get("userInfo");
        users.remove(userInfo.getUserId());
        logger.debug("{},用户已退出！", userInfo.getNickname());
        logger.debug("剩余在线用户:{}", users.size());
    }

    /**
     * 文本处理
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        WebSocketUserInfo userInfo = (WebSocketUserInfo) session.getAttributes().get("userInfo");
        logger.debug("服务器收到消息：{}", message);

        if (message.getPayload().startsWith("#anyone#")) { //单发某人

            sendMessageToUser((String) session.getAttributes().get(USER_ID), new TextMessage("服务器单发：" + message.getPayload()));

        } else if (message.getPayload().startsWith("#everyone#")) {

            sendMessageToUsers(new TextMessage("服务器群发：" + message.getPayload()));

        } else {
            NotificationMessageOuterClass.NotificationMessage.Builder builder = NotificationMessageOuterClass.NotificationMessage.newBuilder();
            builder.setTitle("websocket推送标题");
            builder.setMessage("这是一个websocket通知的消息内容");

            NotificationMessageOuterClass.NotificationMessage notificationMessage = builder.build();
            System.out.println(notificationMessage.toString());
            System.out.println(notificationMessage.toByteArray());
            sendMessageToUser(userInfo.getUserId(), new TextMessage(notificationMessage.toString()));
            sendMessageToUser(userInfo.getUserId(), new BinaryMessage(notificationMessage.toByteArray()));
        }

    }

    /**
     * 二进制处理
     *
     * @param session
     * @param message
     */
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        logger.debug("服务器收到二进制消息：{}", message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        logger.debug("传输出现异常，关闭websocket连接... ");
        String userId = (String) session.getAttributes().get(USER_ID);
        users.remove(userId);
    }

    @Override
    public boolean supportsPartialMessages() {

        return false;
    }


    /**
     * 给某个用户发送消息
     *
     * @param userId
     * @param message
     * @return
     */
    public static Boolean sendMessageToUser(String userId, AbstractWebSocketMessage message) {
        for (String id : users.keySet()) {
            if (id.equals(userId)) {
                try {
                    if (users.get(id).isOpen()) {
                        users.get(id).sendMessage(message);
                        return true;

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return false;
    }

    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public static void sendMessageToUsers(AbstractWebSocketMessage message) {
        for (String userId : users.keySet()) {
            try {
                if (users.get(userId).isOpen()) {
                    users.get(userId).sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
