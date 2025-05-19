package com.subro.blog.repositories;

import com.subro.blog.entities.Category;
import com.subro.blog.entities.Post;
import com.subro.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByUser(User user);
    List<Post> findByCategory(Category category);
    List<Post> findByPostTitleContaining(String title);
    Optional<Post> findByPostIdAndUserId(Integer postId, Integer userId);
}
