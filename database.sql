-- 老年人数字反诈教育平台 数据库建表脚本
-- MySQL 8.0

CREATE DATABASE IF NOT EXISTS anti_fraud_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE anti_fraud_platform;

-- =============================================
-- 1. 用户表
-- =============================================
DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码(BCrypt加密)',
    real_name VARCHAR(50) COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    age INT COMMENT '年龄',
    gender TINYINT DEFAULT 0 COMMENT '性别: 0未知 1男 2女',
    avatar VARCHAR(255) COMMENT '头像URL',
    role VARCHAR(20) NOT NULL DEFAULT 'elderly' COMMENT '角色: elderly/family/admin',
    accessibility_mode TINYINT DEFAULT 1 COMMENT '适老化模式: 0关闭 1开启',
    status TINYINT DEFAULT 1 COMMENT '状态: 0禁用 1启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) COMMENT '用户表';

-- =============================================
-- 2. 家庭关联表
-- =============================================
DROP TABLE IF EXISTS t_family_relation;
CREATE TABLE t_family_relation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    elderly_id BIGINT NOT NULL COMMENT '老人用户ID',
    family_member_id BIGINT NOT NULL COMMENT '家庭成员用户ID',
    relation VARCHAR(50) COMMENT '关系: 子女/配偶/孙辈等',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_elderly (elderly_id),
    INDEX idx_family (family_member_id)
) COMMENT '家庭关联表';

-- =============================================
-- 3. 课程表
-- =============================================
DROP TABLE IF EXISTS t_course;
CREATE TABLE t_course (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL COMMENT '课程标题',
    category VARCHAR(50) COMMENT '分类: 电话诈骗/网络诈骗/投资诈骗等',
    description TEXT COMMENT '课程描述',
    cover_image VARCHAR(255) COMMENT '封面图',
    difficulty TINYINT DEFAULT 1 COMMENT '难度: 1简单 2中等 3困难',
    duration INT DEFAULT 0 COMMENT '总时长(分钟)',
    status TINYINT DEFAULT 1 COMMENT '0下架 1上架',
    sort_order INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) COMMENT '课程表';

-- =============================================
-- 4. 章节表
-- =============================================
DROP TABLE IF EXISTS t_chapter;
CREATE TABLE t_chapter (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id BIGINT NOT NULL COMMENT '所属课程ID',
    title VARCHAR(200) NOT NULL COMMENT '章节标题',
    content_rich LONGTEXT COMMENT '富文本内容',
    video_url VARCHAR(500) COMMENT '视频URL',
    sort_order INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_course (course_id)
) COMMENT '章节表';

-- =============================================
-- 5. 学习进度表
-- =============================================
DROP TABLE IF EXISTS t_learning_progress;
CREATE TABLE t_learning_progress (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    chapter_id BIGINT NOT NULL,
    progress_percent INT DEFAULT 0 COMMENT '学习进度百分比',
    completed TINYINT DEFAULT 0 COMMENT '是否完成: 0否 1是',
    last_learn_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_course (user_id, course_id)
) COMMENT '学习进度表';

-- =============================================
-- 6. 诈骗案例表
-- =============================================
DROP TABLE IF EXISTS t_fraud_case;
CREATE TABLE t_fraud_case (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL COMMENT '案例标题',
    case_type VARCHAR(50) COMMENT '诈骗类型',
    description TEXT COMMENT '案例简述',
    fraud_process TEXT COMMENT '诈骗过程',
    prevention_tips TEXT COMMENT '防范提示',
    cover_image VARCHAR(255),
    views INT DEFAULT 0 COMMENT '浏览量',
    status TINYINT DEFAULT 1 COMMENT '0隐藏 1显示',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) COMMENT '诈骗案例表';

-- =============================================
-- 7. 评估题目表
-- =============================================
DROP TABLE IF EXISTS t_assessment_question;
CREATE TABLE t_assessment_question (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question VARCHAR(500) NOT NULL COMMENT '题目内容',
    options_json TEXT COMMENT '选项JSON',
    risk_level INT DEFAULT 0 COMMENT '风险分值',
    category VARCHAR(50) COMMENT '分类',
    deleted TINYINT DEFAULT 0
) COMMENT '评估题目表';

