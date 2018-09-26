package com.tjlcast.server.actors.tenant;


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

public class TenantActor extends ContextAwareActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this) ;

    private final Integer tenantId ;
    private final Map<Integer, ActorRef> ruleActors ;

    public TenantActor(ActorSystemContext context, Integer tenantId) {
        super(context) ;
        this.tenantId = tenantId;
        List<Rule> rules= systemContext.getRuleService().findRuleByTenantId(tenantId);
        ruleActors = new HashMap<Integer, ActorRef>() ;
        for  (Rule rule : rules)
        {
            if(rule.getState().equals("ACTIVE")) {
                getOrCreateRuleActor(rule.getRuleId());
            }
        }
    }

    public void initialTenantActor() {
        // todo
        // load the rule of this tenant.
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
                k -> context().actorOf(Props.create(new RuleActor.ActorCreator(systemContext, tenantId, ruleId)).withDispatcher(DefaultActorService.CORE_DISPATCHER_NAME),
                        ruleId.toString())
        ) ;
    }

    public static class ActorCreator extends ContextBasedCreator<TenantActor> {
        private static final long serialVersionUID = 1L ;

        private final Integer tenantId ;

        public ActorCreator(ActorSystemContext context, Integer tenantId) {
            super(context);
            this.tenantId = tenantId ;
        }

        @Override
        public TenantActor create() throws  Exception {
            return new TenantActor(context, tenantId) ;
        }
    }
}
