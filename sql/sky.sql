-- ============================================================
-- 苍穹外卖 (Sky Take-out) 数据库初始化脚本
-- ============================================================

CREATE DATABASE IF NOT EXISTS sky_take_out
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE sky_take_out;

-- 1. 员工表
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        VARCHAR(32)  NOT NULL COMMENT '姓名',
    `username`    VARCHAR(32)  NOT NULL COMMENT '用户名',
    `password`    VARCHAR(64)  NOT NULL COMMENT '密码(MD5)',
    `phone`       VARCHAR(11)  NOT NULL COMMENT '手机号',
    `sex`         VARCHAR(2)   NOT NULL COMMENT '性别',
    `id_number`   VARCHAR(18)  NOT NULL COMMENT '身份证号',
    `status`      INT          NOT NULL DEFAULT 1 COMMENT '状态 1启用 0禁用',
    `create_time` DATETIME     COMMENT '创建时间',
    `update_time` DATETIME     COMMENT '更新时间',
    `create_user` BIGINT       COMMENT '创建人ID',
    `update_user` BIGINT       COMMENT '修改人ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='员工信息';

-- 2. 分类表
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `type`        INT          COMMENT '类型 1菜品分类 2套餐分类',
    `name`        VARCHAR(32)  NOT NULL COMMENT '分类名称',
    `sort`        INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `status`      INT          NOT NULL DEFAULT 1 COMMENT '状态 1启用 0禁用',
    `create_time` DATETIME     COMMENT '创建时间',
    `update_time` DATETIME     COMMENT '更新时间',
    `create_user` BIGINT       COMMENT '创建人ID',
    `update_user` BIGINT       COMMENT '修改人ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品及套餐分类';

