SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ts_ai_config
-- ----------------------------
DROP TABLE IF EXISTS `ts_ai_config`;
CREATE TABLE `ts_ai_config`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `config_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT '' COMMENT '配置类型',
  `server_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT '' COMMENT '服务器URL',
  `auth_config` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT '' COMMENT '授权配置',
  `proxy_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT '' COMMENT '代理类型',
  `proxy_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT '' COMMENT '代理主机',
  `proxy_port` int(10) NOT NULL DEFAULT 0 COMMENT '代理端口',
  `key_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT '' COMMENT '密钥名称',
  `status` int(2) NOT NULL DEFAULT 0 COMMENT '数据状态',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_520_ci COMMENT = 'ai应用配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ts_ai_config
-- ----------------------------
INSERT INTO `ts_ai_config` VALUES (1, 'chat-xunfei', 'https://spark-api.xf-yun.com/v2.1/chat', '', '', '', 0, '对话-讯飞-星火模型-V2', 1, '2023-11-13 14:39:43', '2023-11-13 14:39:45');
INSERT INTO `ts_ai_config` VALUES (2, 'chat-baidu', 'https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions', '', '', '', 0, '对话-百度-ERNIE Bot模型', 1, '2023-11-15 14:40:39', '2023-11-15 14:40:41');
INSERT INTO `ts_ai_config` VALUES (3, 'chat-aliyun', 'https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation', '', '', '', 0, '对话-阿里云-qwen turbo模型', 1, '2023-11-15 18:18:29', '2023-11-15 18:18:33');
INSERT INTO `ts_ai_config` VALUES (4, 'txt2img-baidu', 'https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/text2image/sd_xl', '', '', '', 0, '文生图-百度-Stable Diffusion XL', 1, '2023-11-20 17:27:48', '2023-11-20 17:27:50');
INSERT INTO `ts_ai_config` VALUES (5, 'chat-skylark', 'https://maas-api.ml-platform-cn-beijing.volces.com', '', '', '', 0, '对话-字节-云雀大规模预训练语言模型', 1, '2023-12-13 22:17:58', '2023-12-13 22:18:01');
INSERT INTO `ts_ai_config` VALUES (6, 'chat-baichuan', 'https://api.baichuan-ai.com/v1/chat/completions', '', '', '', 0, '对话-百川大模型', 1, '2023-12-13 22:17:58', '2023-12-13 22:18:01');

-- ----------------------------
-- Table structure for ts_ai_conversation
-- ----------------------------
DROP TABLE IF EXISTS `ts_ai_conversation`;
CREATE TABLE `ts_ai_conversation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `conversation_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NOT NULL COMMENT '会话ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `robot_id` bigint(20) NOT NULL COMMENT '机器人ID',
  `conversation_title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '会话标题',
  `status` int(2) NULL DEFAULT 1 COMMENT '状态 0：禁用 1：可用 2：删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `c_index`(`conversation_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_520_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ts_ai_conversation
-- ----------------------------

-- ----------------------------
-- Table structure for ts_ai_conversation_history
-- ----------------------------
DROP TABLE IF EXISTS `ts_ai_conversation_history`;
CREATE TABLE `ts_ai_conversation_history`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建用户ID',
  `conversation_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT NULL COMMENT '会话ID',
  `user_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL COMMENT '用户内容',
  `ai_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL COMMENT 'AI内容',
  `status` int(2) NULL DEFAULT 1 COMMENT '状态 0：禁用 1：可用 2：删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_520_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ts_ai_conversation_history
-- ----------------------------

-- ----------------------------
-- Table structure for ts_ai_image_task
-- ----------------------------
DROP TABLE IF EXISTS `ts_ai_image_task`;
CREATE TABLE `ts_ai_image_task`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `task_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NOT NULL COMMENT '任务ID',
  `config_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '配置类型：txt2img-baidu',
  `prompt` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '提示词：文本 || url （目前只有文生图未来可能支持图生图）',
  `generate_num` int(2) NULL DEFAULT NULL COMMENT '生成的数量',
  `image_size` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '生成的尺寸',
  `generate_data_url` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '生成结果-url',
  `generate_data_base64` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL COMMENT '生成结果-base64',
  `status` int(2) NULL DEFAULT NULL COMMENT '状态 0：待完成 1：已完成 2：错误',
  `error_msg` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT NULL COMMENT '错误提示',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_520_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ts_ai_image_task
-- ----------------------------

-- ----------------------------
-- Table structure for ts_ai_robot
-- ----------------------------
DROP TABLE IF EXISTS `ts_ai_robot`;
CREATE TABLE `ts_ai_robot`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `config_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '配置类型',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '机器人名称',
  `description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '机器人描述',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '机器人头像',
  `system_content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '系统人设',
  `robot_type` int(2) NULL DEFAULT NULL COMMENT '机器人类型 1: 聊天机器人 2: 生图机器人',
  `status` int(2) NULL DEFAULT 1 COMMENT '状态 0：禁用 1：可用',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_520_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ts_ai_robot
