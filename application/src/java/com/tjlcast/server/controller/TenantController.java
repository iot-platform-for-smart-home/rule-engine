package com.tjlcast.server.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Created by tangjialiang on 2018/4/13.
 */

@RestController
@RequestMapping("/api/tenant")
@Slf4j
public class TenantController extends BaseContoller {

    @ApiOperation(value = "todo ***")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String addTenant(@RequestBody String jsonStr) {
        // todo
        // 1. decode jsonStr to be a Tenant obj
        // 2. add a gateway.
        return "OK" ;
    }

    @ApiOperation(value = "todo ***")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String deleteTenant(@RequestParam String idStr) {
        // todo
        // 2. remove a gateway.
        return "Ok" ;
    }
}
