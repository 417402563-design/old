-- =========================================================
-- 老年人数字反诈教育平台 - H2 (MySQL模式) 建表脚本
-- 每次启动都会执行；使用 CREATE TABLE IF NOT EXISTS 保证幂等
-- =========================================================

-- 1. 用户表
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    real_name VARCHAR(50),
    phone VARCHAR(20),
    age INT,
    gender TINYINT DEFAULT 0,
    avatar VARCHAR(255),
    role VARCHAR(20) NOT NULL DEFAULT 'elderly',
    accessibility_mode TINYINT DEFAULT 1,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

-- 2. 家庭关联表
CREATE TABLE IF NOT EXISTS t_family_relation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    elderly_id BIGINT NOT NULL,
    family_member_id BIGINT NOT NULL,
    relation VARCHAR(50),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_fr_elderly ON t_family_relation(elderly_id);
CREATE INDEX IF NOT EXISTS idx_fr_family ON t_family_relation(family_member_id);

-- 3. 课程表
CREATE TABLE IF NOT EXISTS t_course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    category VARCHAR(50),
    description TEXT,
    cover_image VARCHAR(255),
    difficulty TINYINT DEFAULT 1,
    duration INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    sort_order INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

-- 4. 章节表
CREATE TABLE IF NOT EXISTS t_chapter (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content_rich CLOB,
    video_url VARCHAR(500),
    sort_order INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);
CREATE INDEX IF NOT EXISTS idx_chapter_course ON t_chapter(course_id);

-- 5. 学习进度表
CREATE TABLE IF NOT EXISTS t_learning_progress (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    chapter_id BIGINT NOT NULL,
    progress_percent INT DEFAULT 0,
    completed TINYINT DEFAULT 0,
    last_learn_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_lp_user_course ON t_learning_progress(user_id, course_id);

-- 6. 诈骗案例表
CREATE TABLE IF NOT EXISTS t_fraud_case (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    case_type VARCHAR(50),
    description CLOB,
    fraud_process CLOB,
    prevention_tips CLOB,
    cover_image VARCHAR(255),
    views INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

-- 7. 评估题目表
CREATE TABLE IF NOT EXISTS t_assessment_question (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question VARCHAR(500) NOT NULL,
    options_json CLOB,
    risk_level INT DEFAULT 0,
    category VARCHAR(50),
    deleted TINYINT DEFAULT 0
);

-- 8. 评估记录表
CREATE TABLE IF NOT EXISTS t_assessment_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    score INT NOT NULL,
    risk_level VARCHAR(20),
    suggestions CLOB,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_ar_user ON t_assessment_record(user_id);

-- 9. 预警推送表
CREATE TABLE IF NOT EXISTS t_alert (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content CLOB,
    alert_type VARCHAR(20),
    is_read TINYINT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_alert_user_read ON t_alert(user_id, is_read);

-- 10. 测试卷表
CREATE TABLE IF NOT EXISTS t_quiz (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description CLOB,
    total_questions INT DEFAULT 0,
    pass_score INT DEFAULT 60,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

-- 11. 测试题目表
CREATE TABLE IF NOT EXISTS t_quiz_question (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quiz_id BIGINT NOT NULL,
    question VARCHAR(500) NOT NULL,
    options_json CLOB,
    correct_answer VARCHAR(10),
    explanation CLOB,
    sort_order INT DEFAULT 0,
    deleted TINYINT DEFAULT 0
);
CREATE INDEX IF NOT EXISTS idx_qq_quiz ON t_quiz_question(quiz_id);

-- 12. 测试记录表
CREATE TABLE IF NOT EXISTS t_quiz_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    quiz_id BIGINT NOT NULL,
    score INT,
    answers_json CLOB,
    passed TINYINT,
    time_spent INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_qr_user ON t_quiz_record(user_id);

-- 13. 管理员表
CREATE TABLE IF NOT EXISTS t_admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    real_name VARCHAR(50),
    role VARCHAR(20) DEFAULT 'content',
    permissions CLOB,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

-- 14. 操作日志表
CREATE TABLE IF NOT EXISTS t_operation_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    admin_id BIGINT NOT NULL,
    action VARCHAR(100),
    target VARCHAR(100),
    detail CLOB,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_ol_admin ON t_operation_log(admin_id);
