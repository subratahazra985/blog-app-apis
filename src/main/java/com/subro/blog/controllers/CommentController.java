package com.subro.blog.controllers;

import com.subro.blog.payloads.ApiResponse;
import com.subro.blog.payloads.CommentDto;
import com.subro.blog.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class CommentController {
    @Autowired
    private CommentService commentService;
   @PostMapping("post/{postId}/comments")
   public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto, @PathVariable Integer postId){
       CommentDto savedCommentDto = this.commentService.createComment(commentDto, postId);
       return new ResponseEntity<CommentDto>(savedCommentDto, HttpStatus.CREATED);
    }

   @DeleteMapping("comments/{commentId}")
   public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId){
        this.commentService.deleteComment(commentId);
        return new ResponseEntity<>(new ApiResponse("Comment deleted successfully", true), HttpStatus.OK);
   }
}
