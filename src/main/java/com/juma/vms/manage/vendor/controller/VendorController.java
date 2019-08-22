package com.juma.vms.manage.vendor.controller;

import com.giants.common.exception.BusinessException;
import com.giants.common.tools.Page;
import com.juma.auth.employee.domain.LoginEmployee;
import com.juma.auth.user.domain.LoginUser;
import com.juma.vms.common.query.QueryCond;
import com.juma.vms.manage.vendor.controller.vo.Certification;
import com.juma.vms.mq.service.MqService;
import com.juma.vms.tool.service.MdcCommonService;
import com.juma.vms.transport.request.CapacityPoolFilter;
import com.juma.vms.transport.response.CapacityPoolQuery;
import com.juma.vms.transport.service.CapacityPoolService;
import com.juma.vms.vendor.domain.Vendor;
import com.juma.vms.vendor.domain.VendorDriver;
import com.juma.vms.vendor.service.VendorDriverService;
import com.juma.vms.vendor.service.VendorService;
import com.juma.vms.vendor.vo.*;
import io.swagger.annotations.*;
import me.about.poi.reader.XlsxReader;
import me.about.poi.writer.XssfWriter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author Libin.Wei
 * @version 1.0.0
 * @ClassName VendorController.java
 * @Description 承运商管理
 * @Date 2018年11月1日 下午8:39:21
 * @Copyright 2016 www.jumapeisong.com Inc. All rights reserved.
 */

@Api(value = "Vendor-Controller")
@Controller
@RequestMapping("vendor")
public class VendorController {

    @Resource
    private VendorService vendorService;
    @Resource
    private VendorDriverService vendorDriverService;
    @Resource
    private CapacityPoolService capacityPoolService;
    @Resource
    private MqService mqService;

    @Resource
    private MdcCommonService mdcCommonService;

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public void upload(@RequestParam MultipartFile uploadFile, HttpServletResponse httpServletResponse)
            throws Exception {
        try {
            LoginUser loginUser = new LoginUser();
            loginUser.setUserId(1);
            List<Certification> rows = XlsxReader.fromInputStream(uploadFile.getInputStream(), Certification.class, 1);

            for (Certification idCard : rows) {
                loginUser.setTenantId(idCard.getTenantId());
                VendorIdentification vendorIdentification = new VendorIdentification();
                vendorIdentification.setIdCardNo(idCard.getIdCardNo());
                vendorIdentification.setEnterpriseCode(idCard.getIdCardNo());
                vendorIdentification.setVendorId(idCard.getVendorId());
                if (StringUtils.isBlank(idCard.getVendorType())) continue;
                String vendorType = idCard.getVendorType().trim();
                vendorIdentification.setVendorType(vendorType.equals("个人") ? 1
                        : (vendorType.equals("车队") ? 2 : (vendorType.equals("公司") ? 3 : 0)));
                vendorIdentification.setVendorName(idCard.getVendorName());
                String jumaPin = null;
                try {
                    jumaPin = mdcCommonService.addVendorToMdata(vendorIdentification, loginUser);
                } catch (Exception e) {
                    jumaPin = "认证不通过：" + e.getMessage();
                }
                idCard.setResult(jumaPin);
            }
            httpServletResponse.setContentType("multipart/form-data");
            httpServletResponse.setHeader("Content-disposition", "attachment; filename=vendor.xlsx");
            new XssfWriter().appendToSheet("承运商验证结果", rows).writeToOutputStream(httpServletResponse.getOutputStream());
        } catch (Exception e) {
            throw new BusinessException("error", e.getMessage());
        }

    }

    @ApiOperation(value = "分页列表")
    @ResponseBody
    @RequestMapping(value = "search", method = RequestMethod.POST)
    public Page<VendorQuery> search(@RequestBody QueryCond<VendorFilter> queryCond, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        // 处理承运商名称、联系人、联系电话、证件号条件
        VendorFilter filters = queryCond.getFilters();
        if (null != filters) {
            Set<Integer> param = new HashSet<>();
            if (null != filters.getVendorId()) {
                param.add(filters.getVendorId());
            }

            if (null != filters.getContactUserNameToVendorId()) {
                param.add(filters.getContactUserNameToVendorId());
            }

            if (null != filters.getEmergencyContactPhoneToVendorId()) {
                param.add(filters.getEmergencyContactPhoneToVendorId());
            }

            if (null != filters.getCredentialNoToVendorId()) {
                param.add(filters.getCredentialNoToVendorId());
            }

            // 有两个或以上的不同的vendorId，不能返回数据
            if (param.size() > 1) {
                filters.setVendorId(-1);
            }

            if (param.size() == 1) {
                List<Integer> list = new ArrayList<>(param);
                filters.setVendorId(list.get(0));
            }
        }

        return vendorService.search(queryCond, loginEmployee);

    }

    @ApiOperation(value = "承运商详情")
    @ResponseBody
    @RequestMapping(value = "{vendorId}/detail", method = RequestMethod.GET)
    public VendorQuery getVendor(@PathVariable Integer vendorId, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        return vendorService.getVendorDetail(vendorId, loginEmployee);
    }

    @ApiOperation(value = "编辑承运商")
    @ResponseBody
    @RequestMapping(value = "modify", method = RequestMethod.POST)
    public Integer modify(@RequestBody VendorBo vendorBo, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        vendorService.update(vendorBo, loginEmployee);
        mqService.sendVendorAfterUpdate(vendorBo.getVendor().getVendorId(), loginEmployee.getTenantId());
        return vendorBo.getVendor().getVendorId();
    }

