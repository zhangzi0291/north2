package com.north.msg.service;

import java.util.Collection;

public interface IMsgService {

    void sendMsg(Collection<String> tos, String title, String content);


}
