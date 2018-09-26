package com.tjlcast.updateMessagePlugin.service;

import com.tjlcast.updateMessagePlugin.common.Constant;
import com.tjlcast.updateMessagePlugin.data.UpdateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class CheckAndSendMessage {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate ;

    public String checkAndSendMessage(UpdateMessage message){
        switch (message.getMessageType()){
            case "fromModule":
                simpMessagingTemplate.convertAndSend(Constant.SOCKET_UPDATEMESSAGE_RESPONSE+"/fromModule/"+message.getTenantId(), message);
                break;
            case "fromWeb":
                simpMessagingTemplate.convertAndSend(Constant.SOCKET_UPDATEMESSAGE_RESPONSE+"/fromWeb/"+message.getTenantId(), message);
                break;
            case "exception":
                simpMessagingTemplate.convertAndSend(Constant.SOCKET_UPDATEMESSAGE_RESPONSE+"/exception/"+message.getTenantId(), message);
                break;
        }
        return "success";
    }
}
