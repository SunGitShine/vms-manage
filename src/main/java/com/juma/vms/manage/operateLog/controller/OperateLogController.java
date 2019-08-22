package com.juma.vms.manage.operateLog.controller;

import com.giants.common.tools.Page;
import com.juma.auth.employee.domain.LoginEmployee;
import com.juma.vms.common.query.QueryCond;
import com.juma.vms.operateLog.domain.OperateLog;
import com.juma.vms.operateLog.service.OperateLogService;
import com.juma.vms.operateLog.vo.OperateLogFilter;
import com.juma.vms.operateLog.vo.OperateLogHistoryQuery;
import com.juma.vms.operateLog.vo.OperateLogQuery;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @ClassName OerateLogController.java
 * @Description 操作记录
 * @author Libin.Wei
 * @Date 2018年11月2日 上午9:47:41
 * @version 1.0.0
 * @Copyright 2016 www.jumapeisong.com Inc. All rights reserved.
 */

@Controller
@RequestMapping("operateLog")
public class OperateLogController {

    @Resource
    private OperateLogService operateLogService;

    @ApiOperation(value = "承运商操作日志")
    @ResponseBody
    @RequestMapping(value = "search", method = RequestMethod.POST)
    public Page<OperateLogQuery> search(@RequestBody QueryCond<OperateLogFilter> queryCond,
                                        @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        return operateLogService.search(queryCond, loginEmployee);
    }

    @ApiOperation(value = "承运商操作日志历史操作记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "relationTableId", value = "关联表I，例如承运商的主键vendorId", required = true)
    })
    @ResponseBody
    @RequestMapping(value = "history/search", method = RequestMethod.POST)
    public Page<OperateLogHistoryQuery> searchHistory(@RequestBody QueryCond<OperateLog> queryCond,
                                                      @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        return operateLogService.searchHistory(queryCond, loginEmployee);
    }
}
