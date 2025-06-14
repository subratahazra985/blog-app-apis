package com.subro.blog.controllers;

import com.subro.blog.payloads.ApiResponse;
import com.subro.blog.payloads.UserDto;
import com.subro.blog.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * Creates a new user based on the information provided in the request body.
     * @param userDto The user information to be created.
     * @return A ResponseEntity containing the created UserDto and an HTTP status of CREATED.
     */
    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid  UserDto userDto){
        UserDto createUserDto = this.userService.createUser(userDto);
        return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
    }

    /**
     * Updates the user identified by the given userId with the provided information.
     * @param userDto The user information to be used to update the existing user.
     * @param userId The ID of the user to be updated.
     * @return A ResponseEntity containing the updated UserDto and an HTTP status of OK.
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UserDto userDto, @PathVariable Integer userId){
        UserDto updatedUser = this.userService.updateUser(userDto, userId);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Retrieves the user identified by the given userId.
     *
     * @param userId The ID of the user to be retrieved.
     * @return A ResponseEntity containing the UserDto of the retrieved user and an HTTP status of OK.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer userId){
        UserDto userDto = this.userService.getUserById(userId);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Handles the HTTP GET request for retrieving all users.
     *
     * @return A ResponseEntity containing the list of UserDto objects of all users and an HTTP status of OK.
     */
    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<UserDto> users = this.userService.getAllUsers();
        return ResponseEntity.ok(users);
    }



    /**
     * Handles the HTTP DELETE request for deleting a user identified by the given userId.
     * @param userId The ID of the user to be deleted.
     * @return A ResponseEntity containing an ApiResponse with a success message and an HTTP status of OK.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUserById(@PathVariable Integer userId){
        this.userService.deleteUser(userId);
//        return ResponseEntity.noContent().build();
//        return new ResponseEntity<>(Map.of("message", "User deleted successfully"), HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(new ApiResponse("User deleted successfully", true),
                HttpStatus.OK);
    }
}
