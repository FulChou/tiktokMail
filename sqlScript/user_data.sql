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