-- ----------------------------
INSERT INTO `ts_ai_robot` VALUES (1, 'chat-xunfei', '星火大模型', '讯飞星火认知大模型是讯飞研发大语言模型，可以通过自然语言交互的方式，为您提供包括语言理解、问答、推理等各类认知智能服务。', 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif', '这是一段文字描述，用于给模型提供上下文，此版本未用到', 1, 1, '2023-12-04 17:56:28', '2023-12-04 17:56:31');
INSERT INTO `ts_ai_robot` VALUES (2, 'chat-baidu', '文心一言', '文心一言是百度自行研发的大语言模型，覆盖海量中文数据，具有更强的对话问答、内容创作生成等能力，响应速度更快。', 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif', '这是一段文字描述，用于给模型提供上下文，此版本未用到', 1, 1, '2023-12-04 17:56:28', '2023-12-04 17:56:31');
INSERT INTO `ts_ai_robot` VALUES (3, 'chat-aliyun', '通义千问', '通义千问是阿里云自主研发的大语言模型，能够在用户自然语言输入的基础上，通过自然语言理解和语义分析，理解用户意图，在不同领域、任务内为用户提供服务和帮助。', 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif', '这是一段文字描述，用于给模型提供上下文，此版本未用到', 1, 1, '2023-12-04 17:56:28', '2023-12-04 17:56:31');
INSERT INTO `ts_ai_robot` VALUES (4, 'chat-skylark', '云雀', '云雀 (Skylark) 是字节内部团队研发的大规模预训练语言模型系列', 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif', '这是一段文字描述，用于给模型提供上下文，此版本未用到', 1, 1, '2023-12-13 22:19:11', '2023-12-13 22:19:13');
INSERT INTO `ts_ai_robot` VALUES (5, 'chat-baichuan', '百川模型', 'Baichuan2 Turbo大模型，融合长上下文窗口和搜索增强，实现大模型与领域知识、全网知识的全新链接。支持PDF、Word等多种文档上传及网址输入，信息获取及时、全面，输出结果准确、专业。', 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif', '这是一段文字描述，用于给模型提供上下文，此版本未用到', 1, 1, '2024-01-08 16:56:37', '2024-01-08 16:56:40');

-- ----------------------------
-- Table structure for ts_blog_article
-- ----------------------------
DROP TABLE IF EXISTS `ts_blog_article`;
CREATE TABLE `ts_blog_article`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '标题',
  `sub_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '副标题',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL COMMENT '详细内容',
  `hits` int(11) NULL DEFAULT 0 COMMENT '点击量',
  `allow_comment` int(2) NULL DEFAULT 0 COMMENT '允许评论0：不允许 1：允许',
  `sort` int(5) NULL DEFAULT 0 COMMENT '排序',
  `is_top` int(2) NULL DEFAULT 0 COMMENT '是否置顶 0：不置顶 1：置顶',
  `status` int(2) NULL DEFAULT 0 COMMENT '状态 0：草稿 1：发布 ',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_520_ci COMMENT = '博客-文章表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ts_blog_article
-- ----------------------------

-- ----------------------------
-- Table structure for ts_blog_article_classify
-- ----------------------------
DROP TABLE IF EXISTS `ts_blog_article_classify`;
CREATE TABLE `ts_blog_article_classify`  (
  `article_id` bigint(20) NOT NULL COMMENT '文章ID',
  `classify_id` bigint(20) NOT NULL COMMENT '分类ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_520_ci COMMENT = '文章-分类关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ts_blog_article_classify
-- ----------------------------

-- ----------------------------
-- Table structure for ts_blog_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `ts_blog_article_tag`;
CREATE TABLE `ts_blog_article_tag`  (
  `article_id` bigint(20) NOT NULL COMMENT '文章ID',
  `tag_id` bigint(20) NOT NULL COMMENT '标签ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_520_ci COMMENT = '文章-标签关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ts_blog_article_tag
-- ----------------------------

-- ----------------------------
-- Table structure for ts_blog_classify
-- ----------------------------
DROP TABLE IF EXISTS `ts_blog_classify`;
CREATE TABLE `ts_blog_classify`  (
  `classify_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT NULL COMMENT '名称',
  `status` int(2) NULL DEFAULT NULL COMMENT '状态 0:禁用 1:启用',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`classify_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_520_ci COMMENT = '文章分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ts_blog_classify
-- ----------------------------

-- ----------------------------
-- Table structure for ts_blog_tag
-- ----------------------------
DROP TABLE IF EXISTS `ts_blog_tag`;
CREATE TABLE `ts_blog_tag`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '标签名',
  `status` int(2) NULL DEFAULT NULL COMMENT '状态 0:禁用 1:可用',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_520_ci COMMENT = '博客-标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ts_blog_tag
-- ----------------------------

-- ----------------------------
-- Table structure for ts_file_upload_record
-- ----------------------------
DROP TABLE IF EXISTS `ts_file_upload_record`;
CREATE TABLE `ts_file_upload_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '原文件名',
  `file_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '文件类型：扩展名',
  `file_size` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '文件大小',
  `file_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '文件url',
  `excerpt_hash` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '文件摘要hash，防止重复文件',
  `upload_user_id` bigint(20) NULL DEFAULT NULL COMMENT '上传人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 80 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_520_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ts_file_upload_record
-- ----------------------------

-- ----------------------------
-- Table structure for ts_music
-- ----------------------------
DROP TABLE IF EXISTS `ts_music`;
CREATE TABLE `ts_music`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '歌曲名称',
  `artist` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '作者',
  `album` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '专辑',
  `src` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '音乐路径',
  `pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '背景图',
  `sort` int(5) NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 52 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_520_ci COMMENT = '工具-音也播放器' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ts_music
-- ----------------------------

-- ----------------------------
-- Table structure for ts_plan
-- ----------------------------
DROP TABLE IF EXISTS `ts_plan`;
CREATE TABLE `ts_plan`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `plan_desc` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '待办描述',
  `plan_date` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '待办日期',
  `status` int(2) NULL DEFAULT 0 COMMENT '状态 0：未完成 1：已完成  2：删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_520_ci COMMENT = '待办表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ts_plan
-- ----------------------------

-- ----------------------------
-- Table structure for ts_sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `ts_sys_menu`;
CREATE TABLE `ts_sys_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父级ID',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '菜单名称',
  `sort` int(4) NULL DEFAULT 0 COMMENT '菜单排序',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '地址',
  `permission_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限码',
  `is_menu` tinyint(1) NULL DEFAULT NULL COMMENT '是否是菜单 0:按钮 1:菜单 2：目录 3：外链',
  `icon` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端图标',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态 1：可用 0：禁用',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5021 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后台菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ts_sys_menu
-- ----------------------------
INSERT INTO `ts_sys_menu` VALUES (1, 0, '系统管理', 2, '', 'sys:sys:manage', 2, 'system', 1, '2023-02-17 11:43:42');
INSERT INTO `ts_sys_menu` VALUES (2, 0, '博客管理', 3, '', 'sys:blog:manage', 2, 'post', 1, '2023-05-31 11:15:08');
INSERT INTO `ts_sys_menu` VALUES (3, 0, '功能管理', 4, '', 'sys:tool:manage', 2, 'tool', 1, '2023-05-18 14:32:34');
INSERT INTO `ts_sys_menu` VALUES (100, 1, '用户管理', 1, '', 'sys:user:manage', 1, 'user', 1, '2023-02-17 11:43:42');
INSERT INTO `ts_sys_menu` VALUES (101, 1, '菜单管理', 2, '', 'sys:menu:manage', 1, 'tree-table', 1, '2023-02-17 11:44:22');
INSERT INTO `ts_sys_menu` VALUES (102, 1, '角色管理', 3, '', 'sys:role:manage', 1, 'peoples', 1, '2023-02-17 11:44:45');
INSERT INTO `ts_sys_menu` VALUES (103, 0, '日志管理', 5, '', 'sys:log:manage', 2, 'log', 1, '2023-05-11 23:26:36');
INSERT INTO `ts_sys_menu` VALUES (201, 2, '文章管理', 1, '', 'sys:article:manage', 1, 'el-icon-document', 1, '2023-05-31 11:16:27');
INSERT INTO `ts_sys_menu` VALUES (202, 2, '标签管理', 2, '', 'sys:articleTag:manage', 1, 'tag', 1, '2023-05-31 11:18:46');
INSERT INTO `ts_sys_menu` VALUES (203, 2, '分类管理', 3, '', 'sys:articleClassify:manage', 1, 'tag', 1, '2023-05-31 11:18:46');
INSERT INTO `ts_sys_menu` VALUES (301, 3, '音乐管理', 2, '', 'sys:music:manage', 1, 'el-icon-service', 1, '2023-05-14 18:55:46');
INSERT INTO `ts_sys_menu` VALUES (302, 3, '好评管理', 3, '', 'sys:takeaway:manage', 1, 'el-icon-food', 1, '2023-05-14 21:38:48');
INSERT INTO `ts_sys_menu` VALUES (500, 103, '登录日志', 1, '', 'sys:loginlog:manage', 1, 'logininfor', 1, '2023-05-14 21:38:48');
INSERT INTO `ts_sys_menu` VALUES (501, 103, '操作日志', 2, '', 'sys:operlog:manage', 1, 'form', 1, '2023-05-14 21:38:48');
INSERT INTO `ts_sys_menu` VALUES (1001, 100, '用户添加', 1, '', 'sys:user:add', 0, '', 1, '2023-02-17 11:46:32');
INSERT INTO `ts_sys_menu` VALUES (1002, 100, '用户修改', 2, '', 'sys:user:update', 0, '', 1, '2023-02-17 11:47:10');
INSERT INTO `ts_sys_menu` VALUES (1003, 100, '用户启用', 3, '', 'sys:user:enable', 0, '', 1, '2023-02-17 11:47:38');
INSERT INTO `ts_sys_menu` VALUES (1004, 100, '用户禁用', 4, '', 'sys:user:disable', 0, '', 1, '2023-02-17 11:47:54');
INSERT INTO `ts_sys_menu` VALUES (1005, 100, '用户删除', 5, '', 'sys:user:delete', 0, '', 1, '2023-02-17 11:48:36');
INSERT INTO `ts_sys_menu` VALUES (1006, 100, '重置用户密码', 6, '', 'sys:user:pwdreset', 0, '', 1, '2023-02-17 11:49:16');
INSERT INTO `ts_sys_menu` VALUES (1007, 100, '用户列表', 7, '', 'sys:user:list', 0, '', 1, '2023-02-17 11:46:32');
INSERT INTO `ts_sys_menu` VALUES (1008, 100, '用户分配角色', 8, '', 'sys:user:auth', 0, '', 1, '2023-02-17 11:46:32');
INSERT INTO `ts_sys_menu` VALUES (1011, 101, '菜单添加', 1, '', 'sys:menu:add', 0, '', 1, '2023-02-17 11:50:18');
INSERT INTO `ts_sys_menu` VALUES (1012, 101, '菜单修改', 2, '', 'sys:menu:update', 0, '', 1, '2023-02-17 11:50:47');
INSERT INTO `ts_sys_menu` VALUES (1013, 101, '菜单删除', 3, '', 'sys:menu:delete', 0, '', 1, '2023-02-17 11:50:47');
INSERT INTO `ts_sys_menu` VALUES (1014, 101, '菜单启用', 4, '', 'sys:menu:enable', 0, '', 1, '2023-02-17 11:50:47');
INSERT INTO `ts_sys_menu` VALUES (1015, 101, '菜单禁用', 5, '', 'sys:menu:disable', 0, '', 1, '2023-02-17 11:50:47');
INSERT INTO `ts_sys_menu` VALUES (1016, 101, '菜单列表', 6, '', 'sys:menu:list', 0, '', 1, '2023-02-17 11:50:47');
INSERT INTO `ts_sys_menu` VALUES (1021, 102, '角色删除', 1, '', 'sys:role:delete', 0, '', 1, '2023-05-04 15:57:05');
INSERT INTO `ts_sys_menu` VALUES (1022, 102, '角色启用', 2, '', 'sys:role:enable', 0, '', 1, '2023-05-04 15:57:42');
INSERT INTO `ts_sys_menu` VALUES (1023, 102, '角色禁用', 3, '', 'sys:role:disable', 0, '', 1, '2023-05-04 15:58:03');
INSERT INTO `ts_sys_menu` VALUES (1024, 102, '角色添加', 4, '', 'sys:role:add', 0, '', 1, '2023-02-17 11:52:09');
INSERT INTO `ts_sys_menu` VALUES (1025, 102, '角色修改', 5, '', 'sys:role:update', 0, '', 1, '2023-02-17 11:53:00');
INSERT INTO `ts_sys_menu` VALUES (1026, 102, '角色列表', 6, '', 'sys:role:list', 0, '', 1, '2023-02-17 11:53:00');
INSERT INTO `ts_sys_menu` VALUES (1027, 102, '角色分配', 7, '', 'sys:role:auth', 0, '', 1, '2023-02-17 11:53:00');
INSERT INTO `ts_sys_menu` VALUES (2011, 201, '添加文章', 1, '', 'sys:blog:add', 0, '', 1, '2023-05-31 11:17:21');
INSERT INTO `ts_sys_menu` VALUES (2012, 201, '修改文章', 2, '', 'sys:blog:update', 0, '', 1, '2023-05-31 11:17:57');
INSERT INTO `ts_sys_menu` VALUES (2013, 201, '删除文章', 3, '', 'sys:blog:delete', 0, '', 1, '2023-05-31 11:18:07');
INSERT INTO `ts_sys_menu` VALUES (2014, 201, '文章列表', 4, '', 'sys:blog:list', 0, '', 1, '2023-05-31 11:18:07');
INSERT INTO `ts_sys_menu` VALUES (2021, 202, '添加标签', 1, '', 'sys:blogTag:add', 0, '', 1, '2023-05-31 11:19:25');
INSERT INTO `ts_sys_menu` VALUES (2022, 202, '修改标签', 2, '', 'sys:blogTag:update', 0, '', 1, '2023-05-31 11:19:43');
INSERT INTO `ts_sys_menu` VALUES (2023, 202, '删除标签', 3, '', 'sys:blogTag:delete', 0, '', 1, '2023-05-31 11:19:59');
INSERT INTO `ts_sys_menu` VALUES (2024, 202, '标签启用', 4, '', 'sys:blogTag:enable', 0, '', 1, '2023-05-31 11:21:19');
INSERT INTO `ts_sys_menu` VALUES (2025, 202, '标签禁用', 5, '', 'sys:blogTag:disable', 0, '', 1, '2023-05-31 11:21:38');
INSERT INTO `ts_sys_menu` VALUES (2026, 202, '标签列表', 6, '', 'sys:blogTag:list', 0, '', 1, '2023-05-31 11:21:38');
INSERT INTO `ts_sys_menu` VALUES (2031, 203, '添加分类', 1, '', 'sys:blogClassify:add', 0, '', 1, '2023-05-31 11:19:25');
INSERT INTO `ts_sys_menu` VALUES (2032, 203, '修改分类', 2, '', 'sys:blogClassify:update', 0, '', 1, '2023-05-31 11:19:43');
INSERT INTO `ts_sys_menu` VALUES (2033, 203, '删除分类', 3, '', 'sys:blogClassify:delete', 0, '', 1, '2023-05-31 11:19:59');
INSERT INTO `ts_sys_menu` VALUES (2034, 202, '启用分类', 4, '', 'sys:blogClassify:enable', 0, '', 1, '2023-05-31 11:21:19');
INSERT INTO `ts_sys_menu` VALUES (2035, 203, '禁用分类', 5, '', 'sys:blogClassify:disable', 0, '', 1, '2023-05-31 11:21:38');
INSERT INTO `ts_sys_menu` VALUES (2036, 203, '分类列表', 6, '', 'sys:blogClassify:list', 0, '', 1, '2023-05-31 11:21:38');
INSERT INTO `ts_sys_menu` VALUES (3011, 301, '音乐新增', 1, '', 'sys:music:add', 0, '', 1, '2023-05-14 19:02:24');
INSERT INTO `ts_sys_menu` VALUES (3012, 301, '音乐修改', 2, '', 'sys:music:update', 0, '', 1, '2023-05-14 19:02:48');
INSERT INTO `ts_sys_menu` VALUES (3013, 301, '音乐删除', 3, '', 'sys:music:delete', 0, '', 1, '2023-05-14 19:03:10');
INSERT INTO `ts_sys_menu` VALUES (3014, 301, '音乐列表', 4, '', 'sys:music:list', 0, '', 1, '2023-05-14 19:03:10');
INSERT INTO `ts_sys_menu` VALUES (3021, 302, '新增好评', 1, '', 'sys:takeaway:add', 0, '', 1, '2023-05-14 21:39:13');
INSERT INTO `ts_sys_menu` VALUES (3022, 302, '修改好评', 2, '', 'sys:takeaway:update', 0, '', 1, '2023-05-14 21:39:30');
INSERT INTO `ts_sys_menu` VALUES (3023, 302, '删除好评', 3, '', 'sys:takeaway:delete', 0, '', 1, '2023-05-14 21:40:06');
INSERT INTO `ts_sys_menu` VALUES (3024, 302, '好评列表', 4, '', 'sys:takeaway:list', 0, '', 1, '2023-05-14 21:40:06');
INSERT INTO `ts_sys_menu` VALUES (5001, 500, '登录日志列表', 1, '', 'monitor:loginlog:list', 1, '', 1, '2023-05-11 23:27:54');
INSERT INTO `ts_sys_menu` VALUES (5011, 501, '操作日志列表', 2, '', 'monitor:operlog:list', 1, '', 1, '2023-05-11 23:29:29');
INSERT INTO `ts_sys_menu` VALUES (5015, 5013, '外卖好评', 2, '', 'sys:open:takeaway', 1, 'el-icon-food', 1, '2023-06-02 16:06:44');
INSERT INTO `ts_sys_menu` VALUES (5016, 0, '首页数据统计', 0, '', 'sys:index:monitor', 2, 'dashboard', 1, '2023-11-06 11:27:44');
INSERT INTO `ts_sys_menu` VALUES (5017, 3, '上传管理', 1, '', 'sys:upRecord:manage', 1, 'upload', 1, '2023-11-16 15:26:36');
INSERT INTO `ts_sys_menu` VALUES (5018, 5017, '上传列表', 1, '', 'sys:upRecord:list', 0, '', 1, '2023-11-16 15:27:18');
INSERT INTO `ts_sys_menu` VALUES (5019, 5017, '删除记录', 2, '', 'sys:upRecord:delete', 0, '', 1, '2023-11-16 15:28:11');
INSERT INTO `ts_sys_menu` VALUES (5020, 0, 'AI 聊天', 1, '', 'sys:ai:chat', 2, 'el-icon-s-comment', 1, '2024-01-11 18:25:09');

-- ----------------------------
-- Table structure for ts_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `ts_sys_role`;
CREATE TABLE `ts_sys_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `user_count` int(11) NULL DEFAULT NULL COMMENT '用户数量',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `status` int(1) NULL DEFAULT 1 COMMENT '启用状态：0->禁用；1->启用',
  `sort` int(11) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ts_sys_role
-- ----------------------------
INSERT INTO `ts_sys_role` VALUES (1, 'ADMIN', '超级管理员', 1, '2023-02-17 12:00:23', 1, 0);
INSERT INTO `ts_sys_role` VALUES (3, 'USER', '普通用户', 4, '2023-05-02 10:57:37', 1, 2);
INSERT INTO `ts_sys_role` VALUES (10, 'SUB_ADMIN', '子管理员', 2, '2023-05-05 17:21:01', 1, 1);

-- ----------------------------
-- Table structure for ts_sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `ts_sys_role_menu`;
CREATE TABLE `ts_sys_role_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NULL DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 633 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后台角色菜单关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ts_sys_role_menu
-- ----------------------------
INSERT INTO `ts_sys_role_menu` VALUES (293, 3, 201);
INSERT INTO `ts_sys_role_menu` VALUES (294, 3, 2014);
INSERT INTO `ts_sys_role_menu` VALUES (295, 3, 202);
INSERT INTO `ts_sys_role_menu` VALUES (296, 3, 2026);
INSERT INTO `ts_sys_role_menu` VALUES (298, 3, 5014);
INSERT INTO `ts_sys_role_menu` VALUES (299, 3, 5015);
INSERT INTO `ts_sys_role_menu` VALUES (300, 3, 4);
INSERT INTO `ts_sys_role_menu` VALUES (574, 10, 1);
INSERT INTO `ts_sys_role_menu` VALUES (575, 10, 100);
INSERT INTO `ts_sys_role_menu` VALUES (576, 10, 1001);
INSERT INTO `ts_sys_role_menu` VALUES (577, 10, 1002);
INSERT INTO `ts_sys_role_menu` VALUES (578, 10, 1003);
INSERT INTO `ts_sys_role_menu` VALUES (579, 10, 1004);
INSERT INTO `ts_sys_role_menu` VALUES (580, 10, 1005);
INSERT INTO `ts_sys_role_menu` VALUES (581, 10, 1006);
INSERT INTO `ts_sys_role_menu` VALUES (582, 10, 1007);
INSERT INTO `ts_sys_role_menu` VALUES (583, 10, 1008);
INSERT INTO `ts_sys_role_menu` VALUES (584, 10, 101);
INSERT INTO `ts_sys_role_menu` VALUES (585, 10, 1011);
INSERT INTO `ts_sys_role_menu` VALUES (586, 10, 1012);
INSERT INTO `ts_sys_role_menu` VALUES (587, 10, 1013);
INSERT INTO `ts_sys_role_menu` VALUES (588, 10, 1014);
INSERT INTO `ts_sys_role_menu` VALUES (589, 10, 1015);
INSERT INTO `ts_sys_role_menu` VALUES (590, 10, 1016);
INSERT INTO `ts_sys_role_menu` VALUES (591, 10, 102);
INSERT INTO `ts_sys_role_menu` VALUES (592, 10, 1021);
INSERT INTO `ts_sys_role_menu` VALUES (593, 10, 1022);
INSERT INTO `ts_sys_role_menu` VALUES (594, 10, 1023);
INSERT INTO `ts_sys_role_menu` VALUES (595, 10, 1024);
INSERT INTO `ts_sys_role_menu` VALUES (596, 10, 1025);
INSERT INTO `ts_sys_role_menu` VALUES (597, 10, 1026);
INSERT INTO `ts_sys_role_menu` VALUES (598, 10, 1027);
INSERT INTO `ts_sys_role_menu` VALUES (599, 10, 2);
INSERT INTO `ts_sys_role_menu` VALUES (600, 10, 201);
INSERT INTO `ts_sys_role_menu` VALUES (601, 10, 2011);
INSERT INTO `ts_sys_role_menu` VALUES (602, 10, 2012);
INSERT INTO `ts_sys_role_menu` VALUES (603, 10, 2013);
INSERT INTO `ts_sys_role_menu` VALUES (604, 10, 2014);
INSERT INTO `ts_sys_role_menu` VALUES (605, 10, 202);
INSERT INTO `ts_sys_role_menu` VALUES (606, 10, 2021);
INSERT INTO `ts_sys_role_menu` VALUES (607, 10, 2022);
INSERT INTO `ts_sys_role_menu` VALUES (608, 10, 2023);
INSERT INTO `ts_sys_role_menu` VALUES (609, 10, 2024);
INSERT INTO `ts_sys_role_menu` VALUES (610, 10, 2034);
INSERT INTO `ts_sys_role_menu` VALUES (611, 10, 2025);
INSERT INTO `ts_sys_role_menu` VALUES (612, 10, 2026);
INSERT INTO `ts_sys_role_menu` VALUES (613, 10, 203);
INSERT INTO `ts_sys_role_menu` VALUES (614, 10, 2031);
INSERT INTO `ts_sys_role_menu` VALUES (615, 10, 2032);
INSERT INTO `ts_sys_role_menu` VALUES (616, 10, 2033);
INSERT INTO `ts_sys_role_menu` VALUES (617, 10, 2035);
INSERT INTO `ts_sys_role_menu` VALUES (618, 10, 2036);
INSERT INTO `ts_sys_role_menu` VALUES (619, 10, 3);
INSERT INTO `ts_sys_role_menu` VALUES (620, 10, 5017);
INSERT INTO `ts_sys_role_menu` VALUES (621, 10, 5018);
INSERT INTO `ts_sys_role_menu` VALUES (622, 10, 5019);
INSERT INTO `ts_sys_role_menu` VALUES (623, 10, 301);
INSERT INTO `ts_sys_role_menu` VALUES (624, 10, 3011);
INSERT INTO `ts_sys_role_menu` VALUES (625, 10, 3012);
INSERT INTO `ts_sys_role_menu` VALUES (626, 10, 3013);
INSERT INTO `ts_sys_role_menu` VALUES (627, 10, 3014);
INSERT INTO `ts_sys_role_menu` VALUES (628, 10, 302);
INSERT INTO `ts_sys_role_menu` VALUES (629, 10, 3021);
INSERT INTO `ts_sys_role_menu` VALUES (630, 10, 3022);
INSERT INTO `ts_sys_role_menu` VALUES (631, 10, 3023);
INSERT INTO `ts_sys_role_menu` VALUES (632, 10, 3024);

-- ----------------------------
-- Table structure for ts_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `ts_sys_user`;
CREATE TABLE `ts_sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户类型 00:超级管理员 11:普通用户',
  `user_resource` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'system' COMMENT '用户来源 ',
  `open_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '三方openID',
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `icon` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '头像',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '邮箱',
  `nick_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '昵称',
  `note` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '备注信息',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `login_time` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `status` int(1) NULL DEFAULT 1 COMMENT '帐号启用状态：0->禁用；1->启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ts_sys_user
-- ----------------------------
INSERT INTO `ts_sys_user` VALUES (1, 'ADMIN', 'system', '', 'root', '9D7CFA999D9A6F3060265896E8ED9C90', 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif', '1000@qq.com', '炒炒炒炒鸡管理员', '和梦想平等交易，和喧嚣保持距离', '2023-02-13 15:38:41', NULL, 1);
INSERT INTO `ts_sys_user` VALUES (35, 'USER', 'system', '', 'sub-root', '565372162987A94A01DA9D17B323F68C', 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif', '', '子管理员', '', '2024-01-11 18:28:53', NULL, 1);

-- ----------------------------
-- Table structure for ts_sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `ts_sys_user_role`;
CREATE TABLE `ts_sys_user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `admin_id` bigint(20) NULL DEFAULT NULL,
  `role_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后台用户和角色关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ts_sys_user_role
-- ----------------------------
INSERT INTO `ts_sys_user_role` VALUES (1, 1, 1);
INSERT INTO `ts_sys_user_role` VALUES (60, 35, 10);

-- ----------------------------
-- Table structure for ts_takeaway
-- ----------------------------
DROP TABLE IF EXISTS `ts_takeaway`;
CREATE TABLE `ts_takeaway`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content_text` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '内容',
  `nice_num` int(8) NULL DEFAULT 0 COMMENT '点赞数',
  `keywork_num` int(8) NULL DEFAULT 0 COMMENT '字数',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_520_ci COMMENT = '外卖好评内容表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ts_takeaway
-- ----------------------------
INSERT INTO `ts_takeaway` VALUES (1, '很不错的饭菜，价格未免也太实惠了，味道超级无敌可以的，，数量足，快递也贼快，整体还是非常不错的，超级满足，下次再来，实在是太好吃了。', 0, 66, '2023-05-06 00:00:00');
INSERT INTO `ts_takeaway` VALUES (2, '味道不错,包装感觉干净卫生,外卖小哥送餐速度快,及时送达,电话沟通语言和善，结果是感觉比想象中令人满意, 味道美极，下次还要来', 0, 63, '2023-05-06 00:00:00');

-- ----------------------------
-- Table structure for ts_user_login_log
-- ----------------------------
DROP TABLE IF EXISTS `ts_user_login_log`;
CREATE TABLE `ts_user_login_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `admin_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `ip` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `browser` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '浏览器登录类型',
  `os` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '操作系统类型',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后台用户登录日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ts_user_login_log
-- ----------------------------

-- ----------------------------
-- Table structure for ts_user_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `ts_user_oper_log`;
CREATE TABLE `ts_user_oper_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int(2) NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除 4授权 5:导出 6:导入）',
  `operator_type` int(1) NULL DEFAULT 0 COMMENT '操作类别（0：后台用户）',
  `oper_admin_id` bigint(20) NULL DEFAULT 0 COMMENT '操作人员id',
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '请求方式',
  `request_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '请求URI',
  `resp_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '响应码',
  `resp_msg` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '' COMMENT '响应消息',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL DEFAULT '0' COMMENT '操作状态',
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NULL COMMENT '错误消息',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 749 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_520_ci COMMENT = '操作日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ts_user_oper_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
