package com.he.service;

import com.he.entity.Result;
import com.he.entity.Tag;
import com.he.entity.vo.TagVo;

import java.util.List;

public interface  TagService {
    List<TagVo> findAllTag(Long id);

    List<Tag> selectHotTag();


    Result findAll();

    Result findAllDetail();


    Result findAllDetailById(Long id);
}
