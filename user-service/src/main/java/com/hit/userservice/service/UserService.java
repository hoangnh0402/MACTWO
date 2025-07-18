package com.hit.userservice.service;

import com.hit.userservice.domain.dto.*;
import com.hit.userservice.payload.*;
import org.springframework.data.domain.Pageable;

public interface UserService {
    PagedResponse<UserDto> getAllUsers(Pageable pageable);
    UserDto getUserById(Integer userId);
    UserDto updateUser(Integer userId, UpdateUserRequestDto requestDto);
    ApiResponse deleteUser(Integer userId);
}
