package com.tjlcast.server.actors.gateway;


import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.tjlcast.server.actors.ActorSystemContext;
import com.tjlcast.server.actors.rule.RuleActor;
import com.tjlcast.server.actors.service.ContextAwareActor;
import com.tjlcast.server.actors.service.ContextBasedCreator;
import com.tjlcast.server.actors.service.DefaultActorService;
import com.tjlcast.server.data.Rule;
import com.tjlcast.server.data_source.FromMsgMiddlerDeviceMsg;
import com.tjlcast.server.message.DeviceRecognitionMsg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tangjialiang on 2017/12/8.
 *
 */

public class GatewayActor extends ContextAwareActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this) ;

    private final String gatewayId ;
    private final Map<Integer, ActorRef> ruleActors ;

    public GatewayActor(ActorSystemContext context, String gatewayId) {
        super(context) ;
        this.gatewayId = gatewayId;
        List<Rule> rules= systemContext.getRuleService().findRuleByGatewayId(gatewayId);
        ruleActors = new HashMap<Integer, ActorRef>() ;
        for  (Rule rule : rules)
        {
            if(rule.getState().equals("ACTIVE")) {
                getOrCreateRuleActor(rule.getRuleId());
            }
        }
    }

    public void initialGatewayActor() {
        // todo
        // load the rule of this gateway.
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof DeviceRecognitionMsg) {
            // not sure.
            //List<Rule> rules=systemContext.getRuleService().findRuleByTenantId(tenantId);
           // for (Rule rule : rules)
            //{
            //    getOrCreateRuleActor(rule.getId()).tell(message,ActorRef.noSender());
            //}
        } else if (message instanceof FromMsgMiddlerDeviceMsg) {
            for (Map.Entry<Integer, ActorRef> entry : ruleActors.entrySet()) {
                ActorRef ruleActor = entry.getValue();
                ruleActor.tell(message, ActorRef.noSender());
            }
        }
    }

    private ActorRef getOrCreateRuleActor(final Integer ruleId) {
        return ruleActors.computeIfAbsent(
                ruleId,
                k -> context().actorOf(Props.create(new RuleActor.ActorCreator(systemContext, gatewayId, ruleId)).withDispatcher(DefaultActorService.CORE_DISPATCHER_NAME),
                        ruleId.toString())
        ) ;
    }

    public static class ActorCreator extends ContextBasedCreator<GatewayActor> {
        private static final long serialVersionUID = 1L ;

        private final String gatewayId ;

        public ActorCreator(ActorSystemContext context, String gatewayId) {
            super(context);
            this.gatewayId = gatewayId ;
        }

        @Override
        public GatewayActor create() throws  Exception {
            return new GatewayActor(context, gatewayId) ;
        }
    }
}
