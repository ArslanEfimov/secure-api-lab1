package com.example.secure_api_lab1.controller;

import com.example.secure_api_lab1.dto.AuthRequestDTO;
import com.example.secure_api_lab1.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("secure-api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("sign-up")
    public ResponseEntity<?> register(@RequestBody @Valid AuthRequestDTO authRequestDTO){
        return userService.register(authRequestDTO);
    }

    @PostMapping("sign-in")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO authRequestDTO){

        return userService.login(authRequestDTO);

    }
}
