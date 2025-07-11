package com.subro.blog.controllers;

import com.subro.blog.config.AppConstants;
import com.subro.blog.entities.Post;
import com.subro.blog.payloads.ApiResponse;
import com.subro.blog.payloads.PostDto;
import com.subro.blog.payloads.PostResponse;
import com.subro.blog.services.FileService;
import com.subro.blog.services.PostService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private FileService fileService;
    @Value("${project.image}")
    private String path;
    /**
     * Creates a new post under the specified user and category.
     *
     * @param postDto the post details
     * @param userId the user ID
     * @param categoryId the category ID
     * @return a ResponseEntity containing the saved post details
     */
    @PostMapping("/user/{userId}/category/{categoryId}/posts")
    public ResponseEntity<PostDto> createPost(
            @RequestBody @Valid PostDto postDto,
            @PathVariable Integer userId,
            @PathVariable Integer categoryId)
    {
        PostDto savedPostDto = this.postService.createPost(postDto, userId, categoryId);
        return new ResponseEntity<PostDto>(savedPostDto, HttpStatus.CREATED);
    }

    /**
     * Retrieves all posts created by a specific user.
     *
     * @param userId the ID of the user whose posts are to be retrieved
     * @return a ResponseEntity containing a list of PostDto objects and an HTTP status of OK
     */
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable Integer userId){
        List<PostDto> postsByUser = this.postService.getPostsByUser(userId);
        return new ResponseEntity<>(postsByUser, HttpStatus.OK);
    }

    /**
     * Retrieves all posts categorized under the specified category ID.
     *
     * @param categoryId the ID of the category whose posts are to be retrieved
     * @return a ResponseEntity containing a list of PostDto objects and an HTTP status of OK
     */
    @GetMapping("/category/{categoryId}/posts")
    public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable Integer categoryId){
        List<PostDto> postsByCategory = this.postService.getPostsByCategory(categoryId);
        return new ResponseEntity<>(postsByCategory, HttpStatus.OK);
    }

    /**
     * Retrieves a post by its ID.
     *
     * @param postId the ID of the post to be retrieved
     * @return a ResponseEntity containing the PostDto object and an HTTP status of OK
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId){
        PostDto postDtoById = this.postService.getPostById(postId);
        return new ResponseEntity<>(postDtoById, HttpStatus.OK);
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
     * @return a ResponseEntity containing the posts and an HTTP status of OK
     */
    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value="sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value="sortDirection", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortDirection)
    {
        PostResponse allPosts = this.postService.getAllPosts(pageNumber, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(allPosts, HttpStatus.OK);
    }

    /**
     * Updates a post identified by its ID.
     *
     * @param postDto the post details to be updated
     * @param postId the ID of the post to be updated
     * @return a ResponseEntity containing the updated PostDto and an HTTP status of OK
     */
    @PutMapping("posts/{postId}")
    public ResponseEntity<PostDto> updatePost(@RequestBody @Valid PostDto postDto, @PathVariable Integer postId ){
        PostDto updatedPostDto = this.postService.updatePost(postDto, postId);
        return new ResponseEntity<>(updatedPostDto, HttpStatus.OK);
    }

    /**
     * Deletes a post identified by its ID.
     *
     * @param postId the ID of the post to be deleted
     * @return a ResponseEntity containing an ApiResponse with a success message and an HTTP status of OK
     */
    @DeleteMapping("posts/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId){
        this.postService.deletePost(postId);
        return new ResponseEntity<>(new ApiResponse("Post is delete successfully!!", true), HttpStatus.OK);
    }

    /**
     * Retrieves all posts with titles containing the given search keywords.
     *
     * @param keywords the search keywords
     * @return a ResponseEntity containing the list of PostDto objects and an HTTP status of OK
     */
    @GetMapping("/posts/search/{keywords}")
    public ResponseEntity<List<PostDto>> searchPostsByTitle(
            @PathVariable("keywords")String keywords
    ){
        List<PostDto> postDtos = this.postService.searchPostByTitle(keywords);
        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    /**
     * Uploads an image to the server and assigns it to a post identified by its ID.
     *
     * @param image the image to be uploaded
     * @param postId the ID of the post to which the image is to be assigned
     * @return a ResponseEntity containing the updated PostDto and an HTTP status of OK
     * @throws IOException
     */
    @PostMapping("/post/image/upload/{postId}")
    public ResponseEntity<PostDto> uploadPostImage(
            @RequestParam("image") MultipartFile image,
            @PathVariable Integer postId
    ) throws IOException {
        PostDto postDto = this.postService.getPostById(postId);
        String fileName = this.fileService.uploadImage(path, image);
        postDto.setImageName(fileName);
        PostDto updatedPost = this.postService.updatePost(postDto, postId);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    /**
     * Downloads an image associated with a post identified by its image name.
     *
     * @param imageName the name of the image to be downloaded
     * @param response the HttpServletResponse to write the image to
     * @throws IOException if an error occurs while reading the image from the file system
     */
    @GetMapping(value = "post/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageName") String imageName,
            HttpServletResponse response) throws IOException {
        InputStream resource = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
