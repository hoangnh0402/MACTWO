package com.hit.userservice.controller;

import com.hit.userservice.base.RestApiV1;
import com.hit.userservice.base.VsResponseUtil;
import com.hit.userservice.util.constant.UrlConstant;
import com.hit.userservice.domain.dto.UpdateUserRequestDto;
import com.hit.userservice.domain.dto.UserDto;
import com.hit.userservice.payload.PagedResponse;
import com.hit.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestApiV1
public class UserController {

    private final UserService userService;

    @Operation(summary = "Lấy danh sách người dùng", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(UrlConstant.User.GET_ALL)
    public ResponseEntity<?> getAllUsers(Pageable pageable) {
        PagedResponse<UserDto> response = userService.getAllUsers(pageable);
        return VsResponseUtil.ok(response);
    }

    @Operation(summary = "Lấy thông tin người dùng theo ID", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(UrlConstant.User.GET_BY_ID)
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        return VsResponseUtil.ok(userService.getUserById(id));
    }

    @Operation(summary = "Cập nhật thông tin người dùng", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(UrlConstant.User.UPDATE)
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @Valid @RequestBody UpdateUserRequestDto requestDto) {
        return VsResponseUtil.ok(userService.updateUser(id, requestDto));
    }

    @Operation(summary = "Xóa mềm người dùng", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(UrlConstant.User.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        return VsResponseUtil.ok(userService.deleteUser(id));
    }
}
