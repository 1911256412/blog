package com.he.service;

import com.he.entity.CommentParam;
import com.he.entity.Result;

public interface CommitService {
    //根据文章id查询所有评论
    Result selectCommit(String id);

    Result comment(CommentParam commentParam);
}
