package com.tjlcast.server.actors.rule;


import akka.event.LoggingAdapter;
import com.tjlcast.server.actors.ActorSystemContext;
import com.tjlcast.server.actors.shared.AbstractContextAwareMsgProcessor;
import com.tjlcast.server.data.Filter;
import com.tjlcast.server.data.Transform;
import com.tjlcast.server.data_source.FromMsgMiddlerDeviceMsg;
import com.tjlcast.server.data_source.Item;
import com.tjlcast.server.message.DeviceRecognitionMsg;
import com.tjlcast.server.nashorn.Nashorn;
import okhttp3.*;
import scala.concurrent.duration.Duration;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by tangjialiang on 2017/12/12.
 */
public class RuleActorMessageProcessor extends AbstractContextAwareMsgProcessor {

    private Nashorn nashorn;

    private final Integer ruleId;
    private final List<Filter> filters;
    private final List<Transform> transforms;


    public RuleActorMessageProcessor(ActorSystemContext systemContext, LoggingAdapter logger, Integer ruleId) {
        super(systemContext, logger);
        this.ruleId = ruleId;
        this.filters=systemContext.getFilterService().findFilterByRuleId(ruleId);
        this.nashorn=new Nashorn();
        this.transforms=systemContext.getTransformService().getByRuleId(ruleId);

        initAttributes();
    }

    public RuleActorMessageProcessor(ActorSystemContext systemContext, LoggingAdapter logger, Integer ruleId, RuleActor belongActor) {
        super(systemContext, logger);
        this.ruleId = ruleId;
        this.filters=systemContext.getFilterService().findFilterByRuleId(ruleId);
        this.nashorn=new Nashorn();
        this.transforms=systemContext.getTransformService().getByRuleId(ruleId);

        initAttributes();
    }

    private void initAttributes() {
//        Device device = systemContext.getDeviceService().findDeviceById(deviceId);
//        this.deviceName = device.getName();
//        this.deviceType = device.getType();
//        this.deviceAttributes = new DeviceAttributes(fetchAttributes(DataConstants.CLIENT_SCOPE),
//                fetchAttributes(DataConstants.SERVER_SCOPE), fetchAttributes(DataConstants.SHARED_SCOPE));
        // 初始化设备影子
        //TODO 初始化设备影子并开启心跳
        initDeviceShadow();
        systemContext.getScheduler().schedule(Duration.create(2000, TimeUnit.MILLISECONDS),
                Duration.create(2000, TimeUnit.MILLISECONDS), new Runnable() {
                    @Override
                    public void run() {
                        //TODO 发送心跳
                    }
                },systemContext.getActorSystem().dispatcher());

    }


    private void initDeviceShadow(){
//        Device device = systemContext.getDeviceService().findDeviceById(deviceId);
//        String manufacture = device.getManufacture();
//        String deviceType = device.getDeviceType();
//        String model = device.getModel();
//        if(StringUtil.checkNotNull(manufacture,deviceType,model)){
//            deviceShadow = HttpUtil.getDeviceShadowDoc(manufacture,deviceType,model);
//            deviceShadow.addProperty("deviceId",device.getId().toString());
//            //TODO send to service midware
//        }
    };

//    private void refreshAttributes(DeviceAttributesEventNotificationMsg msg) {
//        if (msg.isDeleted()) {
//            msg.getDeletedKeys().forEach(key -> deviceAttributes.remove(key));
//        } else {
//            deviceAttributes.update(msg.getScope(), msg.getValues());
//        }
//    }

    public void process(DeviceRecognitionMsg msg){

//        Device device = systemContext.getDeviceService().findDeviceById(deviceId);
//        String manufacture = msg.getManufacture();
//        String deviceType = msg.getDeviceType();
//        String model = msg.getModel();
//        if(StringUtil.checkNotNull(manufacture,deviceType,model)){
//            deviceShadow = HttpUtil.getDeviceShadowDoc(manufacture,deviceType,model);
//            deviceShadow.addProperty("deviceId",device.getId().toString());
//            //TODO send to service midware
//        }
    }

    public void process(FromMsgMiddlerDeviceMsg msg) throws ScriptException, NoSuchMethodException {
        // todo

        if(nashornProcess(filters,msg))
        {
            for (Transform transform : transforms) {
                if (transform.getMethod().equals("POST")){
                    sendHTTPPOSTRequest(transform, msg);
                }
            }
        }

    }

    public boolean nashornProcess(List<Filter> filters, FromMsgMiddlerDeviceMsg msg) throws ScriptException, NoSuchMethodException {

        boolean result = true;

        for(Filter filter:filters) {
            boolean tag = false;

            for(Item item:msg.getData())
            {
                tag = tag || nashorn.invokeFunction(filter.getJsCode(), msg.getDeviceId(), msg.getName(), msg.getManufacture(), msg.getDeviceType(), msg.getModel(), item.getTs(), item.getKey(), Double.parseDouble(item.getValue()));
            }
            result=result && tag;
            if(!result) {
                break;
            }
        }

        return result;
    }

    public String sendHTTPPOSTRequest(Transform transform, FromMsgMiddlerDeviceMsg msg)
    {

        OkHttpClient client = new OkHttpClient();
        String checkRequestbody = transform.getRequestBody();
        if(checkRequestbody.contains("{name}")){
            checkRequestbody = checkRequestbody.replaceAll("\\{name\\}", msg.getName());
        }

        if(checkRequestbody.contains("{deviceId}")){
            checkRequestbody = checkRequestbody.replaceAll("\\{deviceId\\}", msg.getDeviceId());
        }

        if(checkRequestbody.contains("{tenantId}")){
            checkRequestbody = checkRequestbody.replaceAll("\\{tenantId\\}", msg.getTenantId().toString());
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8")
                , checkRequestbody);

        System.out.println(requestBody);

        Request request = new Request.Builder()
                .url(transform.getUrl()) //Todo 输入URL
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    System.out.println("Success");
                }
            }
        });
        return "Success";
    }



}
