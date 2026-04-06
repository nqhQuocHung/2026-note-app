package com.hung.noteapp.auth.services.impls;

import com.hung.noteapp.auth.dtos.UserRegisterDTO;
import com.hung.noteapp.auth.dtos.UserResponseDTO;
import com.hung.noteapp.auth.enums.GenderEnum;
import com.hung.noteapp.auth.pojos.User;
import com.hung.noteapp.auth.repositories.UserRepository;
import com.hung.noteapp.auth.services.CloudinaryService;
import com.hung.noteapp.auth.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserRegisterDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            if (!dto.getUsername().equals(user.getUsername())
                    && userRepository.existsByUsername(dto.getUsername())) {
                throw new IllegalArgumentException("username already exists");
            }
            user.setUsername(dto.getUsername());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            if (!dto.getEmail().equals(user.getEmail())
                    && userRepository.existsByEmail(dto.getEmail())) {
                throw new IllegalArgumentException("email already exists");
            }
            user.setEmail(dto.getEmail());
        }

        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }

        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }

        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }

        if (dto.getGender() != null) {
            user.setGender(dto.getGender());
        }

        MultipartFile avatar = dto.getAvatar();
        if (avatar != null && !avatar.isEmpty()) {
            cloudinaryService.validateAvatarFile(avatar);
            String avatarUrl = cloudinaryService.uploadImage(avatar, "avatars");
            user.setAvatar(avatarUrl);
        }

        User savedUser = userRepository.save(user);

        return UserResponseDTO.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .gender(GenderEnum.fromValue(savedUser.getGender()).name().toLowerCase())
                .phone(savedUser.getPhone())
                .avatarUrl(savedUser.getAvatar())
                .roleId(savedUser.getRoles() != null && !savedUser.getRoles().isEmpty()
                        ? savedUser.getRoles().iterator().next().getId()
                        : null)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(GenderEnum.fromValue(user.getGender()).name().toLowerCase())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatar())
                .roleId(user.getRoles() != null && !user.getRoles().isEmpty()
                        ? user.getRoles().iterator().next().getId()
                        : null)
                .build();
    }
}