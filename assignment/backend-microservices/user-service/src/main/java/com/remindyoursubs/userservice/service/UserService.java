package com.remindyoursubs.userservice.service;

import com.remindyoursubs.userservice.dto.UserRequestDTO;
import com.remindyoursubs.userservice.dto.UserResponseDTO;

import java.util.List;

/**
 * Service interface for User operations
 */
public interface UserService {

    /**
     * Get all users
     * 
     * @return List of all users
     */
    List<UserResponseDTO> getAllUsers();

    /**
     * Get user by ID
     * 
     * @param id User ID
     * @return User details
     */
    UserResponseDTO getUserById(Long id);

    /**
     * Get user by email
     * 
     * @param email User email
     * @return User details
     */
    UserResponseDTO getUserByEmail(String email);

    /**
     * Create new user
     * 
     * @param userRequestDTO User data
     * @return Created user
     */
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);

    /**
     * Update existing user
     * 
     * @param id             User ID
     * @param userRequestDTO Updated user data
     * @return Updated user
     */
    UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO);

    /**
     * Delete user
     * 
     * @param id User ID
     */
    void deleteUser(Long id);
}
