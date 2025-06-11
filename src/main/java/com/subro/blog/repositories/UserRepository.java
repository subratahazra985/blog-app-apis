package com.subro.blog.repositories;

import com.subro.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user to retrieve
     * @return the user with the given email address, or empty if no such user exists
     */
    Optional<User> findByEmail(String email);
}
