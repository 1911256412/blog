package com.he.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.he.entity.Tag;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {
    List<Tag> findTagByArticleId(String  id);

    List<Long> selectHot(int limit);

    List<Tag> selectTagsHot(List<Long> tagIds);
}
