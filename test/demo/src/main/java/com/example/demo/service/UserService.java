package com.example.demo.service;

import com.example.demo.domain.dto.AccountsDto;
import com.example.demo.domain.model.Accounts;
import com.example.demo.mappers.UserMapper;
import com.example.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService {

    UserRepository userRepository;

    UserMapper userMapper;

    public void save(AccountsDto accountsDto) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        log.debug("{} {}", accountsDto.getUsername(), accountsDto.getPassword());
        Accounts accounts = userMapper.mapToEntity(accountsDto);
        accounts.setPassword(passwordEncoder.encode(accounts.getPassword()));
        userRepository.save(accounts);
    }

    public AccountsDto getById(int id){
        Accounts accounts = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found"
        ));
        return userMapper.mapToDto(accounts);
    }

    public AccountsDto getByUsername(String username){
        Accounts accounts = userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("User not found"
        ));
        return userMapper.mapToDto(accounts);    }

    public void delete(int id){
        userRepository.deleteById(id);
    }


}
