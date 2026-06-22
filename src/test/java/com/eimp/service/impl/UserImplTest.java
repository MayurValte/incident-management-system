package com.eimp.service.impl;

import com.eimp.dto.UserDetailsDTO;
import com.eimp.dto.UserProfilesDTO;
import com.eimp.dto.UsersDTO;
import com.eimp.entity.UserProfilesEntity;
import com.eimp.entity.UsersEntity;
import com.eimp.exception.ResourceNotFoundException;
import com.eimp.repository.UserProfileRepository;
import com.eimp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserImpl userService;

    @Test
    void shouldCreateUserSuccessfully() {

        UsersDTO dto = new UsersDTO();
        dto.setEmail("mayur@test.com");

        UsersEntity entity = new UsersEntity();
        entity.setEmail("mayur@test.com");

        UsersEntity savedEntity = new UsersEntity();
        savedEntity.setId(1L);
        savedEntity.setEmail("mayur@test.com");

        UsersDTO responseDto = new UsersDTO();
        responseDto.setId(1L);

        when(modelMapper.map(dto, UsersEntity.class))
                .thenReturn(entity);

        when(passwordEncoder.encode("User@123"))
                .thenReturn("encodedPassword");

        when(userRepository.save(entity))
                .thenReturn(savedEntity);

        when(modelMapper.map(savedEntity, UsersDTO.class))
                .thenReturn(responseDto);

        UsersDTO result = userService.createUser(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(userRepository).save(entity);
    }

    @Test
    void shouldReturnUserById() {

        Long userId = 1L;

        UsersEntity user = new UsersEntity();
        user.setId(userId);

        UsersDTO userDto = new UsersDTO();
        userDto.setId(userId);

        when(userRepository.findUserWithProfile(userId))
                .thenReturn(user);

        when(modelMapper.map(user, UsersDTO.class))
                .thenReturn(userDto);

        assertNotNull(userService.getUserById(userId));

        verify(userRepository)
                .findUserWithProfile(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        Long userId = 1L;

        when(userRepository.findUserWithProfile(userId))
                .thenReturn(null);

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserById(userId)
        );
    }

    @Test
    void shouldReturnUserProfile() {

        Long userId = 1L;

        UserProfilesEntity profile = new UserProfilesEntity();
        profile.setId(100L);

        when(userProfileRepository.findByUserId(userId))
                .thenReturn(Optional.of(profile));

        assertNotNull(
                userService.getUserProfileByUserId(userId));
    }

    @Test
    void shouldUpdateProfileSuccessfully() {

        Long userId = 1L;

        UsersEntity user = new UsersEntity();
        user.setId(userId);

        UserProfilesDTO dto = new UserProfilesDTO();
        dto.setDepartment("IT");

        UserProfilesEntity profile = new UserProfilesEntity();
        profile.setUser(user);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(userProfileRepository.findByUserId(userId))
                .thenReturn(Optional.of(profile));

        when(userProfileRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserProfilesDTO result =
                userService.updateUserProfile(
                        userId,
                        dto,
                        null);

        assertEquals(
                "IT",
                result.getDepartment());
    }

    @Test
    void shouldEncodePasswordAndSetPasswordChangeRequired() {

        UsersDTO dto = new UsersDTO();

        UsersEntity entity = new UsersEntity();
        UsersEntity savedEntity = new UsersEntity();

        when(modelMapper.map(dto, UsersEntity.class))
                .thenReturn(entity);

        when(passwordEncoder.encode("User@123"))
                .thenReturn("encodedPassword");

        when(userRepository.save(entity))
                .thenReturn(savedEntity);

        when(modelMapper.map(savedEntity, UsersDTO.class))
                .thenReturn(new UsersDTO());

        userService.createUser(dto);

        assertEquals("encodedPassword",
                entity.getPassword());

        assertTrue(
                entity.getPasswordChangeRequired());

        verify(passwordEncoder)
                .encode("User@123");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingProfileForInvalidUser() {

        Long userId = 1L;

        UserProfilesDTO dto = new UserProfilesDTO();

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.updateUserProfile(
                        userId,
                        dto,
                        null)
        );

        verify(userProfileRepository, never())
                .save(any());
    }

    @Test
    void shouldCreateProfileIfProfileDoesNotExist() {

        Long userId = 1L;

        UsersEntity user = new UsersEntity();
        user.setId(userId);

        UserProfilesDTO dto = new UserProfilesDTO();
        dto.setDepartment("IT");
        dto.setDesignation("Developer");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(userProfileRepository.findByUserId(userId))
                .thenReturn(Optional.empty());

        when(userProfileRepository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        UserProfilesDTO result =
                userService.updateUserProfile(
                        userId,
                        dto,
                        null);

        assertEquals("IT",
                result.getDepartment());

        assertEquals("Developer",
                result.getDesignation());

        verify(userProfileRepository)
                .save(any(UserProfilesEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenProfileNotFound() {

        Long userId = 1L;

        when(userProfileRepository.findByUserId(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserProfileByUserId(userId)
        );
    }

    @Test
    void shouldReturnEmptyUserList() {

        when(userRepository.findAllUsersWithProfiles())
                .thenReturn(List.of());

        List<UserDetailsDTO> result =
                userService.getAllUsers();

        assertNotNull(result);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnAllUsersWithProfiles() {

        UsersEntity user = new UsersEntity();
        user.setId(1L);

        UserProfilesEntity profile =
                new UserProfilesEntity();

        profile.setDepartment("IT");

        user.setUserProfile(profile);

        UsersDTO userDto = new UsersDTO();
        userDto.setId(1L);

        when(userRepository.findAllUsersWithProfiles())
                .thenReturn(List.of(user));

        when(modelMapper.map(
                user,
                UsersDTO.class))
                .thenReturn(userDto);

        when(modelMapper.map(
                profile,
                UserProfilesDTO.class))
                .thenReturn(new UserProfilesDTO());

        List<UserDetailsDTO> result =
                userService.getAllUsers();

        assertEquals(1,
                result.size());

        verify(userRepository)
                .findAllUsersWithProfiles();
    }

    @Test
    void shouldConvertEntityToDto() {

        UsersEntity user = new UsersEntity();
        user.setId(10L);

        UserProfilesEntity entity =
                new UserProfilesEntity();

        entity.setId(100L);
        entity.setUser(user);
        entity.setDepartment("IT");
        entity.setDesignation("Developer");

        UserProfilesDTO dto =
                userService.convertToDTO(entity);

        assertEquals(100L,
                dto.getId());

        assertEquals(10L,
                dto.getUserId());

        assertEquals("IT",
                dto.getDepartment());

        assertEquals("Developer",
                dto.getDesignation());
    }
}