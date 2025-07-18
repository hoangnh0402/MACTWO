package com.hit.userservice.domain.dto;

import com.hit.userservice.domain.entity.AccountStatus;
import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class UserDto {
    private Integer id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private AccountStatus accountStatus;
    private Set<String> roles;
    private List<AddressDto> addressList;
}
