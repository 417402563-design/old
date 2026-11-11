package com.antifraud.dto;

import lombok.Data;
import java.util.List;

@Data
public class LoginResultDTO {
    private String accessToken;
    private String refreshToken;
    private UserInfoDTO userInfo;

    @Data
    public static class UserInfoDTO {
        private Long id;
        private String username;
        private String realName;
        private String phone;
        private Integer age;
        private Integer gender;
        private String avatar;
        private String role;
        private Integer accessibilityMode;
    }
}
