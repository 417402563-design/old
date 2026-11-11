package com.antifraud.common;

public class Constants {

    // 用户角色
    public static final String ROLE_ELDERLY = "elderly";
    public static final String ROLE_FAMILY = "family";
    public static final String ROLE_ADMIN = "admin";

    // JWT
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_ROLE = "role";

    // 风险等级
    public static final String RISK_LOW = "low";
    public static final String RISK_MEDIUM = "medium";
    public static final String RISK_HIGH = "high";

    // 通用状态
    public static final Integer STATUS_ENABLED = 1;
    public static final Integer STATUS_DISABLED = 0;
}
