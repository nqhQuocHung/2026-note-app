package com.hung.noteapp.auth.services.impls;

import com.hung.noteapp.auth.dtos.UserResponseDTO;
import com.hung.noteapp.auth.dtos.UserUpdateDTO;
import com.hung.noteapp.auth.enums.GenderEnum;
import com.hung.noteapp.auth.pojos.User;
import com.hung.noteapp.auth.repositories.UserRepository;
import com.hung.noteapp.auth.services.CloudinaryService;
import com.hung.noteapp.auth.services.MessageService;
import com.hung.noteapp.auth.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final MessageService messageService;

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        messageService.get("user.not_found_by_id", id)
                ));

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            if (!dto.getEmail().equals(user.getEmail())
                    && userRepository.existsByEmail(dto.getEmail())) {
                throw new IllegalArgumentException(messageService.get("user.email_exists"));
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
        return mapToResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        messageService.get("user.not_found_by_id", id)
                ));

        return mapToResponse(user);
    }

    private UserResponseDTO mapToResponse(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender() != null
                        ? GenderEnum.fromValue(user.getGender()).name().toLowerCase()
                        : null)
                .phone(user.getPhone())
                .avatarUrl(user.getAvatar())
                .roleId(user.getRoles() != null && !user.getRoles().isEmpty()
                        ? user.getRoles().iterator().next().getId()
                        : null)
                .build();
    }
    @Override
    @Transactional
    public UserResponseDTO update(Long id, UserUpdateDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        messageService.get("user.not_found_by_id", id)
                ));


        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            if (!dto.getEmail().equals(user.getEmail())
                    && userRepository.existsByEmail(dto.getEmail())) {
                throw new IllegalArgumentException(messageService.get("user.email_exists"));
            }
            user.setEmail(dto.getEmail().trim());
        }

        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName().trim());
        }

        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName().trim());
        }

        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone().trim());
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
        return mapToResponse(savedUser);
    }
}