-- 3. 菜品表
DROP TABLE IF EXISTS `dish`;
CREATE TABLE `dish` (
    `id`          BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        VARCHAR(32)    NOT NULL COMMENT '菜品名称',
    `category_id` BIGINT         NOT NULL COMMENT '分类ID',
    `price`       DECIMAL(10,2)  NOT NULL COMMENT '价格',
    `image`       VARCHAR(255)   NOT NULL COMMENT '图片路径',
    `description` VARCHAR(255)   COMMENT '描述',
    `status`      INT            NOT NULL DEFAULT 1 COMMENT '状态 1起售 0停售',
    `create_time` DATETIME       COMMENT '创建时间',
    `update_time` DATETIME       COMMENT '更新时间',
    `create_user` BIGINT         COMMENT '创建人ID',
    `update_user` BIGINT         COMMENT '修改人ID',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品';

-- 4. 菜品口味表
DROP TABLE IF EXISTS `dish_flavor`;
CREATE TABLE `dish_flavor` (
    `id`      BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `dish_id` BIGINT       NOT NULL COMMENT '菜品ID',
    `name`    VARCHAR(32)  NOT NULL COMMENT '口味名称',
    `value`   VARCHAR(255) NOT NULL COMMENT '口味数据(JSON数组)',
    PRIMARY KEY (`id`),
    KEY `idx_dish_id` (`dish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品口味关系表';

-- 5. 套餐表
DROP TABLE IF EXISTS `setmeal`;
CREATE TABLE `setmeal` (
    `id`          BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `category_id` BIGINT         NOT NULL COMMENT '分类ID',
    `name`        VARCHAR(32)    NOT NULL COMMENT '套餐名称',
    `price`       DECIMAL(10,2)  NOT NULL COMMENT '套餐价格',
    `image`       VARCHAR(255)   NOT NULL COMMENT '图片路径',
    `description` VARCHAR(255)   COMMENT '描述',
    `status`      INT            NOT NULL DEFAULT 1 COMMENT '状态 1起售 0停售',
    `create_time` DATETIME       COMMENT '创建时间',
    `update_time` DATETIME       COMMENT '更新时间',
    `create_user` BIGINT         COMMENT '创建人ID',
    `update_user` BIGINT         COMMENT '修改人ID',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='套餐';

-- 6. 套餐菜品关系表
DROP TABLE IF EXISTS `setmeal_dish`;
CREATE TABLE `setmeal_dish` (
    `id`         BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `setmeal_id` BIGINT         NOT NULL COMMENT '套餐ID',
    `dish_id`    BIGINT         NOT NULL COMMENT '菜品ID',
    `name`       VARCHAR(32)    COMMENT '菜品名称(冗余)',
    `price`      DECIMAL(10,2)  COMMENT '菜品单价(冗余)',
    `copies`     INT            NOT NULL DEFAULT 1 COMMENT '份数',
    PRIMARY KEY (`id`),
    KEY `idx_setmeal_id` (`setmeal_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='套餐菜品关系表';

-- 7. C端用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `openid`      VARCHAR(45)  NOT NULL COMMENT '微信openid',
    `name`        VARCHAR(32)  COMMENT '用户昵称',
    `phone`       VARCHAR(11)  COMMENT '手机号',
    `sex`         VARCHAR(2)   COMMENT '性别',
    `id_number`   VARCHAR(18)  COMMENT '身份证号',
    `avatar`      VARCHAR(500) COMMENT '头像URL',
    `create_time` DATETIME     COMMENT '注册时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_openid` (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='C端用户信息';

-- 8. 地址簿表
DROP TABLE IF EXISTS `address_book`;
CREATE TABLE `address_book` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`       BIGINT       NOT NULL COMMENT '用户ID',
    `consignee`     VARCHAR(50)  NOT NULL COMMENT '收货人',
    `sex`           VARCHAR(1)   COMMENT '性别',
    `phone`         VARCHAR(11)  NOT NULL COMMENT '手机号',
    `province_code` VARCHAR(12)  COMMENT '省级区划编码',
    `province_name` VARCHAR(32)  COMMENT '省份',
    `city_code`     VARCHAR(12)  COMMENT '市级区划编码',
    `city_name`     VARCHAR(32)  COMMENT '城市',
    `district_code` VARCHAR(12)  COMMENT '区级区划编码',
    `district_name` VARCHAR(32)  COMMENT '区',
    `detail`        VARCHAR(200) NOT NULL COMMENT '详细地址',
    `label`         VARCHAR(100) COMMENT '标签(家/公司/学校)',
    `is_default`    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '是否默认地址',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='地址簿';

-- 9. 购物车表
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart` (
    `id`          BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        VARCHAR(32)    COMMENT '商品名称',
    `image`       VARCHAR(255)   COMMENT '商品图片',
    `user_id`     BIGINT         NOT NULL COMMENT '用户ID',
    `dish_id`     BIGINT         COMMENT '菜品ID',
    `setmeal_id`  BIGINT         COMMENT '套餐ID',
    `dish_flavor` VARCHAR(50)    COMMENT '口味选择',
    `number`      INT            NOT NULL DEFAULT 1 COMMENT '数量',
    `amount`      DECIMAL(10,2)  NOT NULL COMMENT '金额',
    `create_time` DATETIME       COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车';

-- 10. 订单表
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
    `id`                      BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键(订单号)',
    `number`                  VARCHAR(50)    COMMENT '订单号(显示用)',
    `status`                  INT            NOT NULL DEFAULT 1 COMMENT '1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款',
    `user_id`                 BIGINT         NOT NULL COMMENT '用户ID',
    `address_book_id`         BIGINT         NOT NULL COMMENT '地址ID',
    `order_time`              DATETIME       NOT NULL COMMENT '下单时间',
    `checkout_time`           DATETIME       COMMENT '付款时间',
    `pay_method`              INT            NOT NULL DEFAULT 1 COMMENT '1微信 2支付宝',
    `pay_status`              TINYINT        NOT NULL DEFAULT 0 COMMENT '支付状态 0未付 1已付 2退款',
    `amount`                  DECIMAL(10,2)  NOT NULL COMMENT '实收金额',
    `remark`                  VARCHAR(100)   COMMENT '备注',
    `phone`                   VARCHAR(11)    COMMENT '手机号',
    `address`                 VARCHAR(255)   COMMENT '地址详情',
    `user_name`               VARCHAR(32)    COMMENT '用户名称',
    `consignee`               VARCHAR(32)    COMMENT '收货人',
    `cancel_reason`           VARCHAR(255)   COMMENT '取消原因',
    `rejection_reason`        VARCHAR(255)   COMMENT '拒单原因',
    `cancel_time`             DATETIME       COMMENT '取消时间',
    `estimated_delivery_time` DATETIME       COMMENT '预计送达时间',
    `delivery_status`         TINYINT        NOT NULL DEFAULT 0 COMMENT '配送状态 0未发 1已发',
    `delivery_time`           DATETIME       COMMENT '送达时间',
    `pack_amount`             INT            COMMENT '打包费',
    `tableware_number`        INT            COMMENT '餐具数量',
    `tableware_status`        TINYINT        NOT NULL DEFAULT 1 COMMENT '餐具数量状态 1按餐量提供 0选择具体数量',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_number` (`number`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 11. 订单明细表
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail` (
    `id`          BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        VARCHAR(32)    NOT NULL COMMENT '商品名称',
    `image`       VARCHAR(255)   COMMENT '商品图片',
    `order_id`    BIGINT         NOT NULL COMMENT '订单ID',
    `dish_id`     BIGINT         COMMENT '菜品ID',
    `setmeal_id`  BIGINT         COMMENT '套餐ID',
    `dish_flavor` VARCHAR(50)    COMMENT '口味',
    `number`      INT            NOT NULL DEFAULT 1 COMMENT '数量',
    `amount`      DECIMAL(10,2)  NOT NULL COMMENT '金额',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- 插入默认管理员账号 (密码 = MD5("123456") = e10adc3949ba59abbe56e057f20f883e)
INSERT INTO `employee` (id, name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user)
VALUES (1, '管理员', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '13812345678', '1', '110101199001011234', 1, NOW(), NOW(), 1, 1);
