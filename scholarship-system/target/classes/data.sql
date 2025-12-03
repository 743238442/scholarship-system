-- 创建用户zhangsan
INSERT INTO tbl_user (username, password, real_name, email, phone, user_type, status, is_deleted, created_by, created_time, updated_by, updated_time)
VALUES ('zhangsan', '$2a$10$eR7w9zNq9zNq9zNq9zNq9u7wW7e7wW7e7wW7e7wW7e7wW7e7wW7e', '张三', 'zhangsan@example.com', '13800138000', 'STUDENT', 'ACTIVE', 0, 'system', NOW(), 'system', NOW());

-- 创建学生信息
INSERT INTO tbl_student (student_no, name, gender, college, major, clazz, grade, contact, enrollment_date, is_graduated, study_years, user_id, id_card, status, created_by, created_time, updated_by, updated_time)
VALUES ('20220001', '张三', 'MALE', '计算机学院', '计算机科学与技术', '计科2班', '2022', '13800138000', '2022-09-01', false, 4, (SELECT id FROM tbl_user WHERE username = 'zhangsan'), '110101200401011234', 'ACTIVE', 'system', NOW(), 'system', NOW());

-- 创建角色关联
INSERT INTO tbl_user_role (user_id, role_id)
VALUES ((SELECT id FROM tbl_user WHERE username = 'zhangsan'), (SELECT id FROM tbl_role WHERE name = 'STUDENT'));
