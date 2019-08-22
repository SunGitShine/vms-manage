package com.juma.vms.manage.transport.controller;

import com.giants.common.tools.Page;
import com.juma.auth.employee.domain.Department;
import com.juma.auth.employee.domain.LoginEmployee;
import com.juma.vms.common.query.QueryCond;
import com.juma.vms.transport.domain.TransportTruckRefund;
import com.juma.vms.transport.request.TransportTruckRefundPageReq;
import com.juma.vms.transport.request.TransportTruckRefundStorageReq;
import com.juma.vms.transport.request.TransportTruckRefundTaskReq;
import com.juma.vms.transport.request.TransportTruckRefundTruckReq;
import com.juma.vms.transport.response.ParentDepartment;
import com.juma.vms.transport.response.TransportReturnTruckInfoResp;
import com.juma.vms.transport.response.TransportTruckReturnResp;
import com.juma.vms.transport.service.TransportTruckReturnService;
import com.juma.workflow.core.domain.TaskDetail;
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
 * 功能 :
 * 退车单入口
 * @author : Bruce(刘正航) 14:05 2019-04-03
 */
@Api(value = "运力退车")
@Controller
@RequestMapping("transportRefund")
public class TransportTruckRefundController {

    @Autowired
    private TransportTruckReturnService transportTruckReturnService;

    /**查询退车单列表**/
    @ApiOperation(value = "退车单列表")
    @ResponseBody
    @RequestMapping(value = "search", method = RequestMethod.POST)
    public Page<TransportTruckReturnResp> search(
            @RequestBody @ApiParam(name = "request",value = "搜索关键词对象") QueryCond<TransportTruckRefundPageReq> request,
            @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        return transportTruckReturnService.findTransportTruckRefundPage(request,loginEmployee);
    }

    /**撤销退车单**/
    @ApiOperation(value = "撤销退车单")
    @ResponseBody
    @RequestMapping(value = "cancelTruckRefund", method = RequestMethod.GET)
    public void cancelTruckRefund(
            @ApiParam(name = "refundId",value = "退车单主键") Integer refundId,
            @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        transportTruckReturnService.cancelTruckRefund(refundId,loginEmployee);
    }

    /**根据车牌号,查询车辆列表**/
    @ApiOperation(value = "查询车辆列表")
    @ResponseBody
    @RequestMapping(value = "findRefundTruckInfos", method = RequestMethod.POST)
    public List<TransportReturnTruckInfoResp> findRefundTruckInfos(
            @RequestBody @ApiParam(name = "request",value = "搜索关键词对象") TransportTruckRefundTruckReq request,
            @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        return transportTruckReturnService.findRefundTruckInfos(request,loginEmployee);
    }

    /**添加退车单**/
    @ApiOperation(value = "添加退车单")
    @ResponseBody
    @RequestMapping(value = "addTransportTruckRefund", method = RequestMethod.POST)
    public void addTransportTruckRefund(
            @RequestBody @ApiParam(name = "refund",value = "退车单信息") TransportTruckRefund refund,
            @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        transportTruckReturnService.addTransportTruckRefund(refund,loginEmployee);
    }

    /**退车单详情**/
    @ApiOperation(value = "退车单详情")
    @ResponseBody
    @RequestMapping(value = "transportTruckRefundDetail", method = RequestMethod.GET)
    public TransportTruckReturnResp findDetailByRefundId(
            @ApiParam(name = "refundId",value = "退车单主键") Integer refundId,
            @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        return transportTruckReturnService.findDetailByRefundId(refundId,loginEmployee);
    }

    /**获取工作流信息**/
    @ApiOperation(value = "根据任务ID:获取工作流信息")
    @ResponseBody
    @RequestMapping(value = "task", method = RequestMethod.GET)
    public TaskDetail task(
            @ApiParam(name = "taskId",value = "任务ID") String taskId,
            @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        return transportTruckReturnService.getWorkflowElement(taskId,loginEmployee);
    }

    @ApiOperation(value = "审核审批流程")
    @ResponseBody
    @RequestMapping(value = "doWorkFlowTask", method = RequestMethod.POST)
    public void doWorkFlowTask(
            @RequestBody @ApiParam(name = "request",value = "任务审批参数") TransportTruckRefundTaskReq request,
            @ApiParam(hidden = true) LoginEmployee loginEmployee) {
//        transportTruckReturnService.doWorkFlowTask(request, loginEmployee);
    }

    @ApiOperation(value = "根据流程ID:获取工作流信息")
    @ResponseBody
    @RequestMapping(value = "findTaskByProcessInstanceId", method = RequestMethod.GET)
    public TaskDetail findTaskByProcessInstanceId(
            @ApiParam(name = "processInstanceId",value = "流程ID") String processInstanceId,
            @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        return transportTruckReturnService.findTaskByProcessInstanceId(processInstanceId, loginEmployee);
    }

    @ApiOperation(value = "退车入库")
    @ResponseBody
    @RequestMapping(value = "doRefundTruckStorage", method = RequestMethod.POST)
    public void doRefundTruckStorage(
            @RequestBody @ApiParam(name = "request",value = "任务审批参数") TransportTruckRefundStorageReq request,
            @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        transportTruckReturnService.doRefundTruckStorage(request, loginEmployee);
    }

    @ApiOperation(value = "根据部门ID:获取上级子公司信息")
    @ResponseBody
    @RequestMapping(value = "findSuperCompany", method = RequestMethod.GET)
    public Department findSuperCompany(
        @ApiParam(name = "departmentId",value = "部门id") Integer departmentId,
        @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        return transportTruckReturnService.findSuperCompany(departmentId);
    }

}