    @ApiOperation(value = "根据承运商名称或电话单独模糊匹配承运商列表", notes = "URL中的参数useTenant=0,全局查询；useTenant=1,当前登录人租户下查询, callbackPageSize为返回条数，默认15")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "vendorName", name = "承运商名称"),
            @ApiImplicitParam(value = "contactUserName", name = "联系人姓名"),
            @ApiImplicitParam(value = "contactPhone", name = "承运商联系人电话"),
            @ApiImplicitParam(value = "idCardNo", name = "身份证号"),
            @ApiImplicitParam(value = "credentialNo", name = "证件号：身份证号或信用代码"),
            @ApiImplicitParam(value = "vendorSource", name = "承运商运营类型，1：自营；2：非自营")
    })
    @ResponseBody
    @RequestMapping(value = "load/{useTenant}/like/vendorName", method = RequestMethod.POST)
    public List<VendorQuery> loadLikeVendorName(@PathVariable Integer useTenant, @RequestBody VendorFilter vendorFilter, Integer callbackPageSize,
                                                @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        useTenant = useTenant == null ? 0 : useTenant;
        // 重新声明，只接收vendorNmae和phone、idCardNo
        VendorFilter filter = new VendorFilter();
        filter.setIsDelete((byte) 0);
        filter.setVendorName(vendorFilter.getVendorName());
        filter.setContactPhone(vendorFilter.getContactPhone());
        filter.setContactUserName(vendorFilter.getContactUserName());
        filter.setIdCardNo(vendorFilter.getIdCardNo());
        filter.setCredentialNo(vendorFilter.getCredentialNo());
        filter.setVendorSource(vendorFilter.getVendorSource());

        return vendorService.listByVendorFilter(filter, callbackPageSize == null ? 15 : callbackPageSize, useTenant == 1 ? loginEmployee : null);
    }

    @ApiOperation(value = "根据承运商名称或手机号模糊匹配承运商列表", notes = "URL中的参数useTenant=0,全局查询；useTenant=1,当前登录人租户下查询, callbackPageSize为返回条数，默认15")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "vendorName", name = "承运商名称或电话"),
            @ApiImplicitParam(value = "vendorSource", name = "承运商运营类型，1：自营；2：非自营"),
            @ApiImplicitParam(value = "areaCodeList", name = "业务区域集合")
    })
    @ResponseBody
    @RequestMapping(value = "load/{useTenant}/like/vendorNameOrPhone", method = RequestMethod.POST)
    public List<VendorQuery> loadLikeVendorNameOrPhone(@PathVariable Integer useTenant, @RequestBody VendorFilter vendorFilter, Integer callbackPageSize,
                                                @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        useTenant = useTenant == null ? 0 : useTenant;
        // 重新声明，只接收vendorNameOrPhone
        VendorFilter filter = new VendorFilter();
        filter.setIsDelete((byte) 0);
        filter.setVendorNameOrPhone(vendorFilter.getVendorName());
        filter.setVendorSource(vendorFilter.getVendorSource());
        filter.setVendorName(null);
        filter.setIsEnable(vendorFilter.getIsEnable());
        if (CollectionUtils.isNotEmpty(vendorFilter.getAreaCodeList())) {
            filter.setAreaCodeList(vendorFilter.getAreaCodeList());
        } else {
            List<LoginEmployee.LoginDepartment.BusinessArea> businessAreas = loginEmployee.getLoginDepartment().getBusinessAreas();
            if (CollectionUtils.isNotEmpty(businessAreas)) {
                for (LoginEmployee.LoginDepartment.BusinessArea businessArea: businessAreas) {
                    filter.getAreaCodeList().add(businessArea.getAreaCode()) ;
                }
            }
        }

        // 如果业务取值有且只有一个00数值，排除
        if(CollectionUtils.isNotEmpty(vendorFilter.getAreaCodeList())
                && filter.getAreaCodeList().size() == 1 && filter.getAreaCodeList().get(0).equals("00")) {
            filter.getAreaCodeList().remove(0);
        }

        return vendorService.listByVendorFilter(filter, callbackPageSize == null ? 15 : callbackPageSize, useTenant == 1 ? loginEmployee : null);
    }

    @ApiOperation(value = "承运商关联司机分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "vendorId", name = "承运商ID", required = true)
    })
    @ResponseBody
    @RequestMapping(value = "driver/search", method = RequestMethod.POST)
    public Page<VendorDriverQuery> driverSearch(@RequestBody QueryCond<VendorDriver> queryCond, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        return vendorDriverService.search(queryCond,loginEmployee);
    }

    @ApiOperation(value = "承运商关联车辆分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "vendorId", name = "承运商ID", required = true)
    })
    @ResponseBody
    @RequestMapping(value = "truck/search", method = RequestMethod.POST)
    public Page<CapacityPoolQuery> truckSearch(@RequestBody QueryCond<CapacityPoolFilter> queryCond, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        return capacityPoolService.searchByVendor(queryCond, loginEmployee);
    }

    @ApiOperation(value = "承运商关联车辆统计有效运力")
    @ApiImplicitParam(paramType = "path", value = "vendorId", name = "承运商ID", required = true)
    @ResponseBody
    @RequestMapping(value = "{vendorId}/countValidCapacity", method = RequestMethod.POST)
    public Integer countValidCapacity(@PathVariable Integer vendorId, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        return capacityPoolService.countValidCapacity(vendorId, loginEmployee);
    }

    @ApiOperation(value = "身份证号查承运商")
    @ResponseBody
    @RequestMapping(value = "{idCardNo}/getVendorByIdCardNo", method = RequestMethod.GET)
    public Vendor getVendorByIdCardNo(@PathVariable String idCardNo, @ApiParam(hidden = true) LoginEmployee loginEmployee) {
        return vendorService.loadByIdCardNo(idCardNo, null);
    }
}
