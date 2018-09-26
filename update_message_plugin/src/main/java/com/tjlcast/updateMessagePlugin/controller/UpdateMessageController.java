package com.tjlcast.updateMessagePlugin.controller;


import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tjlcast.basePlugin.aop.ConfirmActive;
import com.tjlcast.basePlugin.common.ZKConstant;
import com.tjlcast.basePlugin.pluginManager.Plugin;
import com.tjlcast.updateMessagePlugin.common.Constant;
import com.tjlcast.updateMessagePlugin.data.UpdateMessage;
import com.tjlcast.updateMessagePlugin.service.CheckAndSendMessage;
import com.tjlcast.updateMessagePlugin.service.UpdateMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.List;


@RestController
@RequestMapping("/api/v1/updatemessageplugin")
@Plugin(pluginInfo = "UpdateMessagePlugin", registerAddr = ZKConstant.ZK_ADDRESS, detailInfo = "updatemessageplugin:8500|use for updating message")
public class UpdateMessageController {

    private final String controllerName = UpdateMessageController.class.getSimpleName() ;

    private MetricRegistry metrics ;
    private Counter pendingJobs ;

    @Autowired
    public void setMetrics(MetricRegistry metrics) {
        this.metrics = metrics ;
        this.pendingJobs = this.metrics.counter(controllerName) ;
    }
    
    @Autowired
    UpdateMessageService updateMessageService;

   /* @Autowired
    KafkaProducer kafkaProducer;*/

    @Autowired
    CheckAndSendMessage checkAndSendMessage;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate ;

    @Autowired
    HttpServletRequest request;

    @ConfirmActive
    @RequestMapping(value = "/updateMessage/insert", method = RequestMethod.POST)
    public String insertMessage(@RequestBody String msg){

        JsonObject jsonObj = (JsonObject)new JsonParser().parse(msg);
        UpdateMessage addMsg = new UpdateMessage(jsonObj);
        UpdateMessage insertmsg = updateMessageService.insertMessage(addMsg);

        pendingJobs.inc();

        checkAndSendMessage.checkAndSendMessage(insertmsg);

        return "success";
    }

    @RequestMapping(value = "/updateMessage/{messageType}/{tenantId}", method = RequestMethod.GET)
    public List<UpdateMessage> getUpdateMessageByType(@PathVariable("messageType") String msgType,
                                                      @PathVariable("tenantId") Integer tenantId){
        List<UpdateMessage> updateMessages = updateMessageService.getUpdateMessageByType(msgType, tenantId);
        return updateMessages;
    }

    @RequestMapping(value = "/updateMessage/{tenantId}/fromWeb", method = RequestMethod.GET)
    public List<UpdateMessage> getFromWebMessage(@PathVariable("tenantId") Integer tenantId){
        List<UpdateMessage> updateMessages = updateMessageService.getFromWebMessage(tenantId);
        return updateMessages;
    }

    @RequestMapping(value = "/updateMessage/{tenantId}/fromModule", method = RequestMethod.GET)
    public List<UpdateMessage> getFromModuleMessage(@PathVariable("tenantId") Integer tenantId){
        List<UpdateMessage> updateMessages = updateMessageService.getFromModuleMessage(tenantId);
        return updateMessages;
    }

    @RequestMapping(value = "/allUpdateMessage/{tenantId}", method = RequestMethod.GET)
    public List<UpdateMessage> getAllMessage(@PathVariable("tenantId") Integer tenantId){
        List<UpdateMessage> updateMessages = updateMessageService.getAllMessage(tenantId);
        return updateMessages;
    }

    @RequestMapping(value = "/updateMessage/{tenantId}/{id}", method = RequestMethod.GET)
    public UpdateMessage getMessageById(@PathVariable("tenantId") Integer tenantId, @PathVariable("id") Integer id){
        UpdateMessage updateMessage = updateMessageService.getMessageById(tenantId, id);
        return updateMessage;
    }

    @RequestMapping(value = "/updateMessage/{tenantId}/{startTs}/{endTs}", method = RequestMethod.GET)
    public List<UpdateMessage> getTsMessage(@PathVariable("tenantId") Integer tenantId,
                                            @PathVariable("startTs") BigInteger startTs,
                                            @PathVariable("endTs") BigInteger endTs){
        List<UpdateMessage> updateMessages = updateMessageService.getTsMessage(tenantId, startTs, endTs);
        return updateMessages;
    }

    @RequestMapping(value = "/removeAllMessage", method = RequestMethod.DELETE)
    public void removeAllMessage(){
        updateMessageService.removeAllMessage();
    }

    @RequestMapping(value = "/removeMessage/{id}", method = RequestMethod.DELETE)
    public void removeMessageById(@PathVariable("id") Integer id){
        updateMessageService.removeMessageById(id);
    }

    @MessageMapping("/fromModule")
    public void getFromModuleMsgBySocket(String jsonStr){
        JsonObject jsonObject = (JsonObject)new JsonParser().parse(jsonStr);
        Integer tenantId = jsonObject.get("tenantId").getAsInt();

        List<UpdateMessage> updateMessages = updateMessageService.getFromModuleMessage(tenantId);
       /* for(UpdateMessage updateMessage:updateMessages){
            simpMessagingTemplate.convertAndSend(Constant.SOCKET_UPDATEMESSAGE_RESPONSE+"/fromModule", updateMessage);
        }*/
        simpMessagingTemplate.convertAndSend(Constant.SOCKET_UPDATEMESSAGE_RESPONSE+"/fromModule/"+tenantId, updateMessages);
    }

    @MessageMapping("/fromWeb")
    public void getFromWebMsgBySocket(String jsonStr){
        JsonObject jsonObject = (JsonObject)new JsonParser().parse(jsonStr);
        Integer tenantId = jsonObject.get("tenantId").getAsInt();

        List<UpdateMessage> updateMessages = updateMessageService.getFromWebMessage(tenantId);
       /* for(UpdateMessage updateMessage:updateMessages){
            simpMessagingTemplate.convertAndSend(Constant.SOCKET_UPDATEMESSAGE_RESPONSE+"/fromModule", updateMessage);
        }*/
        simpMessagingTemplate.convertAndSend(Constant.SOCKET_UPDATEMESSAGE_RESPONSE+"/fromWeb/"+tenantId, updateMessages);
    }


}
