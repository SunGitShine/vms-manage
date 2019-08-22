package com.juma.vms.manage.init.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.juma.vms.transport.service.InitService;

@Controller
@RequestMapping("init")
public class InitServiceController {
    
    @Resource
    private InitService initService;
    
    @ResponseBody
    @RequestMapping(value = "initDriver")
    public void initDriver() {
        initService.initDriver();
    }
    
    @ResponseBody
    @RequestMapping(value = "initTruck")
    public void initTruck() {
        initService.initTruck();
    }
    
    @ResponseBody
    @RequestMapping(value = "initPool")
    public void buildPool() {
        initService.initPool();
    }


}
