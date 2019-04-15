package com.tjlcast.server.actors.app;


import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;
import com.tjlcast.server.actors.ActorSystemContext;
import com.tjlcast.server.actors.gateway.GatewayActor;
import com.tjlcast.server.actors.service.ContextAwareActor;
import com.tjlcast.server.actors.service.ContextBasedCreator;
import com.tjlcast.server.actors.service.DefaultActorService;
import com.tjlcast.server.data.Rule;
import com.tjlcast.server.data_source.FromMsgMiddlerDeviceMsg;
import com.tjlcast.server.message.DeviceRecognitionMsg;
import scala.concurrent.duration.Duration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tangjialiang on 2017/12/8.
 *
 * AppActor in the world of Actor
 */

public class AppActor extends ContextAwareActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this) ;

    private final Map<String, ActorRef> gatewayActors ;

    public AppActor(ActorSystemContext systemContext) {
        super(systemContext) ;
        this.gatewayActors = new HashMap<>() ;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof DeviceRecognitionMsg) {
            // not sure.
            //Integer deviceId =((DeviceRecognitionMsg) message).getDeviceId();
            //Device device=systemContext.getDeviceService().findDeviceById(deviceId);
            //Integer tenantId=device.getTenantId();
            //getOrCreateTenantActor(tenantId).tell(message,ActorRef.noSender());
        } else if (message instanceof FromMsgMiddlerDeviceMsg) {
            FromMsgMiddlerDeviceMsg mmessage = (FromMsgMiddlerDeviceMsg) message;
            String gatewayId = mmessage.getGatewayId();
            ActorRef orCreateGatewayActor = getOrCreateGatewayActor(gatewayId);
            orCreateGatewayActor.tell(message,ActorRef.noSender());
        } else if (message instanceof Rule){
            Rule rule =(Rule)message;
            if(gatewayActors.containsKey(rule.getGatewayId())) {
                getContext().stop(gatewayActors.get(rule.getGatewayId()));
                String gatewayId = rule.getGatewayId();
                this.gatewayActors.remove(gatewayId);
            }
        }
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy ;
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    private ActorRef getOrCreateGatewayActor(final String gatewayId) {
        return gatewayActors.computeIfAbsent(gatewayId,
                k -> context().actorOf(Props.create(new GatewayActor.ActorCreator(systemContext, gatewayId)).withDispatcher(DefaultActorService.CORE_DISPATCHER_NAME),
                        gatewayId));
    }

    // for creating the AppActor
    public static class ActorCreator extends ContextBasedCreator<AppActor> {
        /**
         * this class is the inner class of AppActor,
         * so could new an AppActor by this class.
         */

        public ActorCreator(ActorSystemContext context) {
            super(context);
        }

        @Override
        public AppActor create() throws Exception {
            return new AppActor(context) ;
        }
    }

    private final SupervisorStrategy strategy = new OneForOneStrategy(3,
            Duration.create("1 minute"),
            new Function<Throwable, Directive>() {
                @Override
                public Directive apply(Throwable param) throws Exception {
                    logger.error(param, "Unknown failure") ;
                    if (param instanceof RuntimeException) {
                        return SupervisorStrategy.restart() ;
                    } else {
                        return SupervisorStrategy.stop() ;
                    }
                }
            }) ;
}
