package com.example.demo.mappers;

import com.example.demo.domain.dto.AccountsDto;
import com.example.demo.domain.model.Accounts;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public AccountsDto mapToDto(Accounts accounts) {
        if ( accounts == null ) {
            return null;
        }

        AccountsDto accountsDto = new AccountsDto();

        accountsDto.username = accounts.getUsername();
        accountsDto.password = accounts.getPassword();
        accountsDto.email = accounts.getEmail();

        return accountsDto;
    }

    @Override
    public Accounts mapToEntity(AccountsDto accountsDto) {
        if ( accountsDto == null ) {
            return null;
        }

        Accounts accounts = new Accounts();

        accounts.setUsername( accountsDto.username );
        accounts.setPassword( accountsDto.password );
        accounts.setEmail( accountsDto.email );

        return accounts;
    }
}
