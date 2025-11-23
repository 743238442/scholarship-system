-- 连接到数据库后执行以下SQL来检查约束定义
SELECT conname, condef 
FROM pg_constraint 
WHERE conrelid = 'tbl_review'::regclass 
AND contype = 'c';

-- 尝试插入一条测试记录来验证约束
INSERT INTO tbl_review (academic_year, review_status, student_id, scholarship_type_id) 
VALUES ('2024-2025', 'pending', 1, 1) 
ON CONFLICT DO NOTHING;

-- 查询插入是否成功
SELECT COUNT(*) FROM tbl_review WHERE review_status = 'pending';