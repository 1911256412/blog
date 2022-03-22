package com.he.controller;

import com.he.entity.Result;
import com.he.utils.QiniuUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.UUID;

import static java.util.UUID.randomUUID;

@RestController
@RequestMapping("upload")
public class uploadController {

    @Resource
    private QiniuUtils qiniuUtils;
    @PostMapping
    public Result upload(@RequestParam("image") MultipartFile file) {
        String original = file.getOriginalFilename();
        //得到文件后缀名
        String suffix = StringUtils.substringAfterLast(original, ".");
        String filename = UUID.randomUUID().toString() + "." + suffix;
        boolean upload = qiniuUtils.upload(file, filename);
        if(upload){
            return Result.success(QiniuUtils.url + filename);
        }
        return Result.fail(20001,"上传失败");

    }
}
