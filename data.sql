-- =========================================================
-- 初始化数据 (使用 INSERT IGNORE 保证幂等：每次启动都执行不会重复，主键冲突自动跳过)
-- 兼容 application.yml 中 H2 的 MODE=MySQL 设置
-- =========================================================

-- 默认管理员 (密码: admin123)，使用 ON DUPLICATE KEY UPDATE 保证已存在的旧库密码也会被修正
INSERT INTO t_admin (id, username, password, real_name, role, status, deleted) VALUES
(1, 'admin', '$2a$10$Kfk3gaXsGKg/wEvuQ0SvSulX7OcQdDlm2KmphWwTx/9wCmvlKtnjq', '超级管理员', 'super', 1, 0)
ON DUPLICATE KEY UPDATE password = '$2a$10$Kfk3gaXsGKg/wEvuQ0SvSulX7OcQdDlm2KmphWwTx/9wCmvlKtnjq';

-- 默认演示用户 (密码: 123456)
INSERT IGNORE INTO t_user (id, username, password, real_name, phone, age, gender, role, accessibility_mode, status, deleted) VALUES
(1, 'elder', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '张大爷', '13800000001', 68, 1, 'elderly', 1, 1, 0),
(2, 'family', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '小张', '13800000002', 35, 1, 'family', 0, 1, 0);

-- 课程
INSERT IGNORE INTO t_course (id, title, category, description, difficulty, duration, status, sort_order, deleted) VALUES
(1, '认识电话诈骗', '电话诈骗', '教您识别常见的电话诈骗手段，保护财产安全', 1, 30, 1, 1, 0),
(2, '防范网络诈骗', '网络诈骗', '揭秘网络钓鱼、虚假购物等网络诈骗手法', 1, 45, 1, 2, 0),
(3, '警惕投资诈骗', '投资诈骗', '识别高回报投资陷阱，守护养老钱', 2, 40, 1, 3, 0),
(4, '识别冒充公检法诈骗', '冒充诈骗', '公检法不会电话要求转账，牢记这一点', 2, 35, 1, 4, 0),
(5, '短信与微信诈骗防范', '通讯诈骗', '不点陌生链接，不加陌生好友转账', 1, 25, 1, 5, 0);

-- 章节
INSERT IGNORE INTO t_chapter (id, course_id, title, content_rich, sort_order, deleted) VALUES
(1, 1, '什么是电话诈骗', '<h3>电话诈骗的定义</h3><p>电话诈骗是指犯罪分子通过电话拨打被害人的电话，虚构事实，使被害人产生错误认识，从而骗取钱财的行为。</p><p><strong>常见特征：</strong></p><ul><li>冒充公检法机关</li><li>声称您涉嫌犯罪</li><li>要求转账到"安全账户"</li></ul>', 1, 0),
(2, 1, '常见电话骗术揭秘', '<h3>典型电话诈骗场景</h3><p>1. <strong>冒充公检法：</strong>"我是XX公安局，你的银行账户涉嫌洗钱..."</p><p>2. <strong>冒充亲友：</strong>"爸/妈，我出事了，急需汇款..."</p><p>3. <strong>中奖诈骗：</strong>"恭喜您获得XX大奖，请先缴纳手续费..."</p>', 2, 0),
(3, 2, '网络钓鱼与虚假购物', '<h3>识别网络骗局</h3><p>点击不明链接可能导致银行卡信息被盗。</p><p>低价名牌商品要格外小心。</p>', 1, 0);

-- 案例
INSERT IGNORE INTO t_fraud_case (id, title, case_type, description, fraud_process, prevention_tips, views, status, deleted) VALUES
(1, '冒充孙子诈骗案', '冒充熟人', '犯罪分子冒充老人孙子的口吻打电话，称在学校打架需要急用钱',
 '第一步：拨打老人电话，称呼"爷爷/奶奶，是我(孙子小名)"\n第二步：谎称在学校与人发生冲突，急需"私了"费用\n第三步：催促老人不要告诉父母，要求立即转账',
 '1. 遇到自称亲友要求转账时，务必通过其他渠道核实身份\n2. 不急于操作，冷静确认\n3. 及时与家人沟通确认', 1250, 1, 0),
(2, '保健品诈骗案', '虚假宣传', '以免费体检为诱饵，向老人推销高价"保健品"，实为普通食品',
 '第一步：在社区发放免费体检邀请函\n第二步：组织"专家讲座"夸大健康威胁\n第三步：推销高价保健品，号称能治百病\n第四步：以限时优惠诱使老人大量购买',
 '1. 正规药品/保健品不会通过讲座销售\n2. 遇到"免费体检""专家义诊"要提高警惕\n3. 购买前咨询家人或社区医生', 890, 1, 0),
(3, '冒充公检法诈骗案', '冒充诈骗', '骗子自称检察官，威胁老人涉嫌洗钱将逮捕，要求转账"自证清白"',
 '第一步：使用改号软件伪装检察院号码\n第二步：称老人身份证被用于洗钱\n第三步：出示伪造"通缉令"\n第四步：要求转账到"安全账户"验收资金',
 '1. 公检法机关绝不会电话要求转账\n2. 不存在所谓"安全账户"\n3. 立即挂断，拨打110核实', 1580, 1, 0);

