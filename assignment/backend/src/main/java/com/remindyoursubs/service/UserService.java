package com.remindyoursubs.service;

import com.remindyoursubs.dto.UserRequestDTO;
import com.remindyoursubs.dto.UserResponseDTO;
import com.remindyoursubs.exception.ResourceNotFoundException;
import com.remindyoursubs.model.User;
import com.remindyoursubs.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j

@Service
@Transactional
public class UserService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
            org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email)
            throws org.springframework.security.core.userdetails.UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException(
                        "User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                new java.util.ArrayList<>());
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return convertToDTO(user);
    }

    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return convertToDTO(user);
    }

    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + requestDTO.getEmail());
        }

        User user = new User();
        user.setEmail(requestDTO.getEmail());
        user.setName(requestDTO.getName());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setReminderDaysBefore(requestDTO.getReminderDaysBefore());
        user.setResendApiKey(requestDTO.getResendApiKey());

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (!user.getEmail().equals(requestDTO.getEmail()) &&
                userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + requestDTO.getEmail());
        }

        user.setEmail(requestDTO.getEmail());
        user.setName(requestDTO.getName());
        // Password update logic should be separate or handled if provided
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        }
        user.setReminderDaysBefore(requestDTO.getReminderDaysBefore());
        user.setResendApiKey(requestDTO.getResendApiKey());

        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepository.delete(user);
    }

    private UserResponseDTO convertToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setReminderDaysBefore(user.getReminderDaysBefore());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}
