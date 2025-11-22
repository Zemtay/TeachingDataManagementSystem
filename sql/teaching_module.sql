-- ================================================
-- 智能教学助手模块 SQL
-- 适配 RuoYi 框架
-- ================================================

-- 1. 课程信息表
DROP TABLE IF EXISTS course_info;
CREATE TABLE course_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    course_name VARCHAR(100) NOT NULL COMMENT '课程名称',
    course_num VARCHAR(50) NOT NULL COMMENT '课程编号',
    course_semester VARCHAR(50) DEFAULT NULL COMMENT '课程学期',
    creator_id VARCHAR(50) DEFAULT NULL COMMENT '建课人工号/学号',
    class_count INT DEFAULT 0 COMMENT '班级总数',
    total_students INT DEFAULT 0 COMMENT '课程学生总数'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程信息表';


-- 2. 班级信息表
DROP TABLE IF EXISTS class_info;
CREATE TABLE class_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    course_name VARCHAR(100) NOT NULL COMMENT '课程名称',
    course_id VARCHAR(50) NOT NULL COMMENT '课程ID',
    course_num VARCHAR(50) DEFAULT NULL COMMENT '课程编号',
    course_type VARCHAR(50) DEFAULT NULL COMMENT '课程类型',
    total_students INT DEFAULT 0 COMMENT '课程学生总数',
    class_id VARCHAR(50) NOT NULL COMMENT '班级ID',
    EAclass_id VARCHAR(50) DEFAULT NULL COMMENT '教务课班级ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级信息表';


-- 3. 学生信息表
DROP TABLE IF EXISTS student;
CREATE TABLE student (
    student_id VARCHAR(50) PRIMARY KEY COMMENT '学号/工号',
    course_count INT DEFAULT 0 COMMENT '课程数量',
    tasks_completed INT DEFAULT 0 COMMENT '任务点完成数',
    video_tasks_completed INT DEFAULT 0 COMMENT '视频任务点完成数',
    chapter_tests_completed INT DEFAULT 0 COMMENT '章节测验完成数',
    homework_completed INT DEFAULT 0 COMMENT '作业完成数',
    exams_completed INT DEFAULT 0 COMMENT '考试完成数',
    chapter_study_count INT DEFAULT 0 COMMENT '章节学习次数',
    total_discussions INT DEFAULT 0 COMMENT '讨论总数',
    total_posts INT DEFAULT 0 COMMENT '发帖总数',
    total_replies INT DEFAULT 0 COMMENT '回帖总数',
    course_points DECIMAL(10,2) DEFAULT 0 COMMENT '课程积分',
    student_votes INT DEFAULT 0 COMMENT '学生问卷参与数',
    selected_count INT DEFAULT 0 COMMENT '选人被选中次数',
    student_answers INT DEFAULT 0 COMMENT '学生抢答参与数',
    student_ratings INT DEFAULT 0 COMMENT '学生评分参与数',
    student_tasks INT DEFAULT 0 COMMENT '学生任务参与数'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生信息表';


-- 4. 课程活动表
DROP TABLE IF EXISTS course_activity;
CREATE TABLE course_activity (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    student_id VARCHAR(50) NOT NULL COMMENT '学号/工号',
    course_name VARCHAR(100) DEFAULT NULL COMMENT '课程名称',
    class_id VARCHAR(50) NOT NULL COMMENT '教学班ID',
    total_discussions INT DEFAULT 0 COMMENT '讨论总数',
    total_posts INT DEFAULT 0 COMMENT '发帖总数',
    total_replies INT DEFAULT 0 COMMENT '回帖总数',
    course_points DECIMAL(10,2) DEFAULT 0 COMMENT '课程积分',
    student_votes INT DEFAULT 0 COMMENT '学生投票问卷参与数',
    student_vote_rate DECIMAL(5,2) DEFAULT 0 COMMENT '学生投票问卷参与率',
    selected_count INT DEFAULT 0 COMMENT '选人被选中次数',
    selected_rate DECIMAL(5,2) DEFAULT 0 COMMENT '选人被选中次率',
    student_answers INT DEFAULT 0 COMMENT '学生抢答参与数',
    student_answer_rate DECIMAL(5,2) DEFAULT 0 COMMENT '学生抢答参与率',
    student_ratings INT DEFAULT 0 COMMENT '学生评分参与数',
    student_rating_rate DECIMAL(5,2) DEFAULT 0 COMMENT '学生评分参与率',
    student_tasks INT DEFAULT 0 COMMENT '学生任务参与数',
    student_task_rate DECIMAL(5,2) DEFAULT 0 COMMENT '学生任务参与率',
    final_score DECIMAL(10,2) DEFAULT 0 COMMENT '综合成绩',
    certificate_status VARCHAR(10) DEFAULT '否' COMMENT '证书发放状态（是/否）',
    UNIQUE KEY uq_student_class (student_id, class_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程活动表';