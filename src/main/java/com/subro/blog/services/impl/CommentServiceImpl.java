package com.subro.blog.services.impl;

import com.subro.blog.entities.Comment;
import com.subro.blog.entities.Post;
import com.subro.blog.entities.User;
import com.subro.blog.exceptions.ResourceNotFoundException;
import com.subro.blog.payloads.CommentDto;
import com.subro.blog.repositories.CommentRepository;
import com.subro.blog.repositories.PostRepository;
import com.subro.blog.repositories.UserRepository;
import com.subro.blog.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    /**
     * Creates a new comment for a given post by a given user.
     *
     * @param commentDto the comment data transfer object
     * @param postId the ID of the post to which the comment is being added
     * @param userId the ID of the user writing the comment
     * @return a ResponseEntity containing the saved CommentDto and an HTTP status of CREATED
     * @throws ResourceNotFoundException if the post or user does not exist
     */
    @Override
    public CommentDto createComment(CommentDto commentDto, Integer postId, Integer userId) {
        Post post=this.postRepository.findByPostIdAndUserId(postId, userId).orElseThrow(()->new ResourceNotFoundException("Post", "Post Id", postId));
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));

        Comment comment = this.modelMapper.map(commentDto, Comment.class);
        comment.setPost(post);
        comment.setUser(user);
        Comment savedComment = this.commentRepository.save(comment);
        return this.modelMapper.map(savedComment, CommentDto.class);
    }

    /**
     * Deletes a comment identified by its ID.
     *
     * @param commentId the ID of the comment to be deleted
     * @throws ResourceNotFoundException if the comment does not exist
     */
    @Override
    public void deleteComment(Integer commentId) {
        Comment comment = this.commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "Comment Id", commentId));
        this.commentRepository.delete(comment);
    }
}
