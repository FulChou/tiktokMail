-- 创建数据库
CREATE DATABASE IF NOT EXISTS mail;

-- 使用数据库
USE mail;

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 插入初始用户数据
INSERT INTO users (username, email, password) VALUES
('user1', 'user1@example.com', 'password1'),
('user2', 'user2@example.com', 'password2'),
('user3', 'user3@example.com', 'password3');

-- 查询所有用户
SELECT * FROM users;

-- 更新用户数据
UPDATE users SET email = 'newemail@example.com' WHERE username = 'user1';

-- 删除用户数据
DELETE FROM users WHERE username = 'user3';

CREATE TABLE products (
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 商品ID（主键）
    product_name VARCHAR(255) NOT NULL,           -- 商品名称
    description TEXT,                             -- 商品描述
    price DECIMAL(10, 2) NOT NULL,                -- 商品价格
    category_id BIGINT,                           -- 商品分类ID（外键）
    brand_id BIGINT,                              -- 品牌ID（外键）
    status tinyint DEFAULT 1, -- 商品状态
    deleted BOOLEAN DEFAULT FALSE,                -- 软删除    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,    -- 创建时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 更新时间
);

CREATE TABLE inventory (
   inventory_id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 库存ID（主键）
   product_id BIGINT NOT NULL,                     -- 商品ID（外键）
   stock_quantity INT NOT NULL DEFAULT 0,          -- 库存数量
   locked_quantity INT NOT NULL DEFAULT 0,         -- 锁定库存数量（如未支付的订单）
   low_stock_threshold INT DEFAULT 10,             -- 低库存阈值
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 更新时间
   FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE -- 外键约束
);