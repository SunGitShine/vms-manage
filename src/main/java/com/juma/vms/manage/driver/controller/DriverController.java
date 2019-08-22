package com.juma.vms.manage.driver.controller;

import com.giants.common.tools.Page;
import com.juma.auth.employee.domain.LoginEmployee;
import com.juma.vms.common.query.QueryCond;
import com.juma.vms.driver.service.DriverService;
import com.juma.vms.driver.vo.DriverBo;
import com.juma.vms.driver.vo.DriverQuery;
import com.juma.vms.manage.web.controller.BaseController;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Title: DriverController
 * Description:司机管理
 * Created by gzq on 2019/3/19.
 */

@Api(value = "司机管理")
@Controller
@RequestMapping("driver")
public class DriverController extends BaseController {

    @Autowired
    private DriverService driverService;


    /**
     * 分页
     */
    @ApiOperation(value = "司机管理分页列表", notes = "司机管理分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "filters.areaCodeList", value = "业务范围list集合"),
            @ApiImplicitParam(name = "filters.driverId", value = "司机id", dataType = "Integer"),
            @ApiImplicitParam(name = "filters.phone", value = "手机号", dataType = "String"),
            @ApiImplicitParam(name = "filters.vendorId", value = "承运商id", dataType = "Integer"),
            @ApiImplicitParam(name = "filters.driverRunType", value = "司机类型：1自营2加盟3外请4其他", dataType = "Integer"),
    })
    @ResponseBody
    @RequestMapping(value = "search", method = RequestMethod.POST)
    public Page<DriverQuery> search(@RequestBody QueryCond<DriverQuery> queryCond, @ApiParam(hidden = true)LoginEmployee loginEmployee) {
        return driverService.search(queryCond, loginEmployee);
    }

    @ApiOperation(value = "添加或修改司机", notes = "driverId为空添加，不为空则编辑")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "driver.driverId", value = "司机id"),
            @ApiImplicitParam(name = "driver.idCardImg1", value = "身份证正面"),
            @ApiImplicitParam(name = "driver.idCardImg2", value = "身份证反面"),
            @ApiImplicitParam(name = "driver.driveLicenseImg1", value = "驾驶证照片正面"),
            @ApiImplicitParam(name = "driver.driveLicenseImg2", value = "驾驶证照片反面"),
            @ApiImplicitParam(name = "driver.name", value = "司机姓名"),
            @ApiImplicitParam(name = "driver.idCardNo", value = "身份证号"),
            @ApiImplicitParam(name = "driver.phone", value = "手机"),
            @ApiImplicitParam(name = "driver.canDriveType", value = "准驾车型"),
            @ApiImplicitParam(name = "driver.driveLicenseFirstTakeTime", value = "初次领证时间"),
            @ApiImplicitParam(name = "driver.driveLicenseEndTime", value = "驾驶证有效截止日期"),
            @ApiImplicitParam(name = "driverTenant.areaCode", value = "业务区域"),
            @ApiImplicitParam(name = "driver.emergencyContactPhone", value = "紧急手机"),
            @ApiImplicitParam(name = "vendorDriver.vendorId", value = "承运商id"),
            @ApiImplicitParam(name = "truckId", value = "车辆id"),
    })
    @ResponseBody
    @RequestMapping(value = "modify", method = RequestMethod.POST)
    public Integer modify(@RequestBody DriverBo driverBo, @ApiParam(hidden = true)LoginEmployee loginEmployee) {
        if (null != driverBo.getDriver() && null == driverBo.getDriver().getDriverId()) {
            Integer driverId = driverService.insert(driverBo, loginEmployee);
            return driverId;
        }
        //更改承运商
        if(null != driverBo.getVendorFlag() && driverBo.getVendorFlag()){
            driverService.updateVendor(driverBo, loginEmployee);
            return driverBo.getDriver().getDriverId();
        }
        driverService.update(driverBo, loginEmployee);
        return driverBo.getDriver().getDriverId();
    }

    @ResponseBody
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Integer insert(@RequestBody DriverBo driverBo, @ApiParam(hidden = true)LoginEmployee loginEmployee) {
        return driverService.insert(driverBo, loginEmployee);
    }

    @ResponseBody
    @RequestMapping(value = "modify/vendor", method = RequestMethod.POST)
    public Integer modifyVendor(@RequestBody DriverBo driverBo, @ApiParam(hidden = true)LoginEmployee loginEmployee) {
        driverService.updateVendor(driverBo, loginEmployee);
        return driverBo.getDriver().getDriverId();
    }

    @ApiOperation(value = "司机详情")
    @ResponseBody
    @RequestMapping(value = "{driverId}/detail", method = RequestMethod.GET)
    public DriverQuery getDriver(@PathVariable Integer driverId, @ApiParam(hidden = true)LoginEmployee loginEmployee) {
        return driverService.getDriverDetail(driverId, loginEmployee);
    }

    @ApiOperation(value = "司机列表模糊匹配", notes = "label=1,当前承运商下未关联车辆的司机；label=2,当前登录部门业务范围；label=3,当前承运商关联的司机或未关联承运商的司机")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "nameOrPhone", name = "司机名称或手机号"),
    })
    @ResponseBody
    @RequestMapping(value = "search/{label}/list", method = RequestMethod.POST)
    public List<DriverQuery> listByDriverFilter(@PathVariable Integer label,@RequestBody DriverQuery driverQuery, @ApiParam(hidden = true)LoginEmployee loginEmployee) {
        driverQuery.setLabel(label);
        return driverService.listByDriverFilter(driverQuery,loginEmployee);
    }

    @ApiOperation(value = "司机名称或电话模糊搜索", notes = "URL中的参数useTenant=0,全局查询；useTenant=1,当前登录人租户下查询")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "name", name = "名称"),
            @ApiImplicitParam(value = "phone", name = "电话"),
            @ApiImplicitParam(value = "idCardNo", name = "身份证号"),
    })
    @ResponseBody
    @RequestMapping(value = "search/{useTenant}/like/driver", method = RequestMethod.POST)
    public List<DriverQuery> loadLikeDriver(@PathVariable Integer useTenant, @RequestBody DriverQuery driverQuery,
                                                @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        useTenant = useTenant == null ? 0 : useTenant;
        DriverQuery filter = new DriverQuery();
        filter.setName(driverQuery.getName());
        filter.setPhone(driverQuery.getPhone());
        filter.setIdCardNo(driverQuery.getIdCardNo());
        filter.setExcludeTenant(driverQuery.getExcludeTenant());
        filter.setDriverTenantId(loginEmployee.getTenantId());

        return driverService.listByDriver(filter, 15, useTenant == 1 ? loginEmployee : null);
    }
}
