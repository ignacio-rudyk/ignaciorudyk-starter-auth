package com.ignaciorudyk.auth.service;

import com.ignaciorudyk.auth.repository.dto.UserInfoDTO;
import com.ignaciorudyk.auth.repository.dto.request.UpdateMeRequest;
import com.ignaciorudyk.auth.repository.dto.request.UpdateUserRequest;

public interface UserService {

    UserInfoDTO updateMe(Long userId, UpdateMeRequest request);

    UserInfoDTO updateUser(Long userId, UpdateUserRequest request);

    void deleteUser(Long userId);

}
