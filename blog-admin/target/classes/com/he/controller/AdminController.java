package com.he.controller;

import com.he.entity.PageParam;
import com.he.entity.Permission;
import com.he.entity.Result;
import com.he.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private PermissionService permissionService;

    @PostMapping("permission/permissionList")
    public Result permissionList(@RequestBody PageParam pageParam){
        return permissionService.listPermission(pageParam);
    }

    @PostMapping("permission/add")
    public Result add(@RequestBody Permission permission){
        return permissionService.add(permission);
    }

    @PostMapping("permission/update")
    public Result update(@RequestBody Permission permission){
        return permissionService.update(permission);
    }

    @GetMapping("permission/delete/{id}")
    public Result delete(@PathVariable("id") Long id){
        return permissionService.delete(id);
    }
}