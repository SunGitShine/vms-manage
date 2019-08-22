package com.juma.vms.manage.truck.controller;

import com.giants.common.tools.Page;
import com.juma.auth.employee.domain.LoginEmployee;
import com.juma.tgm.truck.domain.TruckType;
import com.juma.vms.common.query.QueryCond;
import com.juma.vms.manage.web.controller.BaseController;
import com.juma.vms.tool.domain.BaseEnumDomian;
import com.juma.vms.truck.domain.Truck;
import com.juma.vms.truck.domain.bo.TruckResp;
import com.juma.vms.truck.service.TruckService;
import com.juma.vms.truck.vo.TruckBo;
import com.juma.vms.truck.vo.TruckQuery;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Title: TruckController
 * Description:运力车辆
 * Created by gzq on 2019/3/19.
 */

@Api(value = "运力车辆")
@Controller
@RequestMapping("truck")
public class TruckController extends BaseController {

    @Autowired
    private TruckService truckService;

    @ApiOperation(value = "车辆分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaCodeList", value = "业务区域数组"),
            @ApiImplicitParam(name = "plateNumber", value = "车牌号"),
            @ApiImplicitParam(name = "truckTypeId", value = "车型"),
            @ApiImplicitParam(name = "vendorId", value = "承运商id"),
            @ApiImplicitParam(name = "driverId", value = "司机id"),
            @ApiImplicitParam(name = "truckRunType", value = "运营类型  1自营2加盟3外请"),
            @ApiImplicitParam(name = "truckBelongToCompany", value = "认领公司id"),
            @ApiImplicitParam(name = "status", value = "0:停用车辆  1:启用车辆"),
            @ApiImplicitParam(name = "truckIdentificationNo", value = "车架号"),
    })
    @ResponseBody
    @RequestMapping(value = "search", method = RequestMethod.POST)
    public Page<TruckQuery> search(@RequestBody QueryCond<TruckQuery> queryCond, @ApiParam(hidden = true)LoginEmployee loginEmployee) {
        return truckService.search(queryCond, loginEmployee);
    }

    @ApiOperation(value = "车辆详情")
    @ResponseBody
    @RequestMapping(value = "{truckId}/detail", method = RequestMethod.GET)
    public TruckResp getTruck(@PathVariable Integer truckId,@ApiParam(hidden = true)LoginEmployee loginEmployee) {
        return truckService.getTruckDetail(truckId, loginEmployee);
    }

    @ApiOperation(value = "编辑车辆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "truckId", value = "车辆id"),
            @ApiImplicitParam(name = "licenseType", value = "牌照类型  1:黄 2:绿 3:蓝"),
            @ApiImplicitParam(name = "truckIdentificationNo", value = "车架号"),
            @ApiImplicitParam(name = "areaCode", value = "业务区域"),
            @ApiImplicitParam(name = "truckTypeId", value = "车型"),
            @ApiImplicitParam(name = "energyType", value = "能耗类型"),
            @ApiImplicitParam(name = "energyOutType", value = "能耗排放类别"),
            @ApiImplicitParam(name = "goCityLicenseType", value = "入城证"),
            @ApiImplicitParam(name = "isTailBoard", value = "尾板"),
            @ApiImplicitParam(name = "truckBodyImg", value = "车辆图片"),
            @ApiImplicitParam(name = "licenseCertificateImg1", value = "行驶证正本"),
            @ApiImplicitParam(name = "licenseCertificateImg2", value = "行驶证副本"),
            @ApiImplicitParam(name = "permitLicenseImg1", value = "营运证正本"),
            @ApiImplicitParam(name = "permitLicenseImg2", value = "营运证副本"),
            @ApiImplicitParam(name = "status", value = "0:停用车辆  1:启用车辆"),
    })
    @ResponseBody
    @RequestMapping(value = "modify", method = RequestMethod.POST)
    public Integer modify(@RequestBody TruckBo truckBo, @ApiParam(hidden = true)LoginEmployee loginEmployee) {
        truckService.update(truckBo, loginEmployee);
        return truckBo.getTruckId();
    }

    @ApiOperation(value = "牌照、能耗、排放下拉选", notes = "回传code")
    @ResponseBody
    @RequestMapping(value = "property/list", method = RequestMethod.GET)
    public Map<String, List<BaseEnumDomian>> getProperty() {
        return truckService.getProperty();
    }

    @ApiOperation(value = "车型下拉选", notes = "回传truckTypeId")
    @ResponseBody
    @RequestMapping(value = "truckType/list", method = RequestMethod.GET)
    public List<TruckType> getTruckType(@ApiParam(hidden = true)LoginEmployee loginEmployee) {
        return truckService.getTruckType(loginEmployee);
    }

    @ApiOperation(value = "更换认领公司")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "truckId", value = "车辆id"),
            @ApiImplicitParam(name = "truckBelongToCompany", value = "车辆认领公司id"),
    })
    @ResponseBody
    @RequestMapping(value = "update/truckCompany", method = RequestMethod.POST)
    public void updateTruckCompany(@RequestBody TruckBo truckBo, @ApiParam(hidden = true)LoginEmployee loginEmployee) {
        truckService.updateTruckCompany(truckBo, loginEmployee);
    }

    @ApiOperation(value = "更换承运商")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "truckId", value = "车辆id"),
            @ApiImplicitParam(name = "vendorId", value = "承运商id"),
    })
    @ResponseBody
    @RequestMapping(value = "update/vendor", method = RequestMethod.POST)
    public void updateVendor(@RequestBody TruckBo truckBo, @ApiParam(hidden = true)LoginEmployee loginEmployee) {
        truckService.updateVendor(truckBo, loginEmployee);
    }

    @ApiOperation(value = "更换自营承运商")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "truckId", value = "车辆id"),
            @ApiImplicitParam(name = "vendorId", value = "承运商id"),
    })
    @ResponseBody
    @RequestMapping(value = "update/ownVendor", method = RequestMethod.POST)
    public void ownVendor(@RequestBody TruckBo truckBo, @ApiParam(hidden = true)LoginEmployee loginEmployee) {
        truckService.updateVendor(truckBo, loginEmployee);
    }

    @ApiOperation(value = "更换司机")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "truckId", value = "车辆id"),
            @ApiImplicitParam(name = "driverId", value = "司机id"),
            @ApiImplicitParam(name = "vendorId", value = "承运商id"),
    })
    @ResponseBody
    @RequestMapping(value = "update/driver", method = RequestMethod.POST)
    public void updateDriver(@RequestBody TruckBo truckBo, @ApiParam(hidden = true)LoginEmployee loginEmployee) {
        truckService.updateDriver(truckBo, loginEmployee);
    }

    @ApiOperation(value = "车牌号模糊搜索车辆", notes = "URL中的参数useTenant=0,全局查询；useTenant=1,当前登录人租户下查询")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "plateNumber", name = "车牌号"),
    })
    @ResponseBody
    @RequestMapping(value = "search/{useTenant}/like/plateNumber", method = RequestMethod.POST)
    public List<TruckQuery> loadLikePlateNumber(@PathVariable Integer useTenant, @RequestBody TruckQuery truckQuery,
                                                @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        useTenant = useTenant == null ? 0 : useTenant;
        TruckQuery filter = new TruckQuery();
        filter.setTruckTenantId(loginEmployee.getTenantId());
        filter.setPlateNumber(truckQuery.getPlateNumber());
        //当前承运商下
        filter.setVendorId(truckQuery.getVendorId());
        //未关联司机
        filter.setHaveDriver(truckQuery.getHaveDriver());

        return truckService.listByPlateNumber(filter, 15, useTenant == 1 ? loginEmployee : null);
    }

    @ApiOperation(value = "车牌号和车架号搜索车辆")
    @ApiImplicitParams({
        @ApiImplicitParam(value = "plateNumber", name = "车牌号"),
        @ApiImplicitParam(value = "truckIdentificationNo", name = "车架号")
    })
    @ResponseBody
    @RequestMapping(value = "search/findTruckBy", method = RequestMethod.POST)
    public Truck findTruckBy(@RequestBody Truck truck, @ApiParam(hidden = true) LoginEmployee loginEmployee) {

        List<Truck> trucks = truckService.findTruckBy(truck);
        if(trucks == null || trucks.isEmpty()){
            return null;
        }
        return trucks.get(0);
    }

    @ApiOperation(value = "更换司机前校验")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "truckId", value = "车辆id"),
        @ApiImplicitParam(name = "driverId", value = "司机id"),
        @ApiImplicitParam(name = "vendorId", value = "承运商id"),
    })
    @ResponseBody
    @RequestMapping(value = "updateDriver/check", method = RequestMethod.POST)
    public String checkUpdateDriver(@RequestBody TruckBo truckBo, @ApiParam(hidden = true)LoginEmployee loginEmployee) {
        return truckService.checkDriverBindTruck(truckBo, loginEmployee);
    }
}
