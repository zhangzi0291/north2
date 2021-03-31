package com.north.msg.service.impl;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.north.msg.service.IMsgService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-26
 */
@Service
public class EmailMsgService implements IMsgService {
    @Value("${north.email.host:}")
    private String host;

    @Value("${north.email.port:}")
    private Integer port;

    @Value("${north.email.auth:}")
    private Boolean auth;

    @Value("${north.email.from:}")
    private String from;

    @Value("${north.email.user:}")
    private String user;

    @Value("${north.email.pass:}")
    private String pass;


    @Override
    public void sendMsg(Collection<String> tos, String title, String content) {
        MailAccount account = new MailAccount();
        account.setHost(this.host);
        account.setPort(this.port);
        account.setAuth(this.auth);
        account.setFrom(this.from);
        account.setUser(this.user);
        account.setPass(this.pass); //密码
        MailUtil.send(account, tos, title, content, false);
    }
}
