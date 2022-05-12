package com.he.entity;

import com.he.entity.vo.CategoryVo;
import com.he.entity.vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class ArticleParam {

    private String id;

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;
}
