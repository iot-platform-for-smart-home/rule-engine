package com.tjlcast.server.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Created by tangjialiang on 2018/4/22.
 */

@RestController
@RequestMapping("/api/filter")
@Slf4j
public class FilterContoller extends BaseContoller {

    @ApiOperation(value = "todo ***")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String addFilter(@RequestParam String jsonStr) {
        // todo
        // 1. get the json.
        // 2. decode jsonStr to be a filter.
        // 3. add filter to db.
        return "ok" ;
    }

    @ApiOperation(value = "todo ***")
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String removeFilter(@RequestParam String id) {
        // todo
        // 1. get the idStr
        // 2. remove the filter in db.
        return "ok" ;
    }

    @ApiOperation(value = "todo ***")
    @RequestMapping(value = "/filters", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getFilter() {
        // todo
        // 1. get the idStr
        // 2. remove the filter in db.
        return "ok" ;
    }
}
