package com.tjlcast.server.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tjlcast.server.data.Transform;
import com.tjlcast.server.services.TransformService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transform")
@Slf4j
public class TransformController {

    @Autowired
    TransformService transformService;

    @ApiOperation(value = "todo ***")
    //@PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/remove/{transformId}", method = RequestMethod.DELETE, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String removeTransform(@PathVariable("transformId") String transformId){
        transformService.deleteById(Integer.valueOf(transformId));

        return "Delete Success";
    }

    @ApiOperation(value = "todo ***")
    //@PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String addATransform(@RequestBody String jsonStr){
        JsonObject jsonObj = (JsonObject)new JsonParser().parse(jsonStr);
        Transform transform=new Transform(jsonObj);

        transformService.addTransform(transform);

        return "Add Success";
    }
}
