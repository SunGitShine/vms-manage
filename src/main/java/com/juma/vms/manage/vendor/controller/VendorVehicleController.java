package com.juma.vms.manage.vendor.controller;

import com.giants.common.tools.Page;
import com.giants.common.tools.PageCondition;
import com.juma.auth.employee.domain.LoginEmployee;
import com.juma.vms.tool.service.AmsCommonService;
import com.juma.vms.vendor.domain.VendorVehicle;
import com.juma.vms.vendor.service.VendorVehicleService;
import com.juma.vms.vendor.vo.QueryVehicleVo;
import com.juma.vms.vendor.vo.VendorVehicleQuery;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName VendorVehicleController.java
 * @Description 承运商车辆关系
 * @author Libin.Wei
 * @Date 2018年11月1日 下午8:44:26
 * @version 1.0.0
 * @Copyright 2016 www.jumapeisong.com Inc. All rights reserved.
 */

@Controller
@RequestMapping("vendorVehicle")
public class VendorVehicleController {

    @Resource
    private VendorVehicleService vendorVehicleService;
    @Resource
    private AmsCommonService amsCommonService;

    @ApiOperation(value = "承运商关联的车辆分页列表")
    @ResponseBody
    @RequestMapping(value = "search", method = RequestMethod.POST)
    public Page<VendorVehicleQuery> search(@RequestBody PageCondition pageCondition, LoginEmployee loginEmployee) {
        return vendorVehicleService.search(pageCondition, loginEmployee);
    }

    @ApiOperation(value = "添加车辆")
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public void add(@RequestBody VendorVehicle vendorVehicle, LoginEmployee loginEmployee) {
        vendorVehicleService.insert(vendorVehicle, loginEmployee);
    }

    @ApiOperation(value = "删除车辆")
    @ResponseBody
    @RequestMapping(value = "{vendorVehicleId}/delete", method = RequestMethod.DELETE)
    public void delete(@PathVariable Integer vendorVehicleId, LoginEmployee loginEmployee) {
        VendorVehicle vendorVehicle = vendorVehicleService.getVendorVehicle(vendorVehicleId);
        if (null == vendorVehicle) {
            return;
        }
        vendorVehicleService.delete(vendorVehicleId, loginEmployee);
    }

    @ApiOperation(value = "车辆列表", notes = "根据车牌号模糊查询")
    @ResponseBody
    @RequestMapping(value = "list/like/plateNumber", method = RequestMethod.POST)
    public List<QueryVehicleVo> listLikePlateNumber(@RequestBody QueryVehicleVo queryVehicleVo,
            LoginEmployee loginEmployee) {
        return amsCommonService.listByPlateNumber(queryVehicleVo.getPlateNumber(), loginEmployee);
    }
}
