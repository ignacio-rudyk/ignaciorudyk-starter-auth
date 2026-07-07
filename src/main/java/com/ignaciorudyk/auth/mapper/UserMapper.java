package com.ignaciorudyk.auth.mapper;

import com.ignaciorudyk.auth.repository.dto.UserInfoDTO;
import com.ignaciorudyk.auth.repository.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserInfoDTO toDTO(User user);

}