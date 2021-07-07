package com.north.util;

import com.north.websocket.handler.SpringWebSocketHandler;
import com.north.websocket.protobuf.NotificationMessageOuterClass;
import org.springframework.web.socket.AbstractWebSocketMessage;
import org.springframework.web.socket.BinaryMessage;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-16
 */
public class WebSocketUtil {

    /**
     * 发送ws通知单个用户
     *
     * @param userId
     * @param title
     * @param message
     */
    public static Boolean notifyUser(String userId, String title, String message) {
        AbstractWebSocketMessage webSocketMessage = buildNotificationMessage(title, message);
        return SpringWebSocketHandler.sendMessageToUser(userId, webSocketMessage);
    }

    /**
     * 发送ws通知所有用户
     *
     * @param title
     * @param message
     */
    public static void notifyAllUser(String title, String message) {
        AbstractWebSocketMessage webSocketMessage = buildNotificationMessage(title, message);
        SpringWebSocketHandler.sendMessageToUsers(webSocketMessage);
    }

    private static AbstractWebSocketMessage buildNotificationMessage(String title, String message) {
        NotificationMessageOuterClass.NotificationMessage.Builder builder = NotificationMessageOuterClass.NotificationMessage.newBuilder();
        builder.setTitle(title);
        builder.setMessage(message);
        NotificationMessageOuterClass.NotificationMessage notificationMessage = builder.build();
        return new BinaryMessage(notificationMessage.toByteArray());
    }

    public static void test() {
        AbstractWebSocketMessage webSocketMessage = new BinaryMessage("test".getBytes());
        SpringWebSocketHandler.sendMessageToUsers(webSocketMessage);
    }
}
