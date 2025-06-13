package com.subro.blog.security;

import com.subro.blog.entities.User;
import com.subro.blog.exceptions.ResourceNotFoundException;
import com.subro.blog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    /**
     * Loads user by username from the database and returns UserDetails object.
     *
     * @param username the username to search for
     * @return UserDetails object
     * @throws UsernameNotFoundException if no user is found with the given username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Loading user from database by username
        User user = this.userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User", "email : " + username, 0));
        return user;
    }
}
