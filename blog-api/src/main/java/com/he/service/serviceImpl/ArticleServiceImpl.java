package com.he.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.he.entity.*;
import com.he.entity.vo.*;
import com.he.mapper.*;
import com.he.service.ArticleService;
import com.he.service.CategoryService;
import com.he.service.SysUserService;
import com.he.service.TagService;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private CategoryService categoryService;
    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private ArticleBodyMapper articleBodyMapper;

    @Resource
    private ArticleTagMapper articleTagMapper;

    @Resource
    private TagService tagService;


    @Resource
    private SysUserService sysUserService;


    public List<ArticleVo> listAritcles(PageParams pageParams) {
        QueryWrapper<Article> wrapper = new QueryWrapper();
        wrapper.orderByAsc("create_date");
        wrapper.orderByAsc("weight");
        //查询文章的参数 加上分类id，判断不为空 加上分类条件
        if (pageParams.getCategoryId() != null) {

            wrapper.eq("category_id", pageParams.getCategoryId());
        }
        //分页查询所有文章
        Page<Article> page = new Page(pageParams.getPage(), pageParams.getPageSize());
        Page<Article> selectPage = articleMapper.selectPage(page, wrapper);

        //得到所有记录数,把所有记录数传递到一个函数中目的是封装成Vo返回前端的对象
        List<ArticleVo> articleVoList = copyList(selectPage.getRecords());
        return articleVoList;
    }


    @Override
    public Result selectHot(int limit) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.select("id", "title");
        wrapper.last("limit" + limit);
        List<Article> articles = articleMapper.selectList(wrapper);
        return Result.success(articles);
    }

    @Override
    public Result selectNew(int limit) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_date");
        wrapper.last("limit " + limit);
        wrapper.select("id", "title");
        List<Article> articles = articleMapper.selectList(wrapper);
        return Result.success(articles);
    }

    @Override
    public Result listArch() {
        List<Archives> archivesList = articleMapper.listArch();
        return Result.success(archivesList);
    }

    @Override
    public ArticleVo findArticleById(String id) {

        Article article = articleMapper.selectById(id);
        ArticleVo articleVo=new ArticleVo();
        if(article!=null){
            articleVo= copy(article, true, true, true, true);
        }
        System.out.println("***********文章信息"+article);
        return articleVo;
    }

    @Override
    public Result publish(ArticleParam articleParam) {
        SysUser sysUser = UserThreadLoacl.get();
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setCategoryId(articleParam.getCategory().getId());
        article.setCreateDate(System.currentTimeMillis());
        articleMapper.insert(article);
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());
                articleTagMapper.insert(articleTag);
            }
        }
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);
        article.setBodyId(articleBody.getId());
        //修改表结构添加bodyId
        articleMapper.updateById(article);
        Map<String, Object> map = new HashMap<>();
        map.put("id", article.getId().toString());
        return Result.success(map);
    }

    @Override
    public List<ArticleVo> PageList(PageParams pageParams) {
        //实现自定义的分页
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());

        IPage<Article> iPage = articleMapper.selectListPage(page, pageParams.getYear(), pageParams.getMonth()
                , pageParams.getTagId(), pageParams.getCategoryId());
        List<Article> records = iPage.getRecords();

        List<ArticleVo> articleVoList = copyList(records);
       // List<ArticleVo> articleVoList = null;
        articleVoList.stream().forEach(item->{
            System.out.println("文章的值"+item);
        });
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, false, false));
        }
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, isBody, false));
        }
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, isBody, isCategory));
        }
        return articleVoList;
    }

    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        if (article != null) {
            BeanUtils.copyProperties(article, articleVo);
        }
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //并不是所有的接口 都需要标签 ，作者信息
        if (isTag) {
            String  articleId = article.getId();

            articleVo.setTags(tagService.findAllTag(articleId));
        }
        if (isAuthor) {
            String  authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findAuthor(authorId).getNickname());
        }
        if (isBody) {
            ArticleBodyVo articleBody = findArticleBody(article.getId());
            articleVo.setBody(articleBody);
        }
        if (isCategory) {
            System.out.println("**************文章分类id"+article.getCategoryId());
            CategoryVo categoryVo = findCategory(article.getCategoryId());
            System.out.println("***********文章分类"+categoryVo);

            articleVo.setCategoryVo(categoryVo);
            System.out.println("文章*********"+articleVo);
        }
        return articleVo;
    }


    private CategoryVo findCategory(String  categoryId) {
        return categoryService.findCategoryById(categoryId);
    }


    private ArticleBodyVo findArticleBody(String articleId) {
        LambdaQueryWrapper<ArticleBody> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleBody::getArticleId, articleId);
        ArticleBody articleBody = articleBodyMapper.selectOne(queryWrapper);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

    //定义要封装对象的函数,需要定义一个方法把Article对象转换成ArticleVo
    private List<ArticleVo> copyList(List<Article> records) {
        List<ArticleVo> list = new ArrayList<>();
        System.out.println("copyList文章的值"+list);
        for (Article record : records) {
            System.out.println("copyList文章的值"+record);
            ArticleVo articleVo = copy(record, true, true);
            list.add(articleVo);
        }
        return list;
    }

    //把Article对象转换为ArticleVo
    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        //定义日期格式
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm:SS"));
        //并不是所有的都有作者信息 ，都需要标签
        if (isTag) {
            //根据作者id查询所有的标签tag
            List<TagVo> tags = tagService.findAllTag(article.getId());
            articleVo.setTags(tags);
        }
        if (isAuthor) {
            SysUser sysUser = sysUserService.findAuthor(article.getAuthorId());
            articleVo.setAuthor(sysUser.getNickname());
        }
        System.out.println("最终传递过来的vo********"+articleVo);
        return articleVo;
    }
}