-- =============================================
-- 8. 评估记录表
-- =============================================
DROP TABLE IF EXISTS t_assessment_record;
CREATE TABLE t_assessment_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    score INT NOT NULL COMMENT '评估分数',
    risk_level VARCHAR(20) COMMENT '风险等级: low/medium/high',
    suggestions TEXT COMMENT '防范建议',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_id)
) COMMENT '评估记录表';

-- =============================================
-- 9. 预警推送表
-- =============================================
DROP TABLE IF EXISTS t_alert;
CREATE TABLE t_alert (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT COMMENT '预警内容',
    alert_type VARCHAR(20) COMMENT '类型: system/manual/risk',
    is_read TINYINT DEFAULT 0 COMMENT '0未读 1已读',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_read (user_id, is_read)
) COMMENT '预警推送表';

-- =============================================
-- 10. 测试卷表
-- =============================================
DROP TABLE IF EXISTS t_quiz;
CREATE TABLE t_quiz (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    total_questions INT DEFAULT 0,
    pass_score INT DEFAULT 60 COMMENT '及格分数线',
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) COMMENT '测试卷表';

-- =============================================
-- 11. 测试题目表
-- =============================================
DROP TABLE IF EXISTS t_quiz_question;
CREATE TABLE t_quiz_question (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    quiz_id BIGINT NOT NULL,
    question VARCHAR(500) NOT NULL,
    options_json TEXT COMMENT '选项JSON',
    correct_answer VARCHAR(10) COMMENT '正确答案',
    explanation TEXT COMMENT '答案解析',
    sort_order INT DEFAULT 0,
    deleted TINYINT DEFAULT 0,
    INDEX idx_quiz (quiz_id)
) COMMENT '测试题目表';

-- =============================================
-- 12. 测试记录表
-- =============================================
DROP TABLE IF EXISTS t_quiz_record;
CREATE TABLE t_quiz_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    quiz_id BIGINT NOT NULL,
    score INT COMMENT '得分',
    answers_json TEXT COMMENT '答题详情JSON',
    passed TINYINT COMMENT '是否通过',
    time_spent INT COMMENT '用时(秒)',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_id)
) COMMENT '测试记录表';

-- =============================================
-- 13. 管理员表
-- =============================================
DROP TABLE IF EXISTS t_admin;
CREATE TABLE t_admin (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    real_name VARCHAR(50),
    role VARCHAR(20) DEFAULT 'content' COMMENT 'super/content/reviewer',
    permissions TEXT COMMENT '权限JSON',
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) COMMENT '管理员表';

-- =============================================
-- 14. 操作日志表
-- =============================================
DROP TABLE IF EXISTS t_operation_log;
CREATE TABLE t_operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    admin_id BIGINT NOT NULL,
    action VARCHAR(100) COMMENT '操作类型',
    target VARCHAR(100) COMMENT '操作对象',
    detail TEXT COMMENT '操作详情',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_admin (admin_id)
) COMMENT '操作日志表';

-- =============================================
-- 初始化数据
-- =============================================

-- 默认管理员 (密码: admin123, BCrypt加密)
INSERT INTO t_admin (username, password, real_name, role, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '超级管理员', 'super', 1);

-- 示例课程
INSERT INTO t_course (title, category, description, difficulty, duration, status, sort_order) VALUES
('认识电话诈骗', '电话诈骗', '教您识别常见的电话诈骗手段，保护财产安全', 1, 30, 1, 1),
('防范网络诈骗', '网络诈骗', '揭秘网络钓鱼、虚假购物等网络诈骗手法', 1, 45, 1, 2),
('警惕投资诈骗', '投资诈骗', '识别高回报投资陷阱，守护养老钱', 2, 40, 1, 3),
('识别冒充公检法诈骗', '冒充诈骗', '公检法不会电话要求转账，牢记这一点', 2, 35, 1, 4),
('短信与微信诈骗防范', '通讯诈骗', '不点陌生链接，不加陌生好友转账', 1, 25, 1, 5);

