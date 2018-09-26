package com.tjlcast.server.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tjlcast.server.data.GenerateData.DefaultGenerator;
import com.tjlcast.server.data.GenerateData.Generator2Kafka;
import com.tjlcast.server.data.GenerateData.Generator2Stdout;
import com.tjlcast.server.data_source.DataSourceProcessor;
import com.tjlcast.server.data_source.FromMsgMiddlerDeviceMsg;
import com.tjlcast.server.services.FilterService;
import com.tjlcast.server.services.Rule2FilterService;
import com.tjlcast.server.services.RuleService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.lang.ref.SoftReference;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tangjialiang on 2018/4/22.
 */

@RestController
@RequestMapping("/api/test")
@Slf4j
public class TestMiddlerMsgController extends BaseContoller {

    @Autowired
    Rule2FilterService rule2FilterService;

    @Autowired
    FilterService filterService;

    @Autowired
    RuleService ruleService;

    @Autowired
    DataSourceProcessor dataSourceProcessor ;

    @Autowired
    KafkaTemplate kafkaTemplate;

    // 线程池
    private ThreadPoolExecutor threadsPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    // 运行任务
    private ConcurrentHashMap<Integer, SoftReference<DefaultGenerator>> tasks = new ConcurrentHashMap<>() ;

    // 任务编号生成器
    private AtomicInteger taskNo = new AtomicInteger(0);

    // soft queue ???
    // private ReferenceQueue refQueue = new ReferenceQueue() ;

    @ApiOperation(value = "测试：模拟从kafka中拉取数据")
    @RequestMapping(value = "/deviceMsg", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String postDeviceMsg(@RequestBody String jsonStr) {
        log.info("MiddlerMsgController receive a msg : " + jsonStr) ;

        // str 2 obj
        JsonObject jsonObj = (JsonObject)new JsonParser().parse(jsonStr);
        FromMsgMiddlerDeviceMsg fromMsgMiddlerDeviceMsg = new FromMsgMiddlerDeviceMsg(jsonObj);
        /**
        Random random = new Random();
        Rule rule = new Rule((int)(Math.random()*100000),fromMsgMiddlerDeviceMsg.getTenantId(),"Rule"+random.nextInt(),8901);
        ruleService.addRule(rule);

        Filter filter1 = new Filter((int)(Math.random()*100000),"function filter(key,value){if(key=='x' && value>0){ return true;} else{return false;}}");
        filterService.addFilter(filter1);

        Filter filter2 = new Filter((int)(Math.random()*100000),"function filter(key,value){if(key=='x' && value>1){ return true;} else{return false;}}");
        filterService.addFilter(filter2);

        Rule2Filter rule2Filter= new Rule2Filter(rule.getId(),filter1.getId());
        rule2FilterService.addARelation(rule2Filter)
        ;
        Rule2Filter rule2Filter2= new Rule2Filter(rule.getId(),filter2.getId());
        rule2FilterService.addARelation(rule2Filter2);
**/
        dataSourceProcessor.process(fromMsgMiddlerDeviceMsg);

        return "OK" ;
    }

    @ApiOperation(value = "测试：okhttp请求接受成功")
    @RequestMapping(value = "/receive", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String receive(@RequestBody String jsonstr){

        System.out.println(jsonstr);
        return "success";
    }

    /**
     * Don't do this.
     *
     * @deprecated
     * @throws InterruptedException
     */
    @ApiOperation(value = "测试：间隔0.5z一条数据持续发送")
    @RequestMapping(value = "/send", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void queryAllItem() throws InterruptedException {
        while(true) {
            Thread.sleep(500);
            kafkaTemplate.send("deviceData", "", "{\n" + "\t\"deviceId\": \"1\",\n" + "\t\"tenantId\": \"1\",\n" + "\t\"data\": [{\n" + "\t\t\"key\": \"x\",\n" + "\t\t\"ts\": \"1524708830000\",\n" + "\t\t\"value\": \"2.00\"\n" + "\t}]\n" + "}");
        }

    }

    @ApiOperation(value = "测试：....")
    @RequestMapping(value = "/randSend", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void addSendTask() {
        Generator2Stdout generator2Stdout = new Generator2Stdout(10);
        threadsPool.submit(generator2Stdout) ;
        SoftReference<DefaultGenerator> task = new SoftReference<>(generator2Stdout) ;
        int no = taskNo.getAndIncrement();
        tasks.put(no, task) ;
    }

    @ApiOperation(value = "测试：....")
    @RequestMapping(value = "/sendKafka", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void addKafkaTask() {
        Generator2Kafka generator2Kafka = new Generator2Kafka(100, kafkaTemplate);
        threadsPool.submit(generator2Kafka) ;
        SoftReference<DefaultGenerator> task = new SoftReference<DefaultGenerator>(generator2Kafka) ;
        int no = taskNo.getAndIncrement();
        tasks.put(no, task) ;
    }

    @ApiOperation(value = "测试：....")
    @RequestMapping(value = "/remove/{taskNo}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void removeSendTask(@PathVariable int taskNo) {
        tasks.computeIfPresent(taskNo, (k, v) -> {
            DefaultGenerator defaultGenerator = v.get();
            if (defaultGenerator!=null) defaultGenerator.cancel() ;
            return null ;
        }) ;
    }

    @ApiOperation(value = "测试：....")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Set<?> listTasks() throws InterruptedException {
        return tasks.keySet() ;
    }
}
