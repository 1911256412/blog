package com.he.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.he.entity.*;
import com.he.entity.vo.CommentVo;
import com.he.entity.vo.UserVo;
import com.he.mapper.CommitMapper;
import com.he.mapper.SysUserMapper;
import com.he.service.CommitService;
import com.he.service.SysUserService;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommitServiceImpl implements CommitService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private CommitMapper commitMapper;

    //根据文章id查询所有评论
    public Result selectCommit(String id) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        //查询一级评论 ，level为1  
        wrapper.eq(Comment::getArticleId, id);
        wrapper.eq(Comment::getLevel, 1);
        List<Comment> comments = commitMapper.selectList(wrapper);
        return Result.success(copyList(comments));
    }

    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLoacl.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
         Integer parent = Integer.parseInt(commentParam.getParent());
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        } else {
            comment.setLevel(2);
        }
        if(parent==null){
            comment.setParentId("0");
        }else {
            comment.setParentId(parent+"");
        }

        String  toUserId = commentParam.getToUserId();
        if(toUserId==null){
            comment.setToUid("0");
        }else {
            comment.setToUid(toUserId+"");
        }
        commitMapper.insert(comment);
        return Result.success(null);
    }

    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        SysUser sysUser = sysUserMapper.selectById(comment.getAuthorId());
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser, userVo);
        commentVo.setAuthor(userVo);
        //查询评论的评论
        List<CommentVo> commentVoList = findCommentsByParentId(comment.getId());
        //把二级评论添加到封装的对象中
        commentVo.setChildrens(commentVoList);
        //如果level大于1 证明有二级评论
        if (commentVo.getLevel() > 1) {
            SysUser sysUser1 = sysUserMapper.selectById(comment.getToUid());
            UserVo userVo1 = new UserVo();
            BeanUtils.copyProperties(sysUser1, userVo1);
            //给谁的评论
            commentVo.setToUser(userVo1);
        }
        return commentVo;
    }

    //通过父id查询
    private List<CommentVo> findCommentsByParentId(String   id) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getParentId, id);
        //级别为二级
        wrapper.eq(Comment::getLevel, 2);
        List<Comment> comments = commitMapper.selectList(wrapper);
        return copyList(comments);
    }
}
