package com.eimp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UsersDTO {
    private Long id;
    private String firstName;
    private String lastName;
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;
    private String role;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}