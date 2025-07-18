package com.hit.userservice.service.impl;

import com.hit.userservice.domain.dto.UpdateUserRequestDto;
import com.hit.userservice.domain.dto.UserDto;
import com.hit.userservice.domain.entity.AccountStatus;
import com.hit.userservice.domain.entity.User;
import com.hit.userservice.exception.BadRequestException;
import com.hit.userservice.exception.ResourceNotFoundException;
import com.hit.userservice.mapper.UserMapper;
import com.hit.userservice.payload.ApiResponse;
import com.hit.userservice.payload.PagedResponse;
import com.hit.userservice.repository.UserRepository;
import com.hit.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public PagedResponse<UserDto> getAllUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        List<UserDto> userDtoList = userMapper.toUserDtoList(userPage.getContent());
        return new PagedResponse<>(userDtoList, userPage.getNumber(), userPage.getSize(),
                userPage.getTotalElements(), userPage.getTotalPages(), userPage.isLast());
    }

    @Override
    @Cacheable(value = "userCache", key = "#userId")
    public UserDto getUserById(Integer userId) {
        log.info("Đang truy vấn CSDL để lấy user ID: {}", userId);
        User user = findUserById(userId);
        return userMapper.toUserDto(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = "userCache", key = "#userId")
    public UserDto updateUser(Integer userId, UpdateUserRequestDto requestDto) {
        log.info("Xóa cache cho user ID: {}", userId);
        User user = findUserById(userId);
        userMapper.updateUserFromDto(requestDto, user);
        User updatedUser = userRepository.save(user);
        log.info("Cập nhật thành công người dùng: {}", user.getEmail());
        return userMapper.toUserDto(updatedUser);
    }

    @Override
    @Transactional
    @CacheEvict(value = "userCache", key = "#userId")
    public ApiResponse deleteUser(Integer userId) {
        User user = findUserById(userId);
        if (user.getAccountStatus() == AccountStatus.DELETED) {
            throw new BadRequestException("Tài khoản này đã được xóa trước đó.");
        }
        user.setAccountStatus(AccountStatus.DELETED);
        userRepository.save(user);
        log.info("Đã xóa mềm người dùng ID: {}", userId);
        return new ApiResponse(true, "Người dùng đã được xóa thành công.");
    }

    private User findUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }
}
