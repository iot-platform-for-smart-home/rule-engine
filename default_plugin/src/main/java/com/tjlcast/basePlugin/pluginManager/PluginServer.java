package com.tjlcast.basePlugin.pluginManager;

import com.tjlcast.basePlugin.service.DefaultService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * Created by tangjialiang on 2018/5/4.
 */
@Component
public class PluginServer implements ApplicationContextAware, InitializingBean {

    @Autowired
    DefaultService service;

    private String pluginInfo ;
    private String detailInfo ;

    private PluginRegistry pluginRegistry ;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (pluginRegistry != null) {
            pluginRegistry.register(pluginInfo,detailInfo); // 注册服务地址
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 得到所有RpcService注解的SpringBean
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Plugin.class);
        if (MapUtils.isNotEmpty(beansWithAnnotation)) {
            for (Object serviceBean :
                    beansWithAnnotation.values()) {
                // 得到类上的Plugin标签[因为cglib，可能在父类上]
                Class<?> aClass = serviceBean.getClass() ;

//                Class<?> aClass1 = serviceBean.getClass();
//                Plugin annotation = aClass1.getAnnotation(Plugin.class);
//                String name = serviceBean.getClass().getName();
//
//                if (serviceBean.getClass().equals(DefaultService.class)) {
//                    aClass = serviceBean.getClass() ;
//                } else if (serviceBean.getClass().getSuperclass().equals(DefaultService.class)) {
//                    aClass = serviceBean.getClass().getSuperclass() ;
//                }


                String pluginInfo = aClass.getAnnotation(Plugin.class).pluginInfo();
                String registerAddr = aClass.getAnnotation(Plugin.class).registerAddr() ;

/*                String host = null;
                try {
                    host = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException e) {
                    System.out.println(e);
                }*/
                String detailInfo = aClass.getAnnotation(Plugin.class).detailInfo() ;
/*                detailInfo = host+":8300|"+detailInfo;*/

                this.pluginInfo = pluginInfo ;
                this.detailInfo = detailInfo ;

                pluginRegistry = new PluginRegistry(registerAddr) ;
            }
        }
    }
}
