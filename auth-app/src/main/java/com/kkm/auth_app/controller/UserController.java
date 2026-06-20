package com.kkm.auth_app.controller;

import com.kkm.auth_app.dto.UserDto;
import com.kkm.auth_app.services.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        logger.info("Received request to create user with email: {}", userDto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.register(userDto));
    }

    @GetMapping
    public ResponseEntity<Iterable<UserDto>> getAllUsers(){
        logger.info("Received request to fetch all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserEmail(@PathVariable String email){
        logger.info("Received request to fetch user by email: {}", email);
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") String userId){
        logger.info("Received request to delete user with id: {}", userId);
        userService.deleteUser(userId);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto,
                                              @PathVariable("userId") String userId){
        logger.info("Received request to update user with id: {}", userId);
        return ResponseEntity.ok(userService.updateUser(userDto, userId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("userId") String userId){
        logger.info("Received request to fetch user with id: {}", userId);
        return ResponseEntity.ok(userService.getUserById(userId));
    }





}
