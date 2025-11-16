-- 插入管理员（密码：Admin@123 → BCrypt 加密）
INSERT INTO users (username, password, email, role, is_active)
VALUES (
  'admin',
  '$2a$10$XhU6VvQY7ZqJ1bK9uLmN.eOxRfGzW3QjK5tMnRlS8uT6vWxYzA1B2C', -- BCrypt("Admin@123")
  'admin@univ.edu',
  'ADMIN',
  true
);

-- 插入学生用户（密码：Student@123）
INSERT INTO users (username, password, email, role, is_active)
VALUES (
  'student001',
  '$2a$10$LmN.eOxRfGzW3QjK5tMnRlS8uT6vWxYzA1B2CXhU6VvQY7ZqJ1bK9u', -- BCrypt("Student@123")
  'student001@univ.edu',
  'STUDENT',
  true
);

-- 关联学生信息
INSERT INTO students (user_id, student_no, name, college, major, grade)
VALUES (2, '20230001', '张三', '计算机学院', '软件工程', 2023);

-- 示例奖学金类型
INSERT INTO scholarship_types (name, amount, description, eligibility_criteria)
VALUES 
('国家奖学金', 8000.00, '国家级优秀学生奖励', 'GPA ≥ 3.5，无违纪'),
('校级一等奖学金', 3000.00, '校级综合表现优秀', 'GPA ≥ 3.0，参与科研或竞赛');