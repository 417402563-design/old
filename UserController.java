package com.antifraud.controller;

import com.antifraud.common.Result;
import com.antifraud.entity.User;
import com.antifraud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public Result<User> getProfile(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userService.getById(userId);
        user.setPassword(null); // 不返回密码
        return Result.success(user);
    }

    @PutMapping("/profile")
    public Result<?> updateProfile(HttpServletRequest request, @RequestBody User updateUser) {
        Long userId = (Long) request.getAttribute("userId");
        userService.updateProfile(userId, updateUser);
        return Result.success("更新成功", null);
    }

    @PutMapping("/password")
    public Result<?> changePassword(HttpServletRequest request, @RequestBody Map<String, String> body) {
        Long userId = (Long) request.getAttribute("userId");
        userService.changePassword(userId, body.get("oldPassword"), body.get("newPassword"));
        return Result.success("密码修改成功", null);
    }

    @PutMapping("/accessibility")
    public Result<?> toggleAccessibility(HttpServletRequest request, @RequestBody Map<String, Integer> body) {
        Long userId = (Long) request.getAttribute("userId");
        User updateUser = new User();
        updateUser.setAccessibilityMode(body.get("mode"));
        userService.updateProfile(userId, updateUser);
        return Result.success("设置已更新", null);
    }
}
