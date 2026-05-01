package com.kkm.auth_app.services;

import com.kkm.auth_app.dto.UserDto;
import com.kkm.auth_app.entities.User;
import com.kkm.auth_app.exception.ResourceNotFoundException;
import com.kkm.auth_app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository repository;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testRegister_success(){
        UserDto dto = UserDto.builder()
                .email("test@example.com")
                .name("Test User")
                .build();
        User savedUser = new User();
        savedUser.setId(UUID.randomUUID());
        savedUser.setEmail((dto.getEmail()));
        when(repository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(repository.save(any(User.class))).thenReturn(savedUser);

        UserDto result = userService.register(dto);
        assertNotNull(result);
        assertEquals(dto.getEmail(), result.getEmail());
        verify(repository, times(1)).save(any(User.class));
    }
    @Test
    void testRegistered_emailRequired(){
        UserDto dto = UserDto.builder()
                .email("")
                .name("Test User")
                .build();
        IllegalArgumentException exception = assertThrows(
              IllegalArgumentException.class,
                ()->userService.register(dto)
        );
        assertEquals("Email is required", exception.getMessage());
        verify(repository, never()).save(any());
        verify(repository, never()).existsByEmail(any());
    }

    @Test
    void testEmailAlreadyExists(){
        UserDto dto = UserDto.builder()
                .email("kkm@gmail.com")
                .name("kamlesh")
                .build();
        when(repository.existsByEmail(dto.getEmail())).thenReturn(true);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                ()->userService.register(dto)
        );
        assertEquals("Email already exists", exception.getMessage());
        verify(repository, times(1)).existsByEmail(dto.getEmail());
        verify(repository, never()).save(any());
    }

    @Test
    void testGetUserByEmail_success(){
       String email = "kkm@gmail.com";
       User user = User.builder()
               .id(UUID.randomUUID())
               .email(email)
               .build();
       when(repository.findByEmail(email)).thenReturn(Optional.of(user));
       UserDto result = userService.getUserByEmail(email);

       assertNotNull(result);
       assertEquals(email, result.getEmail());

    }

    @Test
    void testGetUserByEmail_notFound(){
        String email = "kkmandal@gmail.com";
        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                ()->userService.getUserByEmail(email)
        );

        assertEquals("Resource Not Found with given email id",exception.getMessage());
    }






}
