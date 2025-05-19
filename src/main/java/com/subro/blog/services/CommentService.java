package com.subro.blog.services;

import com.subro.blog.payloads.CommentDto;

public interface CommentService {
    public CommentDto createComment(CommentDto commentDto, Integer postId, Integer userId);
    public void deleteComment(Integer commentId);

}