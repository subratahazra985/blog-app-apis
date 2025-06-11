package com.subro.blog.services.impl;

import com.subro.blog.entities.User;
import com.subro.blog.exceptions.ResourceNotFoundException;
import com.subro.blog.payloads.UserDto;
import com.subro.blog.repositories.UserRepository;
import com.subro.blog.services.UserService;;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    /**
     * Creates a new user based on the given userDto.
     *
     * @param userDto the user details to be saved
     * @return a UserDto containing the saved user details
     */
    @Override
    public UserDto  createUser(UserDto userDto) {
        User user = this.dtoToUser(userDto);
        User savedUser = this.userRepository.save(user);
        return this.userToDto(savedUser);
    }

    /**
     * Updates the user identified by the given userId with the provided information.
     *
     * @param userDto the user information to be used to update the existing user
     * @param userId the ID of the user to be updated
     * @return a UserDto containing the updated user details
     * @throws ResourceNotFoundException if the user with the given ID does not exist
     */
    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());
        User updatedUser = this.userRepository.save(user);
        return this.userToDto(updatedUser);
    }

    /**
     * Retrieves the user identified by the given userId.
     * @param userId The ID of the user to be retrieved
     * @return A UserDto containing the retrieved user details
     * @throws ResourceNotFoundException if the user with the given ID does not exist
     */
    @Override
    public UserDto getUserById(Integer userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        return this.userToDto(user);
    }

    /**
     * Retrieves all users.
     *
     * @return a list of UserDto objects
     */
    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = this.userRepository.findAll();
        return users.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());
    }

    /**
     * Deletes the user identified by the given userId.
     *
     * @param userId the ID of the user to be deleted
     * @throws ResourceNotFoundException if the user with the given ID does not exist
     */
    @Override
    public void deleteUser(Integer userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        this.userRepository.delete(user);

    }

    /**
     * Converts a UserDto into a User.
     * @param userDto the UserDto to be converted
     * @return the corresponding User
     */
    public User dtoToUser(UserDto userDto){
        User user=this.modelMapper.map(userDto, User.class);
//        user.setId(userDto.getId());
//        user.setName(userDto.getName());
//        user.setEmail(userDto.getEmail());
//        user.setPassword(userDto.getPassword());
//        user.setAbout(userDto.getAbout());
       return user;
    }

    /**
     * Converts a User into a UserDto.
     * @param user the User to be converted
     * @return the corresponding UserDto
     */
    public UserDto userToDto(User user){
        UserDto userDto=this.modelMapper.map(user, UserDto.class);
//        userDto.setId(user.getId());
//        userDto.setName(user.getName());
//        userDto.setEmail(user.getEmail());
//        userDto.setPassword(user.getPassword());
//        userDto.setAbout(user.getAbout());
        return userDto;
    }
}
