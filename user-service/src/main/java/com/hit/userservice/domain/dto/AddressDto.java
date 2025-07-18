package com.hit.userservice.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDto {
    private Integer id;
    private String street;
    private String ward;
    private String district;
    private String city;
    private boolean isDefault;
}
