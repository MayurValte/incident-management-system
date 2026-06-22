package com.eimp.controller;

import com.eimp.dto.UserDetailsDTO;
import com.eimp.dto.UserProfilesDTO;
import com.eimp.dto.UsersDTO;
import com.eimp.security.JwtAuthenticationFilter;
import com.eimp.security.service.JwtService;
import com.eimp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtService jwtService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnUserById() throws Exception {

        UsersDTO user = new UsersDTO();
        user.setId(1L);
        user.setFirstName("Mayur");
        user.setLastName("Valte");
        user.setEmail("mayur@test.com");
        user.setRole("ADMIN");
        user.setStatus("ACTIVE");

        UserProfilesDTO profile = new UserProfilesDTO();
        profile.setId(1L);
        profile.setUserId(1L);
        profile.setPhone("9876543210");
        profile.setDepartment("IT");
        profile.setDesignation("Software Engineer");

        UserDetailsDTO response = new UserDetailsDTO();
        response.setUsersDTO(user);
        response.setUserProfilesDTO(profile);

        when(userService.getUserById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.user.firstName")
                        .value("Mayur"))

                .andExpect(jsonPath("$.data.user.lastName")
                        .value("Valte"))

                .andExpect(jsonPath("$.data.user.email")
                        .value("mayur@test.com"))

                .andExpect(jsonPath("$.data.profile.id")
                        .value(1))

                .andExpect(jsonPath("$.data.profile.phone")
                        .value("9876543210"))

                .andExpect(jsonPath("$.data.profile.department")
                        .value("IT"))

                .andExpect(jsonPath("$.data.profile.designation")
                        .value("Software Engineer"));

        verify(userService).getUserById(1L);
    }
}