package com.antifraud.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.antifraud.common.BusinessException;
import com.antifraud.common.Constants;
import com.antifraud.dto.LoginDTO;
import com.antifraud.dto.LoginResultDTO;
import com.antifraud.dto.RegisterDTO;
import com.antifraud.entity.User;
import com.antifraud.mapper.UserMapper;
import com.antifraud.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void register(RegisterDTO dto) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("用户名已存在");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setAge(dto.getAge());
        user.setGender(dto.getGender() != null ? dto.getGender() : 0);
        user.setRole(dto.getRole() != null ? dto.getRole() : Constants.ROLE_ELDERLY);
        user.setAccessibilityMode(1);
        user.setStatus(1);
        userMapper.insert(user);
    }

    public LoginResultDTO login(LoginDTO dto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        User user = userMapper.selectOne(wrapper);

        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getRole());

        LoginResultDTO result = new LoginResultDTO();
        result.setAccessToken(accessToken);
        result.setRefreshToken(refreshToken);

        LoginResultDTO.UserInfoDTO userInfo = new LoginResultDTO.UserInfoDTO();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setRealName(user.getRealName());
        userInfo.setPhone(user.getPhone());
        userInfo.setAge(user.getAge());
        userInfo.setGender(user.getGender());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setRole(user.getRole());
        userInfo.setAccessibilityMode(user.getAccessibilityMode());
        result.setUserInfo(userInfo);

        return result;
    }

    public User getById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return user;
    }

    public void updateProfile(Long userId, User updateUser) {
        User user = getById(userId);
        if (updateUser.getRealName() != null) user.setRealName(updateUser.getRealName());
        if (updateUser.getPhone() != null) user.setPhone(updateUser.getPhone());
        if (updateUser.getAge() != null) user.setAge(updateUser.getAge());
        if (updateUser.getGender() != null) user.setGender(updateUser.getGender());
        if (updateUser.getAvatar() != null) user.setAvatar(updateUser.getAvatar());
        if (updateUser.getAccessibilityMode() != null) user.setAccessibilityMode(updateUser.getAccessibilityMode());
        userMapper.updateById(user);
    }

    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = getById(userId);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);
    }
}