-- 评估题目
INSERT IGNORE INTO t_assessment_question (id, question, options_json, risk_level, category, deleted) VALUES
(1, '您是否经常接到陌生人的电话或短信？', '[{"label":"从不","value":0},{"label":"偶尔","value":1},{"label":"经常","value":3}]', 0, '接触频率', 0),
(2, '如果有人打电话自称是公安局，要求您转账，您会怎么做？', '[{"label":"立即转账","value":5},{"label":"先转账再确认","value":3},{"label":"挂断电话，自己拨打110确认","value":0}]', 0, '防范意识', 0),
(3, '您是否会在网上点击不明链接？', '[{"label":"从不点击","value":0},{"label":"看情况点击","value":2},{"label":"经常点击","value":4}]', 0, '网络习惯', 0),
(4, '如果有人告诉您有一个"稳赚不赔"的投资机会，您会？', '[{"label":"立即投资","value":5},{"label":"先少量试试","value":3},{"label":"不信，告诉家人","value":0}]', 0, '投资意识', 0),
(5, '您是否会向陌生网友提供自己的身份证号？', '[{"label":"绝对不会","value":0},{"label":"看情况","value":3},{"label":"会","value":5}]', 0, '隐私保护', 0);

-- 测试卷
INSERT IGNORE INTO t_quiz (id, title, description, total_questions, pass_score, status, deleted) VALUES
(1, '反诈知识小测试（一）', '检验您对常见诈骗手段的了解程度', 5, 60, 1, 0),
(2, '网络诈骗防范测试', '测试您对网络诈骗的识别能力', 3, 60, 1, 0);

-- 测试题目
INSERT IGNORE INTO t_quiz_question (id, quiz_id, question, options_json, correct_answer, explanation, sort_order, deleted) VALUES
(1, 1, '以下哪种情况最可能是诈骗？', '[{"label":"公安局来电要求到局里配合调查","key":"A"},{"label":"自称您孙子打电话要转账","key":"B"},{"label":"社区组织免费健康讲座","key":"C"},{"label":"银行发短信通知信用卡还款","key":"D"}]', 'B',
 '冒充亲友是老年人最常遇到的诈骗手段，遇到自称家人要求转账的情况一定要通过其他方式核实身份。', 1, 0),
(2, 1, '接到自称"检察院"的电话说您涉嫌洗钱，正确的做法是？', '[{"label":"按照对方指示操作转账","key":"A"},{"label":"挂断电话拨打110核实","key":"B"},{"label":"给对方提供银行卡信息","key":"C"},{"label":"加对方微信详细沟通","key":"D"}]', 'B',
 '公检法机关绝不会通过电话要求转账，也不会通过微信发送通缉令。正确做法是挂断电话后自行拨打110或前往当地公安局核实。', 2, 0),
(3, 1, '收到"恭喜中奖"短信要求先交手续费，应该？', '[{"label":"交钱领取","key":"A"},{"label":"直接忽略删除","key":"B"},{"label":"拨打短信电话询问","key":"C"}]', 'B',
 '所有"先交钱再领奖"的短信都是诈骗，直接删除即可。', 3, 0),
(4, 1, '老年人防范诈骗最有效的方法是？', '[{"label":"不给陌生人开门","key":"A"},{"label":"不轻信、不转账、多沟通","key":"B"},{"label":"不用手机","key":"C"}]', 'B',
 '"不轻信、不转账、多沟通"是防范诈骗的九字箴言，遇到任何可疑情况与家人沟通最重要。', 4, 0),
(5, 1, '以下哪个电话是需要警惕的诈骗电话？', '[{"label":"96110","key":"A"},{"label":"110","key":"B"},{"label":"+852开头的陌生来电","key":"C"},{"label":"120110","key":"D"}]', 'C',
 '96110是反诈专线，110是报警电话。境外+852等号码打来的陌生来电需高度警惕。', 5, 0),
(6, 2, '点击不明链接可能导致？', '[{"label":"手机中毒","key":"A"},{"label":"银行卡被盗","key":"B"},{"label":"以上都可能","key":"C"}]', 'C',
 '不明链接可能带木马、钓鱼页面，导致信息财产损失。', 1, 0),
(7, 2, '微信上陌生好友要求转账应该？', '[{"label":"直接转账","key":"A"},{"label":"电话或视频核实身份","key":"B"},{"label":"让对方再等等","key":"C"}]', 'B',
 '网络另一端身份难以确认，务必通过其他方式核实。', 2, 0),
(8, 2, '以下哪种购物网站最可靠？', '[{"label":"朋友发到群里的链接","key":"A"},{"label":"知名大平台官方APP","key":"B"},{"label":"弹出广告的网站","key":"C"}]', 'B',
 '选择知名大平台官方渠道，避免通过不明链接购物。', 3, 0);

-- 家庭绑定示例（可选）
INSERT IGNORE INTO t_family_relation (id, elderly_id, family_member_id, relation) VALUES
(1, 1, 2, '子女');
