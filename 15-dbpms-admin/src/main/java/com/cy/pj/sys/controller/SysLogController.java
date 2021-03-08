package com.cy.pj.sys.controller;

import com.cy.pj.common.pojo.JsonResult;
import com.cy.pj.sys.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log/")
public class SysLogController {
    @Autowired
    private SysLogService sysLogService;
    @GetMapping("doFindPageObjects")
    public JsonResult doFindPageObjects(String username,Integer pageCurrent){
        return new JsonResult(sysLogService.findPageObjects(username,pageCurrent));
    }
}
