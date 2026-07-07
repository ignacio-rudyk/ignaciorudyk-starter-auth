package com.ignaciorudyk.auth.service.implementation;

import com.ignaciorudyk.auth.mapper.UserMapper;
import com.ignaciorudyk.auth.repository.RefreshTokenRepository;
import com.ignaciorudyk.auth.repository.UserRepository;
import com.ignaciorudyk.auth.repository.dto.UserInfoDTO;
import com.ignaciorudyk.auth.repository.dto.request.UpdateMeRequest;
import com.ignaciorudyk.auth.repository.dto.request.UpdateUserRequest;
import com.ignaciorudyk.auth.repository.model.User;
import com.ignaciorudyk.auth.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserInfoDTO updateMe(Long userId, UpdateMeRequest request) {
        User user = findOrThrow(userId);
        setMeUserData(request, user);
        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserInfoDTO updateUser(Long userId, UpdateUserRequest request) {
        User user = findOrThrow(userId);
        setUserData(request, user);
        refreshTokenRepository.revokeAllUserTokens(user);
        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = findOrThrow(userId);
        user.setEnabled(false);
        userRepository.save(user);
        refreshTokenRepository.revokeAllUserTokens(user);
    }

    private User findOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con id " + id + " no encontrado"));
    }

    private void setUserData(UpdateUserRequest request, User user) {
        if (request.role()    != null) user.setRole(request.role());
        if (request.enabled() != null) user.setEnabled(request.enabled());
        setMeUserData(UpdateUserRequest.toUpdateMeRequest(request), user);
    }

    private void setMeUserData(UpdateMeRequest request, User user) {
        if (request.firstName() != null) user.setFirstName(request.firstName());
        if (request.lastName()  != null) user.setLastName(request.lastName());
        if (request.email()  != null) user.setEmail(request.email());
    }

}