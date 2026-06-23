package com.eimp.service.impl;

import com.eimp.dto.UserDetailsDTO;
import com.eimp.dto.UserProfilesDTO;
import com.eimp.dto.UsersDTO;
import com.eimp.entity.UserProfilesEntity;
import com.eimp.entity.UsersEntity;
import com.eimp.exception.ResourceNotFoundException;
import com.eimp.repository.UserProfileRepository;
import com.eimp.repository.UserRepository;
import com.eimp.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public UserImpl(UserRepository userRepository, ModelMapper modelMapper, UserProfileRepository userProfileRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsersDTO createUser(UsersDTO userDTO) {
        UsersEntity entity = modelMapper.map(userDTO, UsersEntity.class);
        entity.setPasswordChangeRequired(true);
        entity.setPassword(passwordEncoder.encode("User@123"));
        UsersEntity saved = userRepository.save(entity);
        return modelMapper.map(saved, UsersDTO.class);
    }

    @Override
    @Cacheable(value = "allUsers")
    public List<UserDetailsDTO> getAllUsers() {
        List<UsersEntity> entities = userRepository.findAllUsersWithProfiles();

        return entities.stream().map(userEntity -> {

            UsersDTO usersDTO = modelMapper.map(userEntity, UsersDTO.class);

            UserProfilesDTO profilesDTO = null;

            if (userEntity.getUserProfile() != null) {
                profilesDTO = modelMapper.map(
                        userEntity.getUserProfile(),
                        UserProfilesDTO.class
                );

                profilesDTO.setUserId(userEntity.getId());
            }

            UserDetailsDTO detailsDTO = new UserDetailsDTO();
            detailsDTO.setUsersDTO(usersDTO);
            detailsDTO.setUserProfilesDTO(profilesDTO);

            return detailsDTO;

        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Cacheable(value = "user", key = "#id")
    public UserDetailsDTO getUserById(Long id) {
        UsersEntity userEntity = userRepository.findUserWithProfile(id);
        if (userEntity == null) {
            throw new ResourceNotFoundException("User or UserProfile is not present");
        }

        UsersDTO usersDTO = modelMapper.map(userEntity, UsersDTO.class);
        UserProfilesDTO userProfilesDTO = null;

        if (userEntity.getUserProfile() != null) {
            userProfilesDTO = modelMapper.map(userEntity.getUserProfile(), UserProfilesDTO.class);
            userProfilesDTO.setUserId(userEntity.getId());
        }

        UserDetailsDTO detailsDTO = new UserDetailsDTO();
        detailsDTO.setUsersDTO(usersDTO);
        detailsDTO.setUserProfilesDTO(userProfilesDTO);

        return detailsDTO;
    }

    @Override
    public UserProfilesDTO updateUserProfile(Long userId, UserProfilesDTO userProfilesDTO, Authentication authentication) {
        UsersEntity user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User not found with id : " + userId));

        UserProfilesEntity profileEntity = userProfileRepository.findByUserId(userId).orElse(new UserProfilesEntity());
        profileEntity.setUser(user);
        profileEntity.setPhone(userProfilesDTO.getPhone());
        profileEntity.setDepartment(userProfilesDTO.getDepartment());
        profileEntity.setDesignation(userProfilesDTO.getDesignation());
        profileEntity.setJoiningDate(userProfilesDTO.getJoiningDate());

        UserProfilesEntity savedProfile = userProfileRepository.save(profileEntity);

        return convertToDTO(savedProfile);
    }

    @Override
    @Cacheable(value = "userProfile", key = "#userId")
    public UserProfilesDTO getUserProfileByUserId(Long userId) {

        UserProfilesEntity userProfilesEntity = userProfileRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("UserProfile not found with User id :" + userId));

        return convertToDTO(userProfilesEntity);

    }

    public UserProfilesDTO convertToDTO(UserProfilesEntity entity) {
        UserProfilesDTO dto = new UserProfilesDTO();
        dto.setId(entity.getId());

        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
        }

        dto.setPhone(entity.getPhone());
        dto.setDepartment(entity.getDepartment());
        dto.setDesignation(entity.getDesignation());
        dto.setJoiningDate(entity.getJoiningDate());

        return dto;
    }


}
