package com.kkm.auth_app.services;

import com.kkm.auth_app.dto.UserDto;

import java.util.UUID;

public interface UserService {
    UserDto register(UserDto userDto);

    UserDto getUserByEmail(String email);

    UserDto updateUser(UserDto userDto, String userid);

    void deleteUser(String userId);

    UserDto getUserById(String userId);

    //getAllUsers::::::::;;
    Iterable<UserDto> getAllUsers();
}
