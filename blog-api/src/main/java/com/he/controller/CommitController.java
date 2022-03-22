package com.he.controller;

import com.he.entity.CommentParam;
import com.he.entity.Result;
import com.he.service.CommitService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("comments")
public class CommitController {


    @Resource
    private CommitService commitService;

    //通过文章id查询文章评论
    @GetMapping("article/{id}")
    public Result commit( @PathVariable  String id ){


            return commitService.selectCommit(id);

    }


    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        return commitService.comment(commentParam);
    }
}
