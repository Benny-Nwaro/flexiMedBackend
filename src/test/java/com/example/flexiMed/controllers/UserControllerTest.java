//package com.example.lms.controllers;
//
//import com.example.lms.users.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(MockitoExtension.class)
//class UserControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private UserController userController;
//
//    private UUID userId;
//    private UserDTO userDTO;
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//
//        userId = UUID.randomUUID();
//        userDTO = new UserDTO(userId, "Aroh", "Ebenezer", "aroh@aroh.com",
//                Role.ADMIN.name(), "Software Engineer",
//                Gender.MALE, "1234567890", new Date(), "password");
//    }
//
//    @Test
//    void getAllUsers_ShouldReturnListOfUsers() throws Exception {
//        List<UserDTO> users = List.of(userDTO);
//        when(userService.getAllUsers()).thenReturn(users);
//
//        mockMvc.perform(get("/api/v1/users"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(users.size()))
//                .andExpect(jsonPath("$[0].id").value(userId.toString()))
//                .andExpect(jsonPath("$[0].firstName").value("Aroh"))
//                .andExpect(jsonPath("$[0].lastName").value("Ebenezer"))
//                .andExpect(jsonPath("$[0].email").value("aroh@aroh.com"))
//                .andExpect(jsonPath("$[0].role").value(Role.ADMIN.name()))
//                .andExpect(jsonPath("$[0].phoneNumber").value("1234567890"));
//
//        verify(userService, times(1)).getAllUsers();
//    }
//
//    @Test
//    void getUserById_ShouldReturnUser() throws Exception {
//        when(userService.getUserById(userId)).thenReturn(userDTO);
//
//        mockMvc.perform(get("/api/v1/users/{id}", userId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(userId.toString()))
//                .andExpect(jsonPath("$.firstName").value("Aroh"))
//                .andExpect(jsonPath("$.lastName").value("Ebenezer"))
//                .andExpect(jsonPath("$.email").value("aroh@aroh.com"))
//                .andExpect(jsonPath("$.role").value(Role.ADMIN.name()))
//                .andExpect(jsonPath("$.phoneNumber").value("1234567890"));
//
//        verify(userService, times(1)).getUserById(userId);
//    }
//
//    @Test
//    void getUserByEmail_ShouldReturnUser() throws Exception {
//        when(userService.getUserByEmail("aroh@aroh.com")).thenReturn(userDTO);
//
//        mockMvc.perform(get("/api/v1/users/by-email")
//                        .param("email", "aroh@aroh.com"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(userId.toString()))
//                .andExpect(jsonPath("$.firstName").value("Aroh"))
//                .andExpect(jsonPath("$.lastName").value("Ebenezer"))
//                .andExpect(jsonPath("$.email").value("aroh@aroh.com"))
//                .andExpect(jsonPath("$.role").value(Role.ADMIN.name()))
//                .andExpect(jsonPath("$.phoneNumber").value("1234567890"));
//
//        verify(userService, times(1)).getUserByEmail("aroh@aroh.com");
//    }
//
//    @Test
//    void deleteUser_ShouldReturnNoContent() throws Exception {
//        doNothing().when(userService).deleteUser(userId);
//
//        mockMvc.perform(delete("/api/v1/users/{id}", userId))
//                .andExpect(status().isNoContent());
//
//        verify(userService, times(1)).deleteUser(userId);
//    }
//}
