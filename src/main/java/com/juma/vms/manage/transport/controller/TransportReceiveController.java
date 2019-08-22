package com.juma.vms.manage.transport.controller;

import com.giants.common.tools.Page;
import com.juma.auth.employee.domain.LoginEmployee;
import com.juma.vms.common.query.QueryCond;
import com.juma.vms.manage.web.controller.BaseController;
import com.juma.vms.tool.domain.CrmCustomerInfo;
import com.juma.vms.tool.domain.DriveLicenseInfo;
import com.juma.vms.tool.domain.IdentificationCardInfo;
import com.juma.vms.tool.request.DriveLicenseReq;
import com.juma.vms.tool.request.IdentificationReq;
import com.juma.vms.tool.service.OcrCommonService;
import com.juma.vms.transport.request.CompleteTransportReq;
import com.juma.vms.transport.request.TransportCustomerReq;
import com.juma.vms.transport.request.TransportReceivePageReq;
import com.juma.vms.transport.response.*;
import com.juma.vms.transport.service.TransportReceiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @description: ${description}
 *
 * @author: xieqiang
 *
 * @create: 2019-03-22 14:56
 **/
@Api(value = "运力接收")
@Controller
@RequestMapping("transportReceive")
public class TransportReceiveController extends BaseController {

	@Autowired
	private OcrCommonService ocrCommonService;

	@Autowired
	private TransportReceiveService transportReceiveService;

	@ApiOperation(value = "客户列表", notes = "URL可以拼接useTenant，当useTenant=1时，需要租户")
	@ResponseBody
	@RequestMapping(value = "crmCustomers", method = RequestMethod.POST)
	public List<CrmCustomerInfo> customers(
			@RequestBody @ApiParam(name = "request",value = "搜索关键词对象") TransportCustomerReq request,
			Boolean useTenant,
			@ApiParam(hidden = true) LoginEmployee loginEmployee) {
		return transportReceiveService.findTransportCustomers(request, useTenant == null ? false : useTenant,loginEmployee);
	}

	@ApiOperation(value = "分页列表")
	@ResponseBody
	@RequestMapping(value = "search", method = RequestMethod.POST)
	public Page<TransportReceivePageResp> search(
			@RequestBody @ApiParam(name = "request",value = "搜索关键词对象") QueryCond<TransportReceivePageReq> queryCond,
			@ApiParam(hidden = true) LoginEmployee loginEmployee) {
		return transportReceiveService.findTransportReceivePage(queryCond, loginEmployee);
	}

	@ApiOperation(value = "输送单详情")
	@ResponseBody
	@RequestMapping(value = "capacityItemDetail", method = RequestMethod.GET)
	public TransportReceiveDetailResp detail(
			@ApiParam(name = "itemId",value = "列表详情主键ID") Integer itemId,
			@ApiParam(hidden = true) LoginEmployee loginEmployee) {
		return transportReceiveService.findTransportReceiveDetail(itemId, loginEmployee);
	}

	@ApiOperation(value = "接收运力")
	@ResponseBody
	@RequestMapping(value = "receiveTransport", method = RequestMethod.GET)
	public TransportReceiveResp receiveTransportInfo(
			@ApiParam(name = "itemId",value = "列表详情主键ID") Integer itemId,
			@ApiParam(hidden = true) LoginEmployee loginEmployee) {
		return transportReceiveService.receiveTransportInfo(itemId, loginEmployee);
	}

	@ApiOperation(value = "完善运力")
	@ResponseBody
	@RequestMapping(value = "completeTransport", method = RequestMethod.POST)
	public void completeTransport(
			@RequestBody @ApiParam(name = "completeTransportReq",value = "完善运力必传信息") CompleteTransportReq completeTransportReq,
			@ApiParam(hidden = true) LoginEmployee loginEmployee) {
		transportReceiveService.completeTransport(completeTransportReq, loginEmployee);
	}

	@ApiOperation(value = "添加承运商前置信息获取")
	@ResponseBody
	@RequestMapping(value = "beforeAddVendor", method = RequestMethod.GET)
	public TransportBeforeAddVendorResp beforeAddVendor(
			@ApiParam(name = "truckIdentificationNo",value = "车架号") String truckIdentificationNo,
			@ApiParam(hidden = true) LoginEmployee loginEmployee) {
		return transportReceiveService.beforeAddVendor(truckIdentificationNo,loginEmployee);
	}

	@ApiOperation(value = "驾驶证识别")
	@ResponseBody
	@RequestMapping(value = "ocrDriveLicence", method = RequestMethod.POST)
	public DriveLicenseInfo ocrDriveLicence(
			@RequestBody @ApiParam(name = "request",value = "OCR请求对象") DriveLicenseReq request,
			@ApiParam(hidden = true) LoginEmployee loginEmployee) {
		return ocrCommonService.ocrDriveLicenseInfo(request,loginEmployee);
	}

	@ApiOperation(value = "身份证识别")
	@ResponseBody
	@RequestMapping(value = "ocrIdentification", method = RequestMethod.POST)
	public IdentificationCardInfo ocrIdentification(
			@RequestBody @ApiParam(name = "request",value = "OCR请求对象") IdentificationReq request,
			@ApiParam(hidden = true) LoginEmployee loginEmployee) {
		return ocrCommonService.ocrIdentificationInfo(request,loginEmployee);
	}

}
