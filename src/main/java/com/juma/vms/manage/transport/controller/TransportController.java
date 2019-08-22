package com.juma.vms.manage.transport.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.giants.common.tools.Page;
import com.juma.auth.employee.domain.LoginEmployee;
import com.juma.vms.common.query.QueryCond;
import com.juma.vms.driver.vo.DriverBo;
import com.juma.vms.manage.web.controller.BaseController;
import com.juma.vms.tool.domain.BaseEnumDomian;
import com.juma.vms.transport.request.AddExternalTransportReq;
import com.juma.vms.transport.request.AddVendorAndDriverReq;
import com.juma.vms.transport.request.TransportPageReq;
import com.juma.vms.transport.response.DriverBeCurrResp;
import com.juma.vms.transport.response.TransportDetailResp;
import com.juma.vms.transport.response.TransportPageResp;
import com.juma.vms.transport.service.TransportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @description: ${description}
 *
 * @author: xieqiang
 *
 * @create: 2019-03-22 14:43
 **/
@Api(value = "运力管理")
@Controller
@RequestMapping("transport")
public class TransportController extends BaseController{

	@Resource
	private TransportService transportService;

	@ApiOperation(value = "分页列表")
	@ResponseBody
	@RequestMapping(value = "search", method = RequestMethod.POST)
	public Page<TransportPageResp> search(@RequestBody QueryCond<TransportPageReq> capacityPoolQueryCond, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
		return transportService.findTransportByPage(capacityPoolQueryCond, loginEmployee);
	}

	@ApiOperation(value = "运力详情")
	@ResponseBody
	@RequestMapping(value = "detail", method = RequestMethod.GET)
	public TransportDetailResp detail(@RequestParam Integer capacityPoolId, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
		return transportService.findTransportDetail(capacityPoolId, loginEmployee);
	}

	@ApiOperation(value = "添加外请运力")
	@ResponseBody
	@RequestMapping(value = "addTransport", method = RequestMethod.POST)
	public void addTransport(@RequestBody AddExternalTransportReq addExternalTransportReq, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
		transportService.addExternalTransport(addExternalTransportReq, loginEmployee);
	}

	@ApiOperation(value = "添加承运商并绑定司机")
	@ResponseBody
	@RequestMapping(value = "addVendorBindDriver", method = RequestMethod.POST)
	public Map<String, Integer> addVendorBindDriver(@RequestBody AddVendorAndDriverReq addVendorAndDriverReq, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
		return transportService.addVendorAndBindDriver(addVendorAndDriverReq, loginEmployee);
	}

	@ApiOperation(value = "查询下拉属性")
	@ResponseBody
	@RequestMapping(value = "findPropertyValues", method = RequestMethod.POST)
	public Map<String, List<BaseEnumDomian>> findPropertyValues(@RequestBody List<String> keys, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
		return transportService.getPropertyValueByKeys(keys);
	}

	@ApiOperation(value = "添加司机")
	@ResponseBody
	@RequestMapping(value = "addDriver", method = RequestMethod.POST)
	public Integer addDriver(@RequestBody DriverBo driverBo, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
		return transportService.addDriver(driverBo, loginEmployee);
	}

	@ApiOperation(value = "通过身份证号查司机")
	@ResponseBody
	@RequestMapping(value = "loadDriverByIdCardNo", method = RequestMethod.POST)
	public DriverBeCurrResp loadDriverByIdCardNo(@RequestParam String idCordNo, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
		return transportService.loadDriverByIdCardNo(idCordNo, loginEmployee);
	}
}
