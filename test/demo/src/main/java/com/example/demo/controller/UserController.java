package com.example.demo.controller;


import com.example.demo.domain.ApiResponse;
import com.example.demo.domain.dto.AccountsDto;
import com.example.demo.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("/api/users")
public class UserController {

    UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> save(@RequestBody AccountsDto accountsDto){
        if (accountsDto.getUsername() == null || accountsDto.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty!");
        }
        userService.save(accountsDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<String>builder()
                        .data(null)
                        .message("Created!")
                        .status(HttpStatus.CREATED.value())
                        .timestamp(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                                .format(LocalDateTime.now()))
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountsDto>> getById(@PathVariable("id") int id){
        AccountsDto accountsDto = userService.getById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<AccountsDto>builder()
                        .data(accountsDto)
                        .message("Get success")
                        .status(HttpStatus.OK.value())
                        .timestamp(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                                .format(LocalDateTime.now()))
                        .build());
    }

    //PathVariable là để sử dụng những điều duy nhất hoặc một vấn đề cụ thể
    //RequestParam là danh cho tìm một loạt như search hoặc phân trang, bộ lọc

    // Mặc định các requestParam là bắt buộc ta có the chỉnh lại là false required = false
    // @RequestParam(name = "username", required = false) String username nó sẽ hiểu là /by-username?username=xxx
    @GetMapping("/by-username")
    public ResponseEntity<ApiResponse<AccountsDto>> getByUsername(@RequestParam("username") String username){
        AccountsDto accountsDto = userService.getByUsername(username);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<AccountsDto>builder()
                        .data(accountsDto)
                        .message("Get success")
                        .status(HttpStatus.OK.value())
                        .timestamp(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                                .format(LocalDateTime.now()))
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable("id") int id){
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<String>builder()
                        .data(null)
                        .message("Deleted!")
                        .status(HttpStatus.OK.value())
                        .timestamp(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                                .format(LocalDateTime.now()))
                        .build());
    }


}
