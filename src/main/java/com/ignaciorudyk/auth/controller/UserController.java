package com.ignaciorudyk.auth.controller;

import com.ignaciorudyk.auth.repository.dto.UserInfoDTO;
import com.ignaciorudyk.auth.repository.dto.request.UpdateMeRequest;
import com.ignaciorudyk.auth.repository.dto.request.UpdateUserRequest;
import com.ignaciorudyk.auth.repository.dto.response.base.ResponseDTO;
import com.ignaciorudyk.auth.service.UserService;
import com.ignaciorudyk.auth.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/me")
    public ResponseEntity<ResponseDTO> updateMe(HttpServletRequest httpServletRequest,
                                                @AuthenticationPrincipal UserInfoDTO user,
                                                @Valid @RequestBody UpdateMeRequest updateMeRequest) {
        LOGGER.info("Llamado al servicio update-me - User email: {}", updateMeRequest.email());
        return HttpUtil.isSucceful2xxResponse(httpServletRequest, userService.updateMe(user.id(), updateMeRequest));
    }

    @PatchMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public  ResponseEntity<ResponseDTO> updateUser(HttpServletRequest httpServletRequest,
                                                   @PathVariable Long id,
                                                   @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        LOGGER.info("Llamado al servicio update-user - User id: {}", id);
        return HttpUtil.isSucceful2xxResponse(httpServletRequest, userService.updateUser(id, updateUserRequest));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public  ResponseEntity<ResponseDTO> deleteUser(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        LOGGER.info("Llamado al servicio delete-user - User id: {}", id);
        userService.deleteUser(id);
        return HttpUtil.isSucceful2xxResponse(httpServletRequest, null);
    }
}