-- 创建数据库
CREATE DATABASE IF NOT EXISTS mail;

-- 使用数据库
USE mail;

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),          -- 用户电话
    address TEXT,               -- 用户地址
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 插入初始用户数据
-- 插入示例数据
INSERT INTO users (username, email, phone, address, password)
VALUES
    ('张三', 'zhangsan@example.com', '13800000001', '北京市朝阳区', 'password123'),  -- 用户1
    ('李四', 'lisi@example.com', '13800000002', '上海市浦东新区', 'password456'),    -- 用户2
    ('王五', 'wangwu@example.com', '13800000003', '广州市天河区', 'password789'),    -- 用户3
    ('赵六', 'zhaoliu@example.com', '13800000004', '深圳市南山区', 'password1011'),  -- 用户4
    ('周七', 'zhouqi@example.com', '13800000005', '成都市武侯区', 'password1213');   -- 用户5


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
    deleted BOOLEAN DEFAULT FALSE,                -- 软删除
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,    -- 创建时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 更新时间
);

-- 插入初始商品数据
INSERT INTO products (product_name, description, price, category_id, brand_id, status)
VALUES
    ('苹果 iPhone 13', '苹果最新款 iPhone 13 手机', 6999.99, 1, 1, 1),  -- 商品1
    ('华为 Mate 40 Pro', '华为旗舰手机 Mate 40 Pro', 7999.99, 1, 2, 1),  -- 商品2
    ('小米 11', '小米公司推出的 5G 高性价比手机', 3999.99, 1, 3, 1),    -- 商品3
    ('戴森 V11 吸尘器', '高效能吸尘器，强劲吸力', 4999.99, 2, 4, 1),    -- 商品4
    ('三星 QLED 电视', '超高分辨率的三星 QLED 电视', 7999.99, 3, 5, 1);  -- 商品5

-- 创建库存表
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

-- 插入初始库存数据
INSERT INTO inventory (product_id, stock_quantity, locked_quantity, low_stock_threshold)
VALUES
    (1, 100, 0, 10),  -- 苹果 iPhone 13
    (2, 50, 5, 5),    -- 华为 Mate 40 Pro
    (3, 150, 0, 10),  -- 小米 11
    (4, 30, 0, 5),    -- 戴森 V11 吸尘器
    (5, 200, 0, 20);  -- 三星 QLED 电视

