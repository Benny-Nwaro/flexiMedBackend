package com.example.flexiMed.users;

import java.util.Objects;

public class UserMapper {

    // Convert UserEntity → UserDTO (Excludes Password for Security)
    public static UserDTO toDTO(UserEntity user) {
        Objects.requireNonNull(user, "UserEntity cannot be null");

        return new UserDTO(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getPhoneNumber(),
                user.getProfileImageUrl(),
                null  // Exclude password for security
        );
    }

    // Convert UserDTO → UserEntity
    public static UserEntity toEntity(UserDTO userDTO) {
        Objects.requireNonNull(userDTO, "UserDTO cannot be null");

        return new UserEntity(
                userDTO.getUserId(),
                userDTO.getName(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getPhoneNumber(),
                userDTO.getRole(),
                userDTO.getProfileImageUrl()
        );
    }
}
