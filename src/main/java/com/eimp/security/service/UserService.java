package com.eimp.security.service;


import com.eimp.entity.UsersEntity;
import com.eimp.exception.BadRequestException;
import com.eimp.repository.UserRepository;
import com.eimp.security.dto.ChangePasswordRequest;
import com.eimp.service.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public String changePassword(ChangePasswordRequest request, Principal principal) {
        UsersEntity user =
                userRepository.findByEmail(principal.getName())
                        .orElseThrow();

//        verify current password
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new BadRequestException("Current Password is incorrect");
        }

        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new BadRequestException("New Password and Confirmed Password do not match");
        }

        if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
            throw new BadRequestException("New Password cannot be same as current password");
        }


        user.setPassword(
                passwordEncoder.encode(request.newPassword()));

        user.setPasswordChangeRequired(false);

        userRepository.save(user);

        Map<String, Object> variables = new HashMap<>();
        variables.put("name", user.getFirstName());

        emailService.sendEmail(user.getEmail(),
                "Password has been changed", "password-reset", variables);


        return "Password changed successfully";
    }
}