package com.he.service;


import com.he.entity.Article;
import com.he.entity.ArticleParam;
import com.he.entity.Result;
import com.he.entity.vo.ArticleVo;
import com.he.entity.vo.PageParams;

import java.util.List;

public interface ArticleService {
    List<ArticleVo> listAritcles(PageParams pageParams);

    Result selectHot(int limit);

    Result selectNew(int limit);

    Result listArch();

    ArticleVo findArticleById(String id);

    Result publish(ArticleParam articleParam);

    List<ArticleVo> PageList(PageParams pageParams);
}
