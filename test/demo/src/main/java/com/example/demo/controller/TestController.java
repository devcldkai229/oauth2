package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "Public Endpoint - Ai cũng truy cập được!";
    }

    @GetMapping("/user")
    public String userEndpoint() {
        return "User Endpoint - Chỉ user đã đăng nhập!";
    }

    @GetMapping("/admin")
    public String adminEndpoint() {
        return "Admin Endpoint - Chỉ admin mới vào được!";
    }


    @GetMapping("/read")
    public String readEndpoint() {
        return "READ Endpoint - Chỉ có READ mới vào được!";
    }

    @GetMapping("/write")
    public String writeEndpoint() {
        return "Write Endpoint - Chỉ write mới vào được!";
    }

    @GetMapping("/readwrite")
    public String readWriteEndpoint() {
        return "Read write Endpoint - Chỉ read write mới vào được!";
    }

    @GetMapping("/marketplace")
    public String marketplaceEndpoint() {
        return "Nơi mua bán";
    }
}
