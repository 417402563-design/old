package com.antifraud.controller;

import com.antifraud.common.Result;
import com.antifraud.dto.LoginDTO;
import com.antifraud.dto.LoginResultDTO;
import com.antifraud.dto.RegisterDTO;
import com.antifraud.entity.User;
import com.antifraud.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody RegisterDTO dto) {
        userService.register(dto);
        return Result.success("注册成功", null);
    }

    @PostMapping("/login")
    public Result<LoginResultDTO> login(@Valid @RequestBody LoginDTO dto) {
        LoginResultDTO result = userService.login(dto);
        return Result.success("登录成功", result);
    }
}
