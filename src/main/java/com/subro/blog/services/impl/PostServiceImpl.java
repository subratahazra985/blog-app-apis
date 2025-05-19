package com.subro.blog.services.impl;

import com.subro.blog.entities.Category;
import com.subro.blog.entities.Post;
import com.subro.blog.entities.User;
import com.subro.blog.exceptions.ResourceNotFoundException;
import com.subro.blog.payloads.PostDto;
import com.subro.blog.payloads.PostResponse;
import com.subro.blog.repositories.CategoryRepository;
import com.subro.blog.repositories.PostRepository;
import com.subro.blog.repositories.UserRepository;
import com.subro.blog.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service

public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PostResponse postResponse;


    @Override
    public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
        Post post = this.modelMapper.map(postDto, Post.class);
        post.setImageName("default.png");
        post.setAddedDate(new Date());
        post.setUser(user);
        post.setCategory(category);
        Post savedPost = this.postRepository.save(post);
        return this.modelMapper.map(savedPost, PostDto.class);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));
        post.setPostTitle(postDto.getPostTitle());
        post.setPostContent(postDto.getPostContent());
        post.setImageName(postDto.getImageName());
        this.postRepository.save(post);
        return this.modelMapper.map(post, PostDto.class);
    }

    @Override
    public void deletePost(Integer postId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));
        this.postRepository.delete(post);
    }

    @Override
    public PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        Page<Post> pagePostList = this.postRepository.findAll(pageRequest);
        List<Post> postList = pagePostList.getContent();
        List<PostDto> postDtoList = postList.stream().map(post -> this.modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());

        postResponse.setContent(postDtoList);
        postResponse.setPageNumber(pagePostList.getNumber());
        postResponse.setPageSize(pagePostList.getSize());
        postResponse.setTotalElements(pagePostList.getTotalElements());
        postResponse.setTotalPages(pagePostList.getTotalPages());
        postResponse.setLastPage(pagePostList.isLast());
        return postResponse;
    }

    @Override
    public PostDto getPostById(Integer postId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));
        return this.modelMapper.map(post, PostDto.class);
    }

    @Override
    public List<PostDto> getPostsByCategory(Integer categoryId) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
        List<Post> postsByCategory = this.postRepository.findByCategory(category);
        List<PostDto> postDtoList = postsByCategory.stream()
                .map(post -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
        return postDtoList;
    }

    @Override
    public List<PostDto> getPostsByUser(Integer userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
        List<Post> postsByUser = this.postRepository.findByUser(user);
        List<PostDto> postDtoList = postsByUser.stream()
                .map(post -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
        return postDtoList;
    }

    public List<PostDto> searchPostByTitle(String keywords) {
        List<Post> posts = this.postRepository.findByPostTitleContaining(keywords);
        List<PostDto> postDtoList = posts.stream()
                .map(post -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
        return postDtoList;
    }
}
