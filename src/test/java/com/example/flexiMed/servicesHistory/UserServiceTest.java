//package com.example.lms.services;
//
//import com.example.lms.exceptions.ErrorResponse;
//import com.example.lms.users.*;
//import com.example.lms.utils.FileStorageService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @Mock
//    private FileStorageService fileStorageService;
//
//
//    @InjectMocks
//    private UserService userService;
//
//    private UserEntity user;
//    private UUID userId;
//
//    @BeforeEach
//    void setUp() {
//        userId = UUID.randomUUID();
//        user = new UserEntity(
//                userId, "John", "Doe", "john@example.com", "securepassword",
//                "1234567890", new Date(), Role.STUDENT, Gender.MALE,
//                "Bio text", 25
//        );
//    }
//
//    @Test
//    void shouldReturnUserById() {
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//
//        UserDTO result = userService.getUserById(userId);
//
//        assertNotNull(result);
//        assertEquals(userId, result.getId());
//        assertEquals("John", result.getFirstName());
//
//        verify(userRepository, times(1)).findById(userId);
//    }
//
//    @Test
//    void shouldThrowExceptionWhenUserNotFoundById() {
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        assertThrows(ErrorResponse.NoSuchUserExistsException.class, () -> userService.getUserById(userId));
//
//        verify(userRepository, times(1)).findById(userId);
//    }
//
//    @Test
//    void shouldReturnUserByEmail() {
//        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//
//        UserDTO result = userService.getUserByEmail(user.getEmail());
//
//        assertNotNull(result);
//        assertEquals("john@example.com", result.getEmail());
//
//        verify(userRepository, times(1)).findByEmail(user.getEmail());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenUserNotFoundByEmail() {
//        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
//
//        assertThrows(ErrorResponse.NoSuchUserExistsException.class, () -> userService.getUserByEmail(user.getEmail()));
//
//        verify(userRepository, times(1)).findByEmail(user.getEmail());
//    }
//
//    @Test
//    void shouldReturnAllUsers() {
//        List<UserEntity> users = List.of(user);
//        when(userRepository.findAll()).thenReturn(users);
//
//        List<UserDTO> result = userService.getAllUsers();
//
//        assertFalse(result.isEmpty());
//        assertEquals(1, result.size());
//        assertEquals("John", result.get(0).getFirstName());
//
//        verify(userRepository, times(1)).findAll();
//    }
//
//    @Test
//    void shouldReturnUserByPhoneNumber() {
//        when(userRepository.findByPhoneNumber(user.getPhoneNumber())).thenReturn(Optional.of(user));
//
//        Optional<UserDTO> result = userService.getUserByPhoneNumber(user.getPhoneNumber());
//
//        assertTrue(result.isPresent());
//        assertEquals("1234567890", result.get().getPhoneNumber());
//
//        verify(userRepository, times(1)).findByPhoneNumber(user.getPhoneNumber());
//    }
//
//    @Test
//    void shouldSaveNewUser() {
//        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
//        when(passwordEncoder.encode(anyString())).thenReturn("hashedpassword");
//        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
//
//        UserDTO result = userService.saveUser(user);
//
//        assertNotNull(result);
//        assertEquals("John", result.getFirstName());
//        assertEquals("hashedpassword", user.getPassword());
//
//        verify(userRepository, times(1)).save(any(UserEntity.class));
//        verify(passwordEncoder, times(1)).encode(anyString());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenSavingExistingUser() {
//        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//
//        assertThrows(ErrorResponse.UserAlreadyExistsException.class, () -> userService.saveUser(user));
//
//        verify(userRepository, never()).save(any(UserEntity.class));
//    }
//
//    @Test
//    void shouldDeleteExistingUser() {
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        doNothing().when(userRepository).deleteById(userId);
//
//        assertDoesNotThrow(() -> userService.deleteUser(userId));
//
//        verify(userRepository, times(1)).deleteById(userId);
//    }
//
//    @Test
//    void shouldThrowExceptionWhenDeletingNonExistentUser() {
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        assertThrows(ErrorResponse.NoSuchUserExistsException.class, () -> userService.deleteUser(userId));
//
//        verify(userRepository, never()).deleteById(any(UUID.class));
//    }
//
//    @Test
//    void shouldLoadUserByUsername() {
//        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//
//        UserEntity result = userService.loadUserByUsername(user.getEmail());
//
//        assertNotNull(result);
//        assertEquals("John", result.getFirstName());
//
//        verify(userRepository, times(1)).findByEmail(user.getEmail());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenLoadingNonExistentUserByUsername() {
//        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
//
//        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(user.getEmail()));
//
//        verify(userRepository, times(1)).findByEmail(user.getEmail());
//    }
//
//    @Test
//    void shouldUpdateUserProfileWhenUserExists() {
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
//
//        UserDTO updatedUserDTO = new UserDTO();
//        updatedUserDTO.setFirstName("UpdatedName");
//        updatedUserDTO.setLastName("UpdatedLast");
//        updatedUserDTO.setEmail("updated@example.com");
//        updatedUserDTO.setProfileBio("Updated bio");
//        updatedUserDTO.setPhoneNumber("1234567890");
//
//        UserDTO result = userService.updateUserProfile(userId, updatedUserDTO);
//
//        assertNotNull(result);
//        assertEquals("UpdatedName", result.getFirstName());
//        assertEquals("updated@example.com", result.getEmail());
//        assertEquals("Updated bio", result.getProfileBio());
//
//        verify(userRepository, times(1)).findById(userId);
//        verify(userRepository, times(1)).save(any(UserEntity.class));
//    }
//
//    @Test
//    void shouldThrowExceptionWhenUpdatingNonExistentUser() {
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        UserDTO updatedUserDTO = new UserDTO();
//        updatedUserDTO.setFirstName("UpdatedName");
//
//        assertThrows(RuntimeException.class, () -> userService.updateUserProfile(userId, updatedUserDTO));
//
//        verify(userRepository, times(1)).findById(userId);
//        verify(userRepository, never()).save(any(UserEntity.class));
//    }
//
//    @Test
//    void shouldUploadProfileImageWhenUserExists() throws IOException {
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(fileStorageService.saveFile(any(MultipartFile.class), any(UUID.class)))
//                .thenReturn("http://image-url.com/image.jpg");
//        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
//
//        MultipartFile file = mock(MultipartFile.class);
//        UserDTO result = userService.uploadProfileImage(userId, file);
//
//        assertNotNull(result);
//        assertEquals("http://image-url.com/image.jpg", result.getProfileImageUrl());
//
//        verify(fileStorageService, times(1)).saveFile(file, userId);
//        verify(userRepository, times(1)).save(any(UserEntity.class));
//    }
//
//    @Test
//    void shouldThrowExceptionWhenUploadingProfileImageForNonExistentUser() throws IOException {
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        MultipartFile file = mock(MultipartFile.class);
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//                () -> userService.uploadProfileImage(userId, file));
//
//        assertEquals("User not found", exception.getMessage());
//
//        verify(fileStorageService, never()).saveFile(any(MultipartFile.class), any(UUID.class));
//
//        verify(userRepository, never()).save(any(UserEntity.class));
//    }
//
//    @Test
//    void shouldReturnProfileImageUrlWhenUserExists() {
//        user.setProfileImageUrl("http://image-url.com/image.jpg");
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//
//        String imageUrl = userService.getProfileImageUrl(userId);
//
//        assertNotNull(imageUrl);
//        assertEquals("http://image-url.com/image.jpg", imageUrl);
//
//        verify(userRepository, times(1)).findById(userId);
//    }
//
//    @Test
//    void shouldReturnNullWhenProfileImageNotFound() {
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        String imageUrl = userService.getProfileImageUrl(userId);
//
//        assertNull(imageUrl);
//
//        verify(userRepository, times(1)).findById(userId);
//    }
//
//}
//
