package com.eimp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

@Data
public class UserProfilesDTO {
    private Long id;
    private Long userId;
    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^\\d{10}$",
            message = "Phone number must be exactly 10 digits (e.g., 1234567890)"
    )
    private String phone;
    private String department;
    private String designation;
    private Date joiningDate;
}
