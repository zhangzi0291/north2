package com.north.cron;

import com.north.websocket.handler.HardwareInfoWebSocketHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-09-23
 */
@Component
public class HomeCron {

    @Scheduled(fixedDelay = 3000)
    public void homeInfoCron(){
        if(HardwareInfoWebSocketHandler.users.size()>0) {
            HardwareInfoWebSocketHandler.sendMessageToUsers();
        }
    }

}
