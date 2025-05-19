package com.subro.blog.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Integer postId;
    @NotEmpty(message = "Post Title must not be blank or null")
    @Size(min=5)
    private String postTitle;
    @NotEmpty(message = "Post content must not be blank or null")
    @Size(min=5)
    private String postContent;
    private String imageName;
    private Date addedDate;
    private UserDto user;
    private CategoryDto category;
    private List<CommentDto> comments= new ArrayList<>();
}

