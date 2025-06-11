package com.subro.blog.repositories;

import com.subro.blog.entities.Category;
import com.subro.blog.entities.Post;
import com.subro.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    /**
     * Retrieves a list of posts written by the given user.
     * @param user the user
     * @return the list of posts
     */
    List<Post> findByUser(User user);
    /**
     * Retrieves a list of posts associated with the given category.
     * @param category the category
     * @return the list of posts
     */
    List<Post> findByCategory(Category category);
    /**
     * Retrieves a list of posts with titles containing the specified keyword.
     *
     * @param title the keyword to search for in post titles
     * @return the list of posts with titles containing the keyword
     */
    List<Post> findByPostTitleContaining(String title);
    /**
     * Retrieves a post by its ID and the ID of the user who wrote it.
     *
     * @param postId the ID of the post to retrieve
     * @param userId the ID of the user who wrote the post
     * @return the post with the given ID and written by the given user, or empty if no such post exists
     */
    Optional<Post> findByPostIdAndUserId(Integer postId, Integer userId);
}
