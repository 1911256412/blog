package com.he.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.he.entity.Permission;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface PermissionMapper extends BaseMapper<Permission> {

    @Select("SELECT * FROM permission WHERE id IN (SELECT permission_id FROM admin_permission WHERE admin_id=#{id})")
    List<Permission> selectPermission(Long id);
}