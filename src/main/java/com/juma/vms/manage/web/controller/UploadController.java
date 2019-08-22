/**
 * 
 */
package com.juma.vms.manage.web.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.juma.auth.employee.domain.LoginEmployee;
import com.juma.common.storage.service.DistributedFileStorageService;

/**
 * 
 * @ClassName UploadController.java
 * 上传公共类
 * @author Libin.Wei
 * @Date 2018年11月6日 下午6:49:24
 * @version 1.0.0
 * @Copyright 2016 www.jumapeisong.com Inc. All rights reserved.
 */
@Controller
public class UploadController {

    @Autowired
    private DistributedFileStorageService distributedFileStorageService;

    @RequestMapping(value = "upload")
    @ResponseBody
    public String upload(@RequestParam MultipartFile uploadPic, LoginEmployee loginEmployee) throws IOException {
        return this.distributedFileStorageService.putInputBytes("upload/images", uploadPic.getOriginalFilename(),
                uploadPic.getBytes(), uploadPic.getContentType(), true);
    }
}
