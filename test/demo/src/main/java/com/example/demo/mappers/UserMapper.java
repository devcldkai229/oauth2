package com.example.demo.mappers;

import com.example.demo.domain.dto.AccountsDto;
import com.example.demo.domain.model.Accounts;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // dòng này chỉ sử dụng khi không dùng spring boot
    //UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    AccountsDto mapToDto(Accounts accounts);

    @JsonProperty("password") // trường hop gửi postman nhưng không nhận diện được username thì sử dụng cái này
    Accounts mapToEntity(AccountsDto accountsDto);

}
