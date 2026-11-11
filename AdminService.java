package com.antifraud.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.antifraud.common.BusinessException;
import com.antifraud.common.PageResult;
import com.antifraud.dto.LoginDTO;
import com.antifraud.dto.LoginResultDTO;
import com.antifraud.entity.Admin;
import com.antifraud.entity.OperationLog;
import com.antifraud.entity.User;
import com.antifraud.mapper.AdminMapper;
import com.antifraud.mapper.OperationLogMapper;
import com.antifraud.mapper.UserMapper;
import com.antifraud.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginResultDTO adminLogin(LoginDTO dto) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, dto.getUsername());
        Admin admin = adminMapper.selectOne(wrapper);

        if (admin == null || !passwordEncoder.matches(dto.getPassword(), admin.getPassword())) {
            throw new BusinessException("管理员用户名或密码错误");
        }
        if (admin.getStatus() == 0) {
            throw new BusinessException("管理员账号已被禁用");
        }

        String accessToken = jwtUtil.generateAccessToken(admin.getId(), "admin_" + admin.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(admin.getId(), "admin_" + admin.getRole());

        LoginResultDTO result = new LoginResultDTO();
        result.setAccessToken(accessToken);
        result.setRefreshToken(refreshToken);

        // 返回管理员身份信息，role 固定为 "admin"，供前端路由守卫判断（startsWith('admin')）
        LoginResultDTO.UserInfoDTO userInfo = new LoginResultDTO.UserInfoDTO();
        userInfo.setId(admin.getId());
        userInfo.setUsername(admin.getUsername());
        userInfo.setRealName(admin.getRealName());
        userInfo.setRole("admin");
        result.setUserInfo(userInfo);

        return result;
    }

    public void logOperation(Long adminId, String action, String target, String detail) {
        OperationLog log = new OperationLog();
        log.setAdminId(adminId);
        log.setAction(action);
        log.setTarget(target);
        log.setDetail(detail);
        operationLogMapper.insert(log);
    }

    public PageResult<OperationLog> getOperationLogs(int page, int size, Long adminId) {
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        if (adminId != null) {
            wrapper.eq(OperationLog::getAdminId, adminId);
        }
        wrapper.orderByDesc(OperationLog::getCreatedAt);
        Page<OperationLog> pageResult = operationLogMapper.selectPage(new Page<>(page, size), wrapper);
        return new PageResult<>(pageResult.getRecords(), pageResult.getTotal(), pageResult.getPages(), pageResult.getCurrent(), pageResult.getSize());
    }

    public PageResult<User> listUsers(int page, int size, String role) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (role != null && !role.isEmpty()) {
            wrapper.eq(User::getRole, role);
        }
        wrapper.orderByDesc(User::getCreateTime);
        Page<User> pageResult = userMapper.selectPage(new Page<>(page, size), wrapper);
        return new PageResult<>(pageResult.getRecords(), pageResult.getTotal(), pageResult.getPages(), pageResult.getCurrent(), pageResult.getSize());
    }

    public void toggleUserStatus(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException("用户不存在");
        user.setStatus(user.getStatus() == 1 ? 0 : 1);
        userMapper.updateById(user);
    }
}
