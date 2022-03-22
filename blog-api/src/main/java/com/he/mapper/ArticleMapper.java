package com.he.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.he.entity.Archives;
import com.he.entity.Article;

import java.util.List;


public interface ArticleMapper extends BaseMapper<Article> {
    List<Archives> listArch();

    IPage<Article> selectListPage(Page<Article> page,
                                  String month, String year, Long tagId, Long categoryId);
}
