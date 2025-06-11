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
   /**
    * Handles the HTTP POST request for creating a new comment on a specific post by a user.
    *
    * @param commentDto the comment data transfer object containing comment details
    * @param postId the ID of the post to which the comment is being added
    * @param userId the ID of the user who is adding the comment
    * @return a ResponseEntity containing the saved CommentDto and an HTTP status of CREATED
    */
   @PostMapping("user/{userId}/post/{postId}/comments")
   public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto, @PathVariable Integer postId, @PathVariable Integer userId){
       CommentDto savedCommentDto = this.commentService.createComment(commentDto, postId, userId);
       return new ResponseEntity<CommentDto>(savedCommentDto, HttpStatus.CREATED);
    }

   /**
    * Handles the HTTP DELETE request for deleting a comment identified by its ID.
    *
    * @param commentId the ID of the comment to be deleted
    * @return a ResponseEntity containing an ApiResponse message indicating the successful deletion of the comment
    */
   @DeleteMapping("comments/{commentId}")
   public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId){
        this.commentService.deleteComment(commentId);
        return new ResponseEntity<>(new ApiResponse("Comment deleted successfully", true), HttpStatus.OK);
   }
}