-- 示例章节
INSERT INTO t_chapter (course_id, title, content_rich, sort_order) VALUES
(1, '什么是电话诈骗', '<h3>电话诈骗的定义</h3><p>电话诈骗是指犯罪分子通过电话拨打被害人的电话，虚构事实，使被害人产生错误认识，从而骗取钱财的行为。</p><p><strong>常见特征：</strong></p><ul><li>冒充公检法机关</li><li>声称您涉嫌犯罪</li><li>要求转账到"安全账户"</li></ul>', 1),
(1, '常见电话骗术揭秘', '<h3>典型电话诈骗场景</h3><p>1. <strong>冒充公检法：</strong>"我是XX公安局，你的银行账户涉嫌洗钱..."</p><p>2. <strong>冒充亲友：</strong>"爸/妈，我出事了，急需汇款..."</p><p>3. <strong>中奖诈骗：</strong>"恭喜您获得XX大奖，请先缴纳手续费..."</p>', 2);

-- 示例案例
INSERT INTO t_fraud_case (title, case_type, description, fraud_process, prevention_tips, views, status) VALUES
('冒充孙子诈骗案', '冒充熟人', '犯罪分子冒充老人孙子的口吻打电话，称在学校打架需要急用钱', '第一步：拨打老人电话，称呼"爷爷/奶奶，是我(孙子小名)"\n第二步：谎称在学校与人发生冲突，急需"私了"费用\n第三步：催促老人不要告诉父母，要求立即转账', '1. 遇到自称亲友要求转账时，务必通过其他渠道核实身份\n2. 不急于操作，冷静确认\n3. 及时与家人沟通确认', 1250, 1),
('保健品诈骗案', '虚假宣传', '以免费体检为诱饵，向老人推销高价"保健品"，实为普通食品', '第一步：在社区发放免费体检邀请函\n第二步：组织"专家讲座"夸大健康威胁\n第三步：推销高价保健品，号称能治百病\n第四步：以限时优惠诱使老人大量购买', '1. 正规药品/保健品不会通过讲座销售\n2. 遇到"免费体检""专家义诊"要提高警惕\n3. 购买前咨询家人或社区医生', 890, 1);

-- 示例评估题目
INSERT INTO t_assessment_question (question, options_json, risk_level, category) VALUES
('您是否经常接到陌生人的电话或短信？', '[{"label":"从不","value":0},{"label":"偶尔","value":1},{"label":"经常","value":3}]', 0, '接触频率'),
('如果有人打电话自称是公安局，要求您转账，您会怎么做？', '[{"label":"立即转账","value":5},{"label":"先转账再确认","value":3},{"label":"挂断电话，自己拨打110确认","value":0}]', 0, '防范意识'),
('您是否会在网上点击不明链接？', '[{"label":"从不点击","value":0},{"label":"看情况点击","value":2},{"label":"经常点击","value":4}]', 0, '网络习惯'),
('如果有人告诉您有一个"稳赚不赔"的投资机会，您会？', '[{"label":"立即投资","value":5},{"label":"先少量试试","value":3},{"label":"不信，告诉家人","value":0}]', 0, '投资意识');

-- 示例测试卷
INSERT INTO t_quiz (title, description, total_questions, pass_score, status) VALUES
('反诈知识小测试（一）', '检验您对常见诈骗手段的了解程度', 5, 60, 1);

INSERT INTO t_quiz_question (quiz_id, question, options_json, correct_answer, explanation, sort_order) VALUES
(1, '以下哪种情况最可能是诈骗？', '[{"label":"公安局来电要求到局里配合调查","key":"A"},{"label":"自称您孙子打电话要转账","key":"B"},{"label":"社区组织免费健康讲座","key":"C"},{"label":"银行发短信通知信用卡还款","key":"D"}]', 'B', '冒充亲友是老年人最常遇到的诈骗手段，遇到自称家人要求转账的情况一定要通过其他方式核实身份。', 1),
(1, '接到自称"检察院"的电话说您涉嫌洗钱，正确的做法是？', '[{"label":"按照对方指示操作转账","key":"A"},{"label":"挂断电话拨打110核实","key":"B"},{"label":"给对方提供银行卡信息","key":"C"},{"label":"加对方微信详细沟通","key":"D"}]', 'B', '公检法机关绝不会通过电话要求转账，也不会通过微信发送通缉令。正确做法是挂断电话后自行拨打110或前往当地公安局核实。', 2);
