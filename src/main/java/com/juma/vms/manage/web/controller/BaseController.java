package com.juma.vms.manage.web.controller;

import com.giants.common.exception.BusinessException;
import com.giants.common.tools.PageCondition;
import com.juma.auth.tenant.service.TenantService;
import com.juma.vms.operateLog.service.OperateLogService;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName BaseController.java
 * @Description 请填写注释...
 * @author Libin.Wei
 * @Date 2018年11月2日 下午5:25:09
 * @version 1.0.0
 * @Copyright 2016 www.jumapeisong.com Inc. All rights reserved.
 */

public class BaseController {

    @Resource
    private OperateLogService operateLogService;
    @Resource
    private TenantService tenantService;

    /**
     * 转换pagecondition中areaCodeList字符串到List对象
     *
     * @param pageCondition
     */
    protected void formatAreaCodeToList(PageCondition pageCondition, boolean removeAreaCode00) {
        if (pageCondition == null) {
            return;
        }

        Map<String, Object> filter = pageCondition.getFilters();

        this.checkDateNaN(filter);
        pageCondition.setFilters(handleAreaCode(filter, removeAreaCode00));
    }

    // 校验时间是否正确
    private void checkDateNaN(Map<String, Object> filter) {
        if (null == filter) {
            return;
        }

        if ((null == filter.get("startTime") && null != filter.get("endTime"))
                || (null != filter.get("startTime") && null == filter.get("endTime"))) {
            throw new BusinessException("startTimeAndEndTimeCoexistence", "errors.startTimeAndEndTimeCoexistence");
        }

        if (null == filter.get("startTime") && null == filter.get("endTime")) {
            return;
        }

        String startTime = filter.get("startTime").toString();
        String endTime = filter.get("endTime").toString();
        if (startTime.contains("NaN") || startTime.contains("aN") || endTime.contains("NaN")
                || endTime.contains("aN")) {
            throw new BusinessException("timeParseException", "errors.timeParseException");
        }
    }

    protected Map<String, Object> handleAreaCode(Map<String, Object> filter, boolean removeAreaCode00) {
        if (filter == null) {
            filter = new HashMap<String, Object>();
            List<String> target = new ArrayList<String>();
            target.add("-999");
            filter.put("areaCodeList", target);
            return filter;
        }

        if (filter.get("areaCodeList") == null) {
            List<String> target = new ArrayList<String>();
            target.add("-999");
            filter.put("areaCodeList", target);
            return filter;
        }

        String str = filter.get("areaCodeList").toString();
        List<String> target = this.splitStringByComma(str);
        if (target == null) {
            target = new ArrayList<String>();
            target.add("-999");
            filter.put("areaCodeList", target);
            return filter;
        }

        if (!removeAreaCode00) {
            filter.put("areaCodeList", target);
            return filter;
        }

        // target是Arrays.asList生成的，不能使用remove方法
        List<String> arrList = new ArrayList<String>(target);
        // 去掉业务范围全国00
        if (arrList.contains("00")) {
            arrList.remove("00");
        }

        // 若业务范围只包含全国，则不使用业务范围条件
        if (arrList.isEmpty()) {
            filter.remove("areaCodeList");
        } else {
            filter.put("areaCodeList", arrList);
        }

        return filter;
    }

    /**
     * 逗号分隔的字符串转String List
     *
     * @param targetStr
     * @return
     */
    protected List<String> splitStringByComma(String targetStr) {

        if (StringUtils.isBlank(targetStr)) {
            return null;
        }

        return Arrays.asList(StringUtils.split(targetStr, ","));

    }
}