-- 创建订单表
CREATE TABLE orders (
  id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 订单ID（主键）
  order_number VARCHAR(255) NOT NULL,          -- 订单编号（用户看）
  user_id BIGINT NOT NULL,                     -- 用户ID（外键）
  total_price DECIMAL(10, 2) NOT NULL,         -- 总价
  payment_transaction_id BIGINT,       -- 支付交易ID
  status TINYINT DEFAULT 0,                    -- 订单状态（0：待支付，1：已支付，2：已发货，3：已完成，4：已取消）
  deleted BOOLEAN DEFAULT FALSE,                -- 软删除
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 插入初始订单数据
INSERT INTO orders (order_number, user_id, total_price, payment_transaction_id, status)
VALUES
    ('ORD202302210001', 1, 7999.99, 123456789, 0),  -- 用户1，待支付
    ('ORD202302210002', 2, 11999.98, 987654321, 1), -- 用户2，已支付
    ('ORD202302210003', 3, 3999.99, 192837465, 2),  -- 用户3，已发货
    ('ORD202302210004', 4, 7999.99, 112233445, 3),  -- 用户4，已完成
    ('ORD202302210005', 5, 4999.99, 556677889, 4);  -- 用户5，已取消

-- 创建订单明细表
CREATE TABLE order_detail (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 订单商品ID（主键）
    order_id BIGINT NOT NULL,              -- 订单ID（外键，关联到 orders 表）
    product_id BIGINT NOT NULL,            -- 商品ID（外键，关联到 products 表）
    quantity INT NOT NULL,                 -- 购买数量
    price DECIMAL(10, 2) NOT NULL,         -- 购买金额
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 插入初始订单明细数据
INSERT INTO order_detail (order_id, product_id, quantity, price)
VALUES
    (1, 1, 1, 7999.99),  -- 订单1，购买了 1 个苹果 iPhone 13
    (1, 4, 1, 4999.99),  -- 订单1，购买了 1 个戴森 V11 吸尘器
    (2, 2, 1, 7999.99),  -- 订单2，购买了 1 个华为 Mate 40 Pro
    (2, 5, 2, 7999.99),  -- 订单2，购买了 2 个三星 QLED 电视
    (3, 3, 1, 3999.99),  -- 订单3，购买了 1 个小米 11
    (4, 1, 1, 7999.99),  -- 订单4，购买了 1 个苹果 iPhone 13
    (5, 2, 1, 7999.99);  -- 订单5，购买了 1 个华为 Mate 40 Pro

-- 创建购物车表
CREATE TABLE carts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 购物车ID（主键）
    user_id BIGINT NOT NULL,               -- 用户ID（外键，关联到 users 表）
    product_id BIGINT NOT NULL,            -- 商品ID（外键，关联到 products 表）
    quantity INT NOT NULL,                 -- 购买数量
    deleted BOOLEAN DEFAULT FALSE,         -- 软删除
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 插入初始购物车数据
INSERT INTO carts (user_id, product_id, quantity, deleted)
VALUES
    (1, 1, 2, FALSE),  -- 用户1，购物车中有 2 个苹果 iPhone 13
    (1, 2, 1, FALSE),  -- 用户1，购物车中有 1 个华为 Mate 40 Pro
    (2, 3, 3, FALSE),  -- 用户2，购物车中有 3 个小米 11
    (3, 4, 1, FALSE),  -- 用户3，购物车中有 1 个戴森 V11 吸尘器
    (4, 5, 1, FALSE),  -- 用户4，购物车中有 1 个三星 QLED 电视
    (5, 1, 1, TRUE);   -- 用户5，购物车中有 1 个苹果 iPhone 13（已删除）

-- 创建交易支付表
CREATE TABLE payment_transactions (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,         -- 支付交易ID（主键）
      order_id BIGINT NOT NULL,                      -- 订单ID（外键，关联到orders表）
      settlement_bill_id BIGINT NOT NULL,      -- 关联结算单ID
      user_id BIGINT NOT NULL,                       -- 用户ID
      amount DECIMAL(10, 2) NOT NULL,                -- 支付金额
      payment_method VARCHAR(50),                    -- 支付方式（如 "ALIPAY", "WECHAT", "CREDIT_CARD"）
      payment_status TINYINT DEFAULT 0,              -- 支付状态（0：待支付，1：支付成功，2：支付失败，3：已取消）
      transaction_id VARCHAR(255),                   -- 第三方支付交易ID
      message TEXT,                                  -- 支付失败时的消息
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 更新时间
);

-- 插入支付交易数据
INSERT INTO payment_transactions (order_id, settlement_bill_id, user_id, amount, payment_method, payment_status, transaction_id, message, created_at, updated_at)
VALUES
    (1, 1, 123, 1000.00, 'ALIPAY', 1, 'txn_123456', 'Payment successful', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- 支付成功
    (2, 2, 124, 1500.00, 'WECHAT', 1, 'txn_234567', 'Payment successful', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- 支付成功
    (3, 3, 125, 2000.00, 'CREDIT_CARD', 2, 'txn_345678', 'Payment failed: Insufficient funds', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- 支付失败
    (4, 4, 126, 1200.00, 'ALIPAY', 0, 'txn_456789', 'Awaiting payment', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);  -- 待支付


-- 创建结算表
CREATE TABLE settlement_bills (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,         -- 结算单ID（主键）
      order_id BIGINT NOT NULL,                      -- 订单ID（外键，关联到orders表）
      total_amount DECIMAL(10, 2) NOT NULL,          -- 结算总金额
      status TINYINT DEFAULT 0,                      -- 结算状态（0：待结算，1：已结算，2：已退款）
      payment_id BIGINT,                             -- 关联的支付交易ID（外键，关联到payment_transactions表）
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 插入结算单数据
INSERT INTO settlement_bills (order_id, total_amount, status, payment_id, created_at, updated_at)
VALUES
    (1, 1000.00, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- 结算单1，已结算
    (2, 1500.00, 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- 结算单2，已结算
    (3, 2000.00, 0, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- 结算单3，待结算
    (4, 1200.00, 0, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);  -- 结算单4，待结算

-- 创建退款表
CREATE TABLE refunds (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,         -- 退款ID（主键）
     order_id BIGINT NOT NULL,                     -- 订单ID（外键，关联到orders表）
     payment_transaction_id BIGINT NOT NULL,       -- 支付交易ID（外键，关联到payment_transactions表）
     settlement_bill_id BIGINT NOT NULL,           -- 结算单ID（外键，关联到settlement_bills表）
     refund_amount DECIMAL(10, 2) NOT NULL,        -- 退款金额
     refund_status TINYINT DEFAULT 0,              -- 退款状态（0：退款失败，1：退款成功）
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 插入退款数据
INSERT INTO refunds (order_id, payment_transaction_id, settlement_bill_id, refund_amount, refund_status, created_at, updated_at)
VALUES
    (1, 1, 1, 1000.00, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- 退款成功，订单1
    (2, 2, 2, 1500.00, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- 退款成功，订单2
    (3, 3, 3, 2000.00, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- 退款失败，订单3
    (4, 4, 4, 1200.00, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);  -- 退款失败，订单4
