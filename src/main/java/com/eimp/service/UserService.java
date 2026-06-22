package com.eimp.service;

import com.eimp.dto.UserDetailsDTO;
import com.eimp.dto.UserProfilesDTO;
import com.eimp.dto.UsersDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {
    UsersDTO createUser(UsersDTO userDTO);

    List<UserDetailsDTO> getAllUsers();

    UserDetailsDTO getUserById(Long id);

    UserProfilesDTO updateUserProfile(Long userId, UserProfilesDTO userProfilesDTO, Authentication authentication);

    UserProfilesDTO getUserProfileByUserId(Long userId);
}
