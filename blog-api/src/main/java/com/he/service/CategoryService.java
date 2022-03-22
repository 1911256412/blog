package com.he.service;

import com.he.entity.Result;
import com.he.entity.vo.CategoryVo;


public interface CategoryService {

    CategoryVo findCategoryById(Long id);

    Result findAll();

    Result findDetail();

    Result categoriesDetailById(Long id);
}