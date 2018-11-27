package com.tjlcast.server.actors.rule;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.tjlcast.server.actors.ActorSystemContext;
import com.tjlcast.server.actors.service.ContextAwareActor;
import com.tjlcast.server.actors.service.ContextBasedCreator;
import com.tjlcast.server.data.Filter;
import com.tjlcast.server.data_source.FromMsgMiddlerDeviceMsg;
import com.tjlcast.server.message.DeviceRecognitionMsg;

import java.util.List;

/**
 * Created by tangjialiang on 2017/12/8.
 */
public class RuleActor extends ContextAwareActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this) ;

//    private final Integer tenantId ;
    private final String gatewayId;
    private final Integer ruleId ;
    private final RuleActorMessageProcessor processor;
    public final List<Filter> filters; // todo


    private RuleActor(ActorSystemContext context,String gatewayId, Integer ruleId) {
        super(context) ;
//        this.tenantId = tenantId ;
        this.gatewayId = gatewayId;
        this.ruleId = ruleId ;
        this.processor = new RuleActorMessageProcessor(systemContext, logger, ruleId, this);
        this.filters = systemContext.getFilterService().findFilterByRuleId(ruleId);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof DeviceRecognitionMsg){

        } else if (message instanceof FromMsgMiddlerDeviceMsg) {
            processor.process((FromMsgMiddlerDeviceMsg)message);
        }

    }

    public static class ActorCreator extends ContextBasedCreator<RuleActor> {
        private static final long serialVersionUID = 1L ;

        private final String gatewayId ;
        private final Integer ruleId ;

        public ActorCreator(ActorSystemContext context, String gatewayId, Integer ruleId) {
            super(context) ;
            this.gatewayId = gatewayId ;
            this.ruleId = ruleId ;
        }

        @Override
        public RuleActor create() throws Exception {
            return new RuleActor(context, gatewayId, ruleId) ;
        }
    }
}
