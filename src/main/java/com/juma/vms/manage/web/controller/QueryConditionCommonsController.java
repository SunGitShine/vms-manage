package com.juma.vms.manage.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.juma.auth.conf.domain.BusinessArea;
import com.juma.auth.employee.domain.Department;
import com.juma.auth.employee.domain.LoginEmployee;
import com.juma.vms.tool.domain.SmsCode;
import com.juma.vms.tool.domain.VmsDriverCustomerInfo;
import com.juma.vms.tool.service.AuthCommonService;
import com.juma.vms.tool.service.BusinessAreaCommonService;
import com.juma.vms.tool.service.CrmCommonService;
import com.juma.vms.tool.service.MessageService;
import com.juma.vms.vendor.enumeration.VendorSourceEnum;
import io.swagger.annotations.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.juma.conf.domain.ConfParamOption;
import com.juma.conf.service.ConfParamService;
import com.juma.server.vm.common.DriverTypeEnum;
import com.juma.vms.manage.web.vo.BaseRequestVo;
import com.juma.vms.vendor.enumeration.VendorTypeEnum;

/**
 * @author Libin.Wei
 * @version 1.0.0
 * @ClassName QueryConditionCommonsController.java
 * @Description 公共查询条件获取
 * @Date 2017年12月21日 下午2:27:22
 * @Copyright 2016 www.jumapeisong.com Inc. All rights reserved.
 */

@Api("QueryCondition-Commons-Controller")
@Controller
@RequestMapping("query/condition/commons")
public class QueryConditionCommonsController {

    @Resource
    private ConfParamService confParamService;
    @Resource
    private CrmCommonService crmCommonService;
    @Resource
    private BusinessAreaCommonService businessAreaCommonService;
    @Resource
    private MessageService messageService;
    @Resource
    private AuthCommonService authCommonService;

    @ApiOperation(value = "承运商类型列表")
    @ResponseBody
    @RequestMapping(value = "listVendorType", method = RequestMethod.GET)
    public Object listVendorType() {
        List<BaseRequestVo> result = new ArrayList<BaseRequestVo>();
        for (VendorTypeEnum t : VendorTypeEnum.values()) {
            BaseRequestVo vo = new BaseRequestVo();
            vo.setCode(Integer.valueOf(t.getCode()));
            vo.setDesc(t.getDesc());
            result.add(vo);
        }

        return result;
    }

    @ApiOperation(value = "承运商运营类型列表")
    @ResponseBody
    @RequestMapping(value = "listVendorSource", method = RequestMethod.GET)
    public Object listVendorSource() {
        List<BaseRequestVo> result = new ArrayList<BaseRequestVo>();
        for (VendorSourceEnum v : VendorSourceEnum.values()) {
            BaseRequestVo vo = new BaseRequestVo();
            vo.setCode(Integer.valueOf(v.getCode()));
            vo.setDesc(v.getDesc());
            result.add(vo);
        }

        return result;
    }

    @ApiOperation(value = "司机类型列表")
    @ResponseBody
    @RequestMapping(value = "list/driverType", method = RequestMethod.GET)
    public Object listDriverType() {
        List<BaseRequestVo> result = new ArrayList<BaseRequestVo>();
        for (DriverTypeEnum t : DriverTypeEnum.values()) {
            BaseRequestVo vo = new BaseRequestVo();
            vo.setCode(Integer.valueOf(t.getCode()));
            vo.setDesc(t.getDesc());
            result.add(vo);
        }

        return result;
    }

    @ApiOperation(value = "司机准驾车型列表", notes = "数据返回的是司机驾驶证类型列表")
    @ResponseBody
    @RequestMapping(value = "list/canDriverType", method = RequestMethod.GET)
    public Object listCanDriverType() {
        List<BaseRequestVo> result = new ArrayList<BaseRequestVo>();
        List<ConfParamOption> list = confParamService.findParamOptions("CRM_DRIVERS_LICENSE_TYPE");
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }

        for (ConfParamOption c : list) {
            if (StringUtils.isBlank(c.getOptionValue()) || !StringUtils.isNumeric(c.getOptionValue())) {
                continue;
            }

            BaseRequestVo vo = new BaseRequestVo();
            vo.setCode(Integer.valueOf(c.getOptionValue()));
            vo.setDesc(c.getOptionName());
            result.add(vo);
        }

        return result;
    }

    @ApiOperation(value = "获取司机类型的客户列表", notes = "callbackPageSize为最大返回数据量，默认15条，拼接于URL")
    @ApiParam(name = "callbackPageSize", value = "设置每次访问最大返回条数,默认15条，最大200条")
    @ResponseBody
    @RequestMapping(value = "list/driverCustomerInfo", method = RequestMethod.POST)
    public Object listlistDriverCustomerInfo(@RequestBody VmsDriverCustomerInfo vmsDriverCustomerInfo,
                                             Integer callbackPageSize,
                                             LoginEmployee loginEmployee) {
        return crmCommonService.listByName(vmsDriverCustomerInfo.getCrmDrivercustomerName(), callbackPageSize,
                loginEmployee);
    }


    @ApiOperation(value = "获取向上第一个逻辑区域及自己的名字")
    @ApiImplicitParam(paramType = "path", value = "业务区域编码", name = "areaCode")
    @ResponseBody
    @RequestMapping(value = "load/{areaCode}/logicAndSelfAreaName", method = RequestMethod.POST)
    public String loadLogicAndSelfAreaName(@PathVariable String areaCode, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        return businessAreaCommonService.loadLogicAndSelfAreaName(areaCode, loginEmployee);
    }


    @ApiOperation(value = "发送短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "phone", name = "手机号"),
            @ApiImplicitParam(value = "pastPhone", name = "旧手机号"),
            @ApiImplicitParam(value = "name", name = "姓名"),
    })
    @ResponseBody
    @RequestMapping(value = "pushSmsCode", method = RequestMethod.POST)
    public void pushSmsCode(@RequestBody SmsCode smsCode, LoginEmployee loginEmployee) {
        messageService.pushSmsCode(smsCode,loginEmployee);
    }

    @ApiOperation(value = "验证短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "phone", name = "手机号"),
            @ApiImplicitParam(value = "securityCode", name = "手机验证码"),
    })

    @ResponseBody
    @RequestMapping(value = "verifySmsCode", method = RequestMethod.POST)
    public Boolean verifySmsCode(@RequestBody SmsCode smsCode) {
        return messageService.verifySmsCode(smsCode);
    }

    @ResponseBody
    @RequestMapping(value = "businessArea/{parentBusinessAreaId}/children", method = RequestMethod.GET)
    public List<BusinessArea> childrenBusinessArea(@PathVariable Integer parentBusinessAreaId, LoginEmployee loginEmployee) {
        return businessAreaCommonService.listChildBusinessArea(parentBusinessAreaId, loginEmployee);
    }

    @ResponseBody
    @RequestMapping(value = "department/{parentDepartmentId}/children", method = RequestMethod.GET)
    public List<Department> childrenDepartment(@PathVariable Integer parentDepartmentId, LoginEmployee loginEmployee) {
        return authCommonService.listChildDepartment(parentDepartmentId, loginEmployee);
    }
}
