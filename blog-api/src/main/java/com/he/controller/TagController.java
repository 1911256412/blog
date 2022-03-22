package com.he.controller;

import com.he.entity.Result;
import com.he.entity.Tag;
import com.he.service.TagService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {
    @Resource
    private TagService tagService;

    @GetMapping("/hot")
    public Result hot() {
        List<Tag> tagList = tagService.selectHotTag();

        return Result.success(tagList);
    }

    @GetMapping
    public Result findAll(){
        return tagService.findAll();
    }

    @GetMapping("detail")
    public Result findAllDetail(){
        return tagService.findAllDetail();
    }
    @GetMapping("detail/{id}")
    public Result findAllDetailByid(@PathVariable Long id ){
        return tagService.findAllDetailById(id);
    }
}
