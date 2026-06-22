package com.eimp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDetailsDTO {

    @JsonProperty("user")
    UsersDTO usersDTO;
    @JsonProperty("profile")
    UserProfilesDTO userProfilesDTO;

}
