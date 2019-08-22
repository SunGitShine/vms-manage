package com.juma.vms.manage.transport.controller;

import java.util.List;

import javax.annotation.Resource;

import com.juma.auth.user.domain.LoginUser;
import com.juma.vms.mq.domain.WorkFlowInstance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.giants.common.exception.BusinessException;
import com.giants.common.tools.Page;
import com.juma.auth.employee.domain.LoginEmployee;
import com.juma.auth.tenant.domain.Tenant;
import com.juma.vms.common.query.QueryCond;
import com.juma.vms.manage.web.controller.BaseController;
import com.juma.vms.transport.request.AddTransportSendReq;
import com.juma.vms.transport.request.SelectVehicleReq;
import com.juma.vms.transport.request.TransportSendPageReq;
import com.juma.vms.transport.request.TransportWorkflowTask;
import com.juma.vms.transport.response.TransportSendDetailResp;
import com.juma.vms.transport.response.TransportSendPageResp;
import com.juma.vms.transport.response.TransportVehicleDetail;
import com.juma.vms.transport.service.TransportSendService;
import com.juma.workflow.core.domain.TaskDetail;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @description: ${description}
 *
 * @author: xieqiang
 *
 * @create: 2019-03-26 14:20
 **/

@Api(value = "运力输送")
@Controller
@RequestMapping("transportSend")
public class TransportSendController extends BaseController{

	private final static String REAPPLY_OK = "REAPPLY_OK";
	private final static String REAPPLY_DIS = "REAPPLY_DIS";

	@Resource
	private TransportSendService transportSendService;

	@ApiOperation(value = "分页列表")
	@ResponseBody
	@RequestMapping(value = "search", method = RequestMethod.POST)
	public Page<TransportSendPageResp> search(@RequestBody QueryCond<TransportSendPageReq> queryCond, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
		return transportSendService.findTransportSendPage(queryCond, loginEmployee);
	}

	@ApiOperation(value = "选择车辆列表")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "truckIdentificationNo", name = "车架号"),
		@ApiImplicitParam(value = "fromDepartmentCode", name = "输送公司id"),
		@ApiImplicitParam(value = "receiveTenantId", name = "接收公司租户id"),
	})
	@ResponseBody
	@RequestMapping(value = "selectVehicle", method = RequestMethod.POST)
	public List<TransportVehicleDetail> selectVehicle(@RequestBody SelectVehicleReq selectVehicleReq, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
		return transportSendService.selectVehicleList(selectVehicleReq, loginEmployee);
	}

	@ApiOperation(value = "选择车辆详情")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "truckIdentificationNo", name = "车架号"),
		@ApiImplicitParam(value = "fromDepartmentCode", name = "输送公司id"),
		@ApiImplicitParam(value = "receiveTenantId", name = "接收公司租户id"),
	})
	@ResponseBody
	@RequestMapping(value = "selectVehicleDetail", method = RequestMethod.POST)
	public TransportVehicleDetail selectVehicleDetail(@RequestBody SelectVehicleReq selectVehicleReq, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
		return transportSendService.selectVehicleDetail(selectVehicleReq, loginEmployee);
	}

	@ApiOperation(value = "添加输送单")
	@ResponseBody
	@RequestMapping(value = "addTransportSend", method = RequestMethod.POST)
	public void addTransportSend(@RequestBody AddTransportSendReq addTransportSendReq, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
		transportSendService.addTransportSend(addTransportSendReq, loginEmployee);
	}

	@ApiOperation(value = "查询输送单详情")
	@ResponseBody
	@RequestMapping(value = "findDetail", method = RequestMethod.GET)
	public TransportSendDetailResp findDetail(@RequestParam Integer transportId, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
		return transportSendService.findTransportSendDetail(transportId, loginEmployee);
	}

	@ApiOperation(value = "更新输送单")
	@ResponseBody
	@RequestMapping(value = "updateTransportSend", method = RequestMethod.POST)
	public void updateTransportSend(@RequestBody AddTransportSendReq addTransportSendReq, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
		transportSendService.updateTransportSend(addTransportSendReq, loginEmployee);
	}

	@ApiOperation(value = "通过ProcessInstanceId查询工作流对象")
	@ResponseBody
	@RequestMapping(value = "findTaskByProcessInstanceId", method = RequestMethod.GET)
	public TaskDetail findTaskByProcessInstanceId(@RequestParam String processInstanceId, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
		return transportSendService.findTaskByProcessInstanceId(processInstanceId, loginEmployee);
	}

	@ApiOperation(value = "撤销工作流")
	@ResponseBody
	@RequestMapping(value = "cancelWorkFlowTask", method = RequestMethod.GET)
	public void cancelWorkFlowTask(@RequestParam Integer transportId, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
		transportSendService.cancelWorkFlowTask(transportId, loginEmployee);
	}

	@ApiOperation(value = "审核审批流程")
	@ResponseBody
	@RequestMapping(value = "doWorkFlowTask", method = RequestMethod.POST)
	public void doWorkFlowTask(@RequestBody TransportWorkflowTask task, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
//		transportSendService.doWorkFlowTask(task, loginEmployee);
	}

	/**
	 * 流程相关API -任务
	 */
	@ApiOperation(value = "通过taskId查询任务详情")
	@RequestMapping(value="/task",method=RequestMethod.GET)
	@ResponseBody
	public TaskDetail task(@RequestParam(required=false)String taskId, LoginEmployee loginEmployee) {
		TaskDetail taskDetail = new TaskDetail();
		if(taskId != null) {
			try {
				taskDetail = transportSendService.getWorkflowElement(taskId, loginEmployee);
			} catch (Exception e) {
				throw new BusinessException("workflow.error", "workflow.error");
			}
		}
		return taskDetail;
	}

	@ApiOperation(value = "查询租户列表")
	@ResponseBody
	@RequestMapping(value = "findAllTenant", method = RequestMethod.GET)
	public List<Tenant> findAllTenant(@ApiParam(hidden = true) LoginEmployee loginEmployee) {
		return transportSendService.findAllTenant();
	}

	@ApiOperation(value = "查询可选租户列表")
	@ResponseBody
	@RequestMapping(value = "findTransportTenantList", method = RequestMethod.GET)
	public List<Tenant> findTransportTenantList(@ApiParam(hidden = true) LoginEmployee loginEmployee) {
		return transportSendService.findTransportTenantList();
	}

	@ApiOperation(value = "通知工作流", notes = "通知工作流")
	@ResponseBody
	@RequestMapping(value = "sendToWorkflow", method = RequestMethod.POST)
	public void sendToWorkflow(@RequestBody WorkFlowInstance workFlowInstance, LoginEmployee loginEmployee) {
		if(REAPPLY_OK.equals(workFlowInstance.getApprovalKey())){
			transportSendService.reapplyToWorkflow(workFlowInstance, loginEmployee);
		}else if(REAPPLY_DIS.equals(workFlowInstance.getApprovalKey())){
			transportSendService.giveUpToWorkflow(workFlowInstance, loginEmployee);
		}
	}

	@ResponseBody
	@RequestMapping(value = "handleTransportSendAudit",method = RequestMethod.GET)
	public void handleTransportSendAudit(@RequestParam Integer transportId, LoginUser loginUser){
		transportSendService.handleTransportSendAudit(transportId, loginUser);
	}
}
