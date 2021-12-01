package com.north.websocket.handler;

import com.north.sys.service.ISysLogService;
import com.north.sys.service.ISysUserService;
import com.north.utils.SpringUtil;
import com.north.utils.SystemUtil;
import com.north.websocket.entity.WebSocketUserInfo;
import com.north.websocket.protobuf.Protobuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-09-23
 */
public class HardwareInfoWebSocketHandler extends TextWebSocketHandler {

    private static Logger logger = LoggerFactory.getLogger(HardwareInfoWebSocketHandler.class);

    //Map来存储WebSocketSession，key用USER_ID 即在线用户列表
    public static final Map<String, WebSocketSession> users = new HashMap<String, WebSocketSession>();

    //用户标识
    //对应监听器从的key
    private static final String USER_ID = "WEBSOCKET_USERID";

    public HardwareInfoWebSocketHandler() {
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
    }

    /**
     * 文本处理
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        WebSocketUserInfo userInfo = (WebSocketUserInfo) session.getAttributes().get("userInfo");
        logger.debug("服务器收到消息：{}", message);
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
     * 给所有在线用户发送消息
     *
     */
    public static void sendMessageToUsers() {
        ISysUserService sysUserService = SpringUtil.getBean(ISysUserService.class);
        ISysLogService sysLogService = SpringUtil.getBean(ISysLogService.class);
        Protobuf.HomeInfo.Builder homeinfoBuilder = Protobuf.HomeInfo.newBuilder();
        homeinfoBuilder.setTodayUser(sysLogService.getTodayUser().intValue());
        homeinfoBuilder.setTotalUser((int)sysUserService.count());
        homeinfoBuilder.setOnlineUser(sysUserService.getTotalOnlineNum().intValue());

        Protobuf.HomeInfo.CpuInfo.Builder cpuInfo = Protobuf.HomeInfo.CpuInfo.newBuilder();
        Map<String, String> cpuInfoMap = SystemUtil.getSystemCpu();
        cpuInfo.setSystem(cpuInfoMap.get("system"));
        cpuInfo.setProcess(cpuInfoMap.get("process"));
        cpuInfo.setLoadAverage(cpuInfoMap.get("loadAverage"));
        homeinfoBuilder.setCpu(cpuInfo);

        Protobuf.HomeInfo.MemeryInfo.Builder memeryInfo = Protobuf.HomeInfo.MemeryInfo.newBuilder();
        Map<String, String> memeryInfoMap = SystemUtil.getSystemMemery();
        memeryInfo.setUsed(memeryInfoMap.get("used"));
        memeryInfo.setTotal(memeryInfoMap.get("total"));
        homeinfoBuilder.setMemery(memeryInfo);

        Map<String, Map<String, String>> diskInfoMap = SystemUtil.getSystemDisk();
        AtomicInteger i = new AtomicInteger(1);
        diskInfoMap.forEach((String k, Map<String, String> v)->{
            Protobuf.HomeInfo.DiskInfo.Builder diskInfo = Protobuf.HomeInfo.DiskInfo.newBuilder();
            diskInfo.setUsed(v.get("used"));
            diskInfo.setTotal(v.get("total"));
            diskInfo.setName(k);
            homeinfoBuilder.addDisk(diskInfo);
        });
        AbstractWebSocketMessage message = new BinaryMessage(homeinfoBuilder.build().toByteArray());
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
