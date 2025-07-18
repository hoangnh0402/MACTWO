package com.hit.userservice.mapper;

import com.hit.userservice.domain.dto.UserDto;
import com.hit.userservice.domain.dto.CreateUserRequestDto;
import com.hit.userservice.domain.dto.UpdateUserRequestDto;
import com.hit.userservice.domain.entity.Role;
import com.hit.userservice.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface UserMapper {

    @Mapping(source = "addressList", target = "addressList")
    @Mapping(source = "roles", target = "roles")
    UserDto toUserDto(User user);

    List<UserDto> toUserDtoList(List<User> userList);

    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addressList", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "accountStatus", ignore = true)
    @Mapping(target = "verificationToken", ignore = true)
    @Mapping(target = "verificationTokenExpiryTime", ignore = true)
    @Mapping(target = "otp", ignore = true)
    @Mapping(target = "otpExpiryTime", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUserEntity(CreateUserRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "addressList", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "accountStatus", ignore = true)
    @Mapping(target = "verificationToken", ignore = true)
    @Mapping(target = "verificationTokenExpiryTime", ignore = true)
    @Mapping(target = "otp", ignore = true)
    @Mapping(target = "otpExpiryTime", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateUserFromDto(UpdateUserRequestDto dto, @MappingTarget User user);

    default Set<String> mapRoles(Set<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }
}
