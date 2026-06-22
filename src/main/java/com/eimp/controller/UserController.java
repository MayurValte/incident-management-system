package com.eimp.controller;

import com.eimp.dto.UserDetailsDTO;
import com.eimp.dto.UserProfilesDTO;
import com.eimp.dto.UsersDTO;
import com.eimp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/user")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<UsersDTO> createUser(@Valid @RequestBody UsersDTO userDTO) {
        UsersDTO savedUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping
    public ResponseEntity<List<UserDetailsDTO>> getAllUsers() {
        List<UserDetailsDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailsDTO> getUserById(@PathVariable Long userId) {
        UserDetailsDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("{userId}/profile")
    public ResponseEntity<UserProfilesDTO> updateUserProfile(@PathVariable Long userId, @RequestBody UserProfilesDTO userProfilesDTO, Authentication authentication) {
        UserProfilesDTO profilesDTO = userService.updateUserProfile(userId, userProfilesDTO,authentication);
        return ResponseEntity.ok(profilesDTO);
    }

    @GetMapping("/userProfile/{userId}")
    public ResponseEntity<UserProfilesDTO> getUserProfileByUserId(@PathVariable Long userId) {

        UserProfilesDTO userProfileByUserId = userService.getUserProfileByUserId(userId);

        return ResponseEntity.ok(userProfileByUserId);
    }
}
