package com.north.websocket.handler;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.north.websocket.entity.WebSocketUserInfo;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-15
 */
public class SpringWebSocketHandlerInterceptor extends HttpSessionHandshakeInterceptor {


    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        if (StpUtil.isLogin()) {
            WebSocketUserInfo userInfo = getUserinfo(StpUtil.getTokenValue());
            attributes.put("userInfo", userInfo);
            return true;
        }
        return false;
//        return super.beforeHandshake(request, response, wsHandler, attributes);

    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
    }

    private WebSocketUserInfo getUserinfo(String token) {
        SaSession saSession = StpUtil.getSessionByLoginId(StpUtil.getLoginIdByToken(token));
        String userId = saSession.getDataMap().getOrDefault("userId", "").toString();
        String nickname = saSession.getDataMap().getOrDefault("nickname", "").toString();
        WebSocketUserInfo userInfo = new WebSocketUserInfo();
        userInfo.setUserId(userId);
        userInfo.setNickname(nickname);
        return userInfo;
    }

}
