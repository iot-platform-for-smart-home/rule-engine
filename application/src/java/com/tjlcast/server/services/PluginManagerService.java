package com.tjlcast.server.services;

import com.tjlcast.server.common.Constant;
import com.tjlcast.server.data.Plugin;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by tangjialiang on 2018/5/4.
 */
@Service
@Slf4j
public class PluginManagerService implements InitializingBean {

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate ;

    @Value("${zookeeper.address}")
    String zkAddress ;

    PluginDiscovery pluginDiscovery ;

    public List<Plugin> getPluginsInfo() {
        return pluginDiscovery.discover() ;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pluginDiscovery = new PluginDiscovery(zkAddress) ;

        // update plugin's metrics （http from plugins） info to webscoket
        Observable
                .interval(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .subscribe(
                        new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                List<Plugin> pluginsInfo = getPluginsInfo();
                                pluginsInfo.stream().forEach(
                                        new Consumer<Plugin>() {
                                            @Override
                                            public void accept(Plugin plugin) {
                                                try {
                                                    String metrics = metrics(plugin.getUrl());  // metrics is function
                                                    simpMessagingTemplate.convertAndSend(Constant.SOCKET_METRIC_RESPONSE+"/"+plugin.getUrl().replace(":", "/"), metrics);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                );
                            }
                        }
                ) ;
    }

    /**
     * 得到Plugin的状态
     * @param url
     * @param port
     * @return
     * @throws IOException
     */
    public String getPluginState(String url, String port) throws IOException {
        String requestAddr = "/api/plugin/state";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://"+url+":"+port+requestAddr)
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * 激活Plugin
     * @param url
     * @param port
     * @return
     * @throws IOException
     */
    public String activate(String url, String port) throws IOException {
        String requestAddr = "/api/plugin/active";

        OkHttpClient client = new OkHttpClient();

        okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8")
                , "");

        Request request = new Request.Builder()
                .url("http://"+url+":"+port+requestAddr)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * 暂停Plugin
     * @param url
     * @param port
     * @return
     * @throws IOException
     */
    public String suspend(String url, String port) throws IOException {
        String requestAddr = "/api/plugin/suspend";

        OkHttpClient client = new OkHttpClient();

        okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8")
                , "");

        Request request = new Request.Builder()
                .url("http://"+url+":"+port+requestAddr)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }


    /**
     * 得到Plugin的所有metrics信息
     * @param url
     * @param port
     * @return
     * @throws IOException
     */
    public String metrics(String url, String port) throws IOException {
        String requestAddr = "/api/plugin/metrics";

        OkHttpClient client = new OkHttpClient() ;
        Request request = new Request.Builder()
                .url("http://" + url + ":" + port + requestAddr)
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public String metrics(String url) throws IOException {
        String[] split = url.split(":");
        return metrics(split[0], split[1]) ;
    }

    /**
     * 得到Plugin的所有的url地址
     * @param url
     * @param port
     * @return
     * @throws IOException
     */
    public String getAllUrls(String url, String port) throws IOException {
        String requestAddr = "/api/plugin/allUrls";

        OkHttpClient client = new OkHttpClient() ;
        Request request = new Request.Builder()
                .url("http://" + url + ":" + port + requestAddr)
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
}
