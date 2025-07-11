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


    /**
     * Creates a new post with the provided details under the specified user and category.
     *
     * @param postDto the data transfer object containing the details of the post to be created
     * @param userId the ID of the user who is creating the post
     * @param categoryId the ID of the category under which the post is to be categorized
     * @return a PostDto containing the saved post details
     * @throws ResourceNotFoundException if the user or category specified by their ID does not exist
     */
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

    /**
     * Updates an existing post with the provided details.
     *
     * @param postDto the data transfer object containing the updated post details
     * @param postId the ID of the post to be updated
     * @return a PostDto containing the updated post details
     * @throws ResourceNotFoundException if the post with the specified ID does not exist
     */
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

    /**
     * Deletes a post identified by its ID.
     *
     * @param postId the ID of the post to be deleted
     * @throws ResourceNotFoundException if the post with the specified ID does not exist
     */
    @Override
    public void deletePost(Integer postId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));
        this.postRepository.delete(post);
    }

    /**
     * Retrieves all posts in the system. The posts can be sorted on the basis of post title or post creation date.
     * The posts can be retrieved in a paginated manner by specifying the page number and page size.
     * The default page number is 0 and the default page size is 10.
     *
     * @param pageNumber the page number of the posts to be retrieved, default is 0
     * @param pageSize the page size of the posts to be retrieved, default is 10
     * @param sortBy the field to sort the posts on, default is post title
     * @param sortDirection the direction of sorting, default is ascending
     * @return a PostResponse containing the posts and an HTTP status of OK
     */
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

    /**
     * Retrieves a post by its ID.
     *
     * @param postId the ID of the post to be retrieved
     * @return a PostDto containing the retrieved post details
     * @throws ResourceNotFoundException if the post with the specified ID does not exist
     */
    @Override
    public PostDto getPostById(Integer postId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));
        return this.modelMapper.map(post, PostDto.class);
    }

    /**
     * Retrieves all posts categorized under the specified category ID.
     *
     * @param categoryId the ID of the category whose posts are to be retrieved
     * @return a list of PostDto objects containing the retrieved post details
     * @throws ResourceNotFoundException if the category with the specified ID does not exist
     */
    @Override
    public List<PostDto> getPostsByCategory(Integer categoryId) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
        List<Post> postsByCategory = this.postRepository.findByCategory(category);
        List<PostDto> postDtoList = postsByCategory.stream()
                .map(post -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
        return postDtoList;
    }

    /**
     * Retrieves all posts written by the given user.
     *
     * @param userId the ID of the user whose posts are to be retrieved
     * @return a list of PostDto objects containing the retrieved post details
     * @throws ResourceNotFoundException if the user with the specified ID does not exist
     */
    @Override
    public List<PostDto> getPostsByUser(Integer userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
        List<Post> postsByUser = this.postRepository.findByUser(user);
        List<PostDto> postDtoList = postsByUser.stream()
                .map(post -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
        return postDtoList;
    }

    /**
     * Retrieves all posts with titles containing the given search keywords.
     *
     * @param keywords the search keywords
     * @return a list of PostDto objects containing the retrieved post details
     */
    public List<PostDto> searchPostByTitle(String keywords) {
        List<Post> posts = this.postRepository.findByPostTitleContaining(keywords);
        List<PostDto> postDtoList = posts.stream()
                .map(post -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
        return postDtoList;
    }
}
