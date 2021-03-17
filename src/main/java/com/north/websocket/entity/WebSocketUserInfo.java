package com.north.websocket.entity;

import java.util.StringJoiner;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-15
 */
public class WebSocketUserInfo {

    private String userId;
    private String nickname;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", WebSocketUserInfo.class.getSimpleName() + "[", "]")
                .add("userId='" + userId + "'")
                .add("nickname='" + nickname + "'")
                .toString();
    }
}
