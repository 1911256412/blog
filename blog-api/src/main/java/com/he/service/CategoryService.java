package com.he.service;

import com.he.entity.Result;
import com.he.entity.vo.CategoryVo;


public interface CategoryService {

    CategoryVo findCategoryById(String id);

    Result findAll();

    Result findDetail();

    Result categoriesDetailById(String id);
}