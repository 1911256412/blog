package com.he.controller;

import com.he.annotation.LogAnnotation;
import com.he.cache.Cache;
import com.he.entity.Article;
import com.he.entity.ArticleParam;
import com.he.entity.Result;
import com.he.entity.vo.ArticleVo;
import com.he.entity.vo.PageParams;
import com.he.service.ArticleService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("articles")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @LogAnnotation(module="文章",operater="获取文章列表")
    @PostMapping
    //五分钟缓存
    @Cache(expire = 5*60*1000,name = "Articleslist")
    public Result listArticles(@RequestBody(required = false) PageParams pageParams){

        List<ArticleVo> articleList=articleService.PageList(pageParams);
        articleList.stream().forEach(item->{
            System.out.println(item);
        });
        return Result.success(articleList);
    }

    @PostMapping("hot")
    @Cache(expire = 5*60*1000,name = "hot")
    public Result hotArticles(){
        int limit =5 ;
        return articleService.selectHot(limit);
    }
    //最新文章
    @PostMapping("new")
    public Result NewArticles(){
        int limit =3 ;
        return articleService.selectNew(limit );
    }
    @PostMapping("listArchives")
    public Result listArchives(){
        return articleService.listArch();

    }
    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long id) {
        System.out.println("**********id"+id);

        ArticleVo articleVo = articleService.findArticleById(id+"");

        return Result.success(articleVo);
    }
    @RequestMapping("publish")
    public Result publish (@RequestBody ArticleParam articleParam){

        return articleService.publish(articleParam);
    }


}
