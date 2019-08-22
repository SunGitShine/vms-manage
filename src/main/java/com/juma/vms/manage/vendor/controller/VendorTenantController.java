package com.juma.vms.manage.vendor.controller;

import com.giants.common.exception.BusinessException;
import com.juma.auth.employee.domain.LoginEmployee;
import com.juma.vms.mq.service.MqService;
import com.juma.vms.vendor.domain.VendorTenant;
import com.juma.vms.vendor.service.VendorService;
import com.juma.vms.vendor.service.VendorTenantService;
import com.juma.vms.vendor.vo.VendorTenantBo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName VendorTenantController.java
 * @Description 请填写注释...
 * @author Libin.Wei
 * @Date 2018年11月5日 下午7:52:41
 * @version 1.0.0
 * @Copyright 2016 www.jumapeisong.com Inc. All rights reserved.
 */

@Controller
@RequestMapping("vendorTenant")
public class VendorTenantController {

    @Resource
    private VendorTenantService vendorTenantService;
    @Resource
    private VendorService vendorService;
    @Resource
    private MqService mqService;

    @ApiOperation(value = "编辑承运商", notes = "承运商使用方调用接口")
    @ResponseBody
    @RequestMapping(value = "modify", method = RequestMethod.POST)
    public void modify(@RequestBody VendorTenant vendorTenant, LoginEmployee loginEmployee) {
        vendorTenantService.update(vendorTenant, loginEmployee);
    }

    @ApiOperation(value = "删除承运商", notes = "承运商使用方调用接口，删除承运商与当前租户的绑定")
    @ResponseBody
    @RequestMapping(value = "{vendorId}/delete", method = RequestMethod.DELETE)
    public void delete(@PathVariable Integer vendorId, LoginEmployee loginEmployee) {
        VendorTenant vendorTenant = vendorTenantService.loadByVendorId(vendorId, loginEmployee);
        if (null == vendorTenant) {
            return;
        }

        vendorTenantService.delete(vendorTenant.getVendorTenantId(), loginEmployee);
    }

    @ApiOperation(value = "检查承运商在当前租户下是否已经存在")
    @ResponseBody
    @RequestMapping(value = "{vendorId}/checkExist", method = RequestMethod.GET)
    public void checkExist(@PathVariable Integer vendorId, LoginEmployee loginEmployee) {
        VendorTenant vendorTenant = vendorTenantService.loadByVendorId(vendorId, loginEmployee);
        if (null == vendorTenant) {
            return;
        }

        throw new BusinessException("vendorTenantExist", "vendorTenant.error.vendorTenantExist");
    }

    @ApiOperation(value = "启用承运商")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "承运商租户关联ID", name = "vendorTenantId", required = true),
            @ApiImplicitParam(value = "承运商ID", name = "vendorId", required = true),
            @ApiImplicitParam(value = "备注", name = "remark")
    })
    @ResponseBody
    @RequestMapping(value = "enable", method = RequestMethod.POST)
    public void doEnable(@RequestBody VendorTenantBo vendorTenantBo, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        if (null == vendorTenantBo.getVendorTenantId() || null == vendorTenantBo.getVendorId()) {
            return;
        }
        vendorTenantService.doEnable(vendorTenantBo.getVendorTenantId(), loginEmployee);
    }

    @ApiOperation(value = "停用承运商")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "承运商租户关联ID", name = "vendorTenantId", required = true),
            @ApiImplicitParam(value = "承运商ID", name = "vendorId", required = true),
            @ApiImplicitParam(value = "备注", name = "remark")
    })
    @ResponseBody
    @RequestMapping(value = "disable", method = RequestMethod.POST)
    public void doDisable(@RequestBody VendorTenantBo vendorTenantBo, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        if (null == vendorTenantBo.getVendorTenantId() || null == vendorTenantBo.getVendorId()) {
            return;
        }
        vendorTenantService.doDisable(vendorTenantBo.getVendorTenantId(), loginEmployee);
        mqService.sendVendorAfterUpdate(vendorTenantBo.getVendorId(), loginEmployee.getTenantId());
    }

    @ApiOperation(value = "绑定CRM司机类型的客户")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "承运商租户关联ID", name = "vendorTenantId", required = true),
            @ApiImplicitParam(value = "承运商ID", name = "vendorId", required = true),
            @ApiImplicitParam(value = "客户ID", name = "customerId", required = true),
            @ApiImplicitParam(value = "客户名称", name = "crmCustomerName", required = true),
            @ApiImplicitParam(value = "客户联系电话", name = "crmCustomerPhone", required = true)
    })
    @ResponseBody
    @RequestMapping(value = "bindCrmDriverTypeCustomer", method = RequestMethod.POST)
    public void bindCrmDriverTypeCustomer(@RequestBody VendorTenantBo vendorTenantBo, LoginEmployee loginEmployee) {
        vendorTenantService.doBindCrmDriverTypeCustomer(vendorTenantBo.getVendorTenantId(), vendorTenantBo.getCustomerId(), loginEmployee);
    }

    @ApiOperation(value = "校验选中的司机客户的名称和证件号和承运商的是否一致,返回true：一致，false：不一致")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "承运商ID", name = "vendorId", required = true),
            @ApiImplicitParam(value = "客户名称", name = "crmCustomerName", required = true),
            @ApiImplicitParam(value = "客户证件号", name = "crmIdentityCardNo", required = true)
    })
    @ResponseBody
    @RequestMapping(value = "checkCrmCustomer", method = RequestMethod.POST)
    public boolean checkCrmCustomer(@RequestBody VendorTenantBo vendorTenantBo, LoginEmployee loginEmployee) {
        return vendorService.checkCrmCustomer(vendorTenantBo.getVendorId(), vendorTenantBo.getCrmCustomerName(), vendorTenantBo.getCrmIdentityCardNo());
    }
}
