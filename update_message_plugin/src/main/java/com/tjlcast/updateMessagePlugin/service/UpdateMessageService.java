package com.tjlcast.updateMessagePlugin.service;

import com.tjlcast.basePlugin.service.DefaultService;
import com.tjlcast.updateMessagePlugin.data.UpdateMessage;
import com.tjlcast.updateMessagePlugin.mapper.UpdateMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Slf4j
@Service
//@CacheConfig(cacheNames="messageCache")
//@Transactional(propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class UpdateMessageService extends DefaultService{

    @Override
    public Object service(Object[] data) {
        return null;
    }

    @Autowired
    UpdateMessageMapper updateMessageMapper;

    //@CachePut(value = "messageCache", key = "#updateMessage.messageType") // id + type
    public UpdateMessage insertMessage(UpdateMessage updateMessage){
        this.updateMessageMapper.addAMessage(updateMessage);
        return updateMessage;
    }

    //@Cacheable(value = "messageCache", key = "#messageType")
    public List<UpdateMessage> getUpdateMessageByType(String messageType, Integer tenantId){
        return this.updateMessageMapper.getMessageByType(messageType, tenantId);
    }

    public List<UpdateMessage> getAllMessage(Integer tenantId){
        return this.updateMessageMapper.getAllMessage(tenantId);
    }

    public List<UpdateMessage> getFromWebMessage(Integer tenantId){
        return this.updateMessageMapper.getFromWebMessage(tenantId);
    }

    public List<UpdateMessage> getFromModuleMessage(Integer tenantId){
        return this.updateMessageMapper.getFromModuleMessage(tenantId);
    }

    public List<UpdateMessage> getTsMessage(Integer tenantId, BigInteger startTs, BigInteger endTs){
        return this.updateMessageMapper.getTsMessage(tenantId, startTs, endTs);
    }

    public UpdateMessage getMessageById(Integer tenantId, int id){
        return this.updateMessageMapper.getMessageById(tenantId, id);
    }

    public void removeAllMessage(){
        this.updateMessageMapper.removeAllMessage();
    }

    public void removeMessageById(int id){
        this.updateMessageMapper.removeMessageById(id);
    }


    public boolean checkMessageType(UpdateMessage updateMessage){
        if(updateMessage.getMessageType().equals("fromModule")){
            return true;
        } else {
            return false;
        }
    }
}
