package com.example.flexiMed.users;

import com.example.flexiMed.exceptions.ErrorResponse;
import com.example.flexiMed.utils.fileStorage.FileStorageService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder,
                       FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageService = fileStorageService;
    }

    public List<UserDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(UserMapper::toDTO).collect(Collectors.toList());
    }

    public UserDTO getUserById(UUID id) {
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new ErrorResponse.NoSuchUserExistsException("User does not exist"));
        return UserMapper.toDTO(user);
    }

    public UserDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(UserMapper::toDTO).orElseThrow(
                () -> new ErrorResponse.NoSuchUserExistsException("User does not exist"));
    }

    public Optional<UserDTO> getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).map(UserMapper::toDTO);
    }

    @Override
    public UserEntity loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }


    public List<UserDTO> getUsersByRole(Role role) {
        List<UserEntity> users = userRepository.findByRole(role);
        return users.stream().map(UserMapper::toDTO).collect(Collectors.toList());
    }

    public UserEntity saveUser(UserEntity user) {
        Optional<UserEntity> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new ErrorResponse.UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            // Skip password encoding for OAuth users
            user.setPassword(""); // Empty password
        } else {
            // Encode only if password is provided
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(user);
    }

    @Transactional
    public UserDTO updateUserProfile(UUID userId, UserDTO updatedUser, MultipartFile profileImage) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        // Update user details
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPhoneNumber(updatedUser.getPhoneNumber());

        // Handle profile image upload with exception handling
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String imageUrl = fileStorageService.saveFile(profileImage, userId);
                user.setProfileImageUrl(imageUrl); // Save image URL to database
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload profile image: " + e.getMessage(), e);
            }
        }

        userRepository.save(user);
        return UserMapper.toDTO(user);
    }

    public void deleteUser(UUID id) {
        UserEntity existingUser = userRepository.findById(id).orElseThrow(
                () -> new ErrorResponse.NoSuchUserExistsException("User does not exist"));
        userRepository.deleteById(id);
    }
}
