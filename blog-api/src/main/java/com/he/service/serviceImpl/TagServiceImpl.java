package com.he.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.he.entity.Result;
import com.he.entity.Tag;
import com.he.entity.vo.TagVo;
import com.he.mapper.TagMapper;
import com.he.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Resource
    private TagMapper tagMapper;


    private List<TagVo> copyList(List<Tag> list) {
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : list) {
            TagVo tagVo = copy(tag);
            tagVoList.add(tagVo);
        }
        return tagVoList;
    }

    private TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag, tagVo);
        return tagVo;
    }

    @Override
    public List<TagVo> findAllTag(String  id) {
        List<Tag> tagByArticleId = tagMapper.findTagByArticleId(id);
        return copyList(tagByArticleId);
    }

    @Override
    public List<Tag> selectHotTag() {
        //查询Article_tag表  分组查询出最热门前五条数据 ，根据数量降序排序
        Integer limit = 5;
        List<Long> tagIds = tagMapper.selectHot(limit);
        //判断集合为不为空,如果为空直接返回
        if (CollectionUtils.isEmpty(tagIds)) {
            return Collections.emptyList();
        }
        //根据tagIds查询所有标签的名称
        List<Tag> tags = tagMapper.selectTagsHot(tagIds);
        return tags;
    }

    public Result findAll() {
        List<Tag> tags = this.tagMapper.selectList(new LambdaQueryWrapper<>());
        return Result.success(copyList(tags));
    }

    @Override
    public Result findAllDetail() {
        List<Tag> tags = this.tagMapper.selectList(new LambdaQueryWrapper<>());
        return Result.success(copyList(tags));
    }

    @Override
    public Result findAllDetailById(String id) {
        Tag tag = tagMapper.selectById(id);
        return Result.success(tag);
    }


}
