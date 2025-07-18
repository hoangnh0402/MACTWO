package com.hit.userservice.mapper;

import com.hit.userservice.domain.dto.AddressDto;
import com.hit.userservice.domain.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDto toAddressDto(Address address);
}
