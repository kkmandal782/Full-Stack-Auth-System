package com.kkm.auth_app.services;

import com.kkm.auth_app.dto.UserDto;

public interface AuthService {
    UserDto registerUser(UserDto userDto);
    //login user 
}
