package com.juma.vms.manage.web.controller;

import com.juma.auth.user.domain.LoginUser;
import com.juma.vms.tool.service.AuthCommonService;
import com.juma.vms.vendor.domain.Vendor;
import com.juma.vms.vendor.domain.VendorTenant;
import com.juma.vms.vendor.service.VendorService;
import com.juma.vms.vendor.service.VendorTenantService;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Preconditions;


@Controller
public class TestController {

    @Resource
    private AuthCommonService authCommonService;
    @Resource
    private VendorService vendorService;
    @Resource
    private VendorTenantService vendorTenantService;

    @RequestMapping(value = "test")
    @ResponseBody
    public void genericParam(@RequestBody QueryCond<User> cond) {

        String s = null;
        
       // Preconditions.checkArgument("name".equals("aa"), "不相等");
        
        Preconditions.checkArgument(s != null, "s 为 null");
        
        System.out.println(cond);
    }
    
    public static void main(String[] args) {
       /* User u = new User();
        u.setName("q");
        Validator validator =  Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<User>> set = validator.validate(u,Default.class);
        for (ConstraintViolation<User> constraintViolation : set) {
            System.out.println(constraintViolation);
        }*/

    }


    //
    @ResponseBody
    @RequestMapping(value = "vendorAuthorizationEcoUser", method = RequestMethod.GET)
    public void vendorAuthorizationEcoUser(Integer vendorId, Integer tenantId, String checkCode) {
        if (null == vendorId || null == tenantId || StringUtils.isBlank(checkCode)) {
            return;
        }

        if (!checkCode.equals("2342354")) {
            return;
        }

        Vendor vendor = vendorService.getVendor(vendorId);
        if (null == vendor) {
            return;
        }

        VendorTenant vendorTenant = vendorTenantService.loadByVendorId(vendorId, new LoginUser(tenantId, 1));
        if (null == vendorTenant) {
            return;
        }

        authCommonService.vendor2User(vendor, vendorTenant, new LoginUser(tenantId, vendor.getCreateUserId()));
    }
    
}
