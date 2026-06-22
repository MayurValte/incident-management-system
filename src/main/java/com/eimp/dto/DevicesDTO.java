package com.eimp.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DevicesDTO {
    private Long id;

    private String hostname;

    @Pattern(
            regexp = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$",
            message = "Invalid IPv4 address"
    )
    private String ipAddress;

    private String location;
    private String deviceType;
    private String vendor;

    private String status;

    private Timestamp createdAt;
    private Timestamp updatedAt;
}
