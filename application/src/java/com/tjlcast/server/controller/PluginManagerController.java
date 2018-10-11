package com.tjlcast.server.controller;

import com.tjlcast.server.common.Constant;
import com.tjlcast.server.data.Plugin;
import com.tjlcast.server.services.PluginManagerService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * Created by tangjialiang on 2018/5/5.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/smartruler/plugin")
public class PluginManagerController {

    @Autowired
    PluginManagerService pluginManagerService ;

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate ;

    @ApiOperation(value = "todo ***")
    //@PreAuthorize("#oauth2.hasScope('all') OR isAuthenticated()")
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public List<Plugin> getAllPlugins() {
        return pluginManagerService.getPluginsInfo();
    }


    @ApiOperation(value = "todo ***")
    //@PreAuthorize("#oauth2.hasScope('all') OR isAuthenticated()")
    @RequestMapping(value = "/state/{url}/{port}", method = RequestMethod.GET)
    @ResponseBody
    public String getPluginState(@PathVariable("url") String url,@PathVariable("port") String port) throws IOException {
        return pluginManagerService.getPluginState(url, port);
    }


    @ApiOperation(value = "todo ***")
    //@PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/active/{url}/{port}", method = RequestMethod.POST)
    @ResponseBody
    public String activate(@PathVariable("url") String url, @PathVariable("port") String port) throws IOException {
        return pluginManagerService.activate(url, port) ;
    }


    @ApiOperation(value = "todo ***")
    //@PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/suspend/{url}/{port}", method = RequestMethod.POST)
    @ResponseBody
    public String suspend(@PathVariable("url") String url, @PathVariable("port") String port) throws IOException {
        return pluginManagerService.suspend(url, port) ;
    }

   // @PreAuthorize("#oauth2.hasScope('all') OR isAuthenticated()")
    @RequestMapping(value = "/metrics/{url}/{port}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String pluginMetrics(@PathVariable("url") String url, @PathVariable("port") String port) throws IOException {
        return pluginManagerService.metrics(url, port) ;
    }

    //@PreAuthorize("#oauth2.hasScope('all') OR isAuthenticated()")
    @RequestMapping(value = "/allUrls/{url}/{port}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String pluginRPCUrls(@PathVariable("url") String url, @PathVariable("port") String port) throws IOException {
        return pluginManagerService.getAllUrls(url, port) ;
    }

    // socket
    @MessageMapping("/details")
    public void pluginDetailsSocket(String url) throws IOException {
        String metrics = pluginManagerService.metrics(url);
        simpMessagingTemplate.convertAndSend(Constant.SOCKET_METRIC_RESPONSE+"/"+url.replace(":", "/"), metrics);
    }
}
