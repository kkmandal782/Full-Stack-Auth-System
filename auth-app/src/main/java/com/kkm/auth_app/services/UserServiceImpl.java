package com.kkm.auth_app.services;

import com.kkm.auth_app.dto.UserDto;
import com.kkm.auth_app.entities.Provider;
import com.kkm.auth_app.entities.User;
import com.kkm.auth_app.exception.ResourceNotFoundException;
import com.kkm.auth_app.helpers.UserHelper;
import com.kkm.auth_app.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserDto register(UserDto userDto) {
        if(userDto.getEmail() == null || userDto.getEmail().isBlank()){
            throw new IllegalArgumentException("Email is required");
        }
        if(userRepository.existsByEmail(userDto.getEmail())){
            throw new IllegalArgumentException("Email already exists");
        }

        User user = modelMapper.map(userDto, User.class);
        user.setProvider(userDto.getProvider()!=null?userDto.getProvider(): Provider.LOCAL);

        //TODO: role assign here to user for authorization.

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->
            new ResourceNotFoundException("Resource Not Found with given email id")
        );
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userid) {
        UUID uid = UserHelper.parseUUID(userid);
        User existingUser = userRepository
                .findById(uid)
                .orElseThrow(()->new ResourceNotFoundException("Resource Not Found with given User id"));
       //Update process:::::::
       if(userDto.getName()!=null) existingUser.setName(userDto.getName());
       if(userDto.getImage() != null) existingUser.setImage(userDto.getImage());
       if(userDto.getProvider()!= null) existingUser.setProvider(userDto.getProvider());
       if(userDto.getPassword() != null) existingUser.setPassword(userDto.getPassword());
       existingUser.setEnabled(userDto.isEnabled());
       existingUser.setUpdatedAt(Instant.now());
       User updatedUser = userRepository.save(existingUser);
       return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public void deleteUser(String userId) {
        UUID uID = UserHelper.parseUUID(userId);
        User user = userRepository.findById(uID).orElseThrow(()->new ResourceNotFoundException("Resource Not Found with given User id"));
        userRepository.delete(user);
    }

    @Override
    public UserDto getUserById(String userId) {

        User user = userRepository.findById(UserHelper.parseUUID(userId)).orElseThrow(()->new ResourceNotFoundException("Resource Not Found with given User id"));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public Iterable<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user->modelMapper.map(user, UserDto.class))
                .toList();
    }
}
