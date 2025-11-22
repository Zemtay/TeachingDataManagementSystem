package com.ruoyi.web.controller.dashboard;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ruoyi.system.mapper.CourseInfoMapper;
import com.ruoyi.system.mapper.ClassInfoMapper;
import com.ruoyi.system.mapper.StudentMapper;
import com.ruoyi.system.mapper.CourseActivityMapper;

import com.ruoyi.system.domain.CourseInfo;
import com.ruoyi.system.domain.ClassInfo;
import com.ruoyi.system.domain.Student;
import com.ruoyi.system.domain.CourseActivity;



/**
 * 异步导入 Excel 数据的服务类
 */
@Service
public class ImportDataService {

    @Autowired
    private CourseInfoMapper courseInfoMapper;

    @Autowired
    private ClassInfoMapper classInfoMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private CourseActivityMapper courseActivityMapper;


    @Async
    public void processFileAsync(File file) {
        System.out.println("开始处理文件: " + file.getName());

        // 定义百分比字段集合
        Set<String> percentFields = new HashSet<>(Arrays.asList(
                "学生投票问卷参与率", "选人被选中次率", "学生抢答参与率",
                "学生评分参与率", "学生任务参与率"
        ));

        try (FileInputStream fis = new FileInputStream(file)) {
            Workbook workbook;

            if (file.getName().toLowerCase().endsWith(".xls")) {
                workbook = new HSSFWorkbook(fis);
            } else if (file.getName().toLowerCase().endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else {
                System.err.println("不支持的文件类型: " + file.getName());
                return;
            }

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                System.out.println("文件无有效数据，跳过: " + file.getName());
                workbook.close();
                return;
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                System.out.println("无法读取表头: " + file.getName());
                workbook.close();
                return;
            }

            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                cell.setCellType(CellType.STRING);
                headers.add(cell.getStringCellValue().trim());
            }

            // 定义表头集合
            Set<String> headersStudentParticipation = new HashSet<>(Arrays.asList(
                    "学号/工号", "课程数量", "任务点完成数", "视频任务点完成数",
                    "章节测验完成数", "作业完成数", "考试完成数", "章节学习次数",
                    "讨论总数", "发帖总数", "回帖总数", "课程积分",
                    "学生问卷参与数", "选人被选中次数", "学生抢答参与数",
                    "学生评分参与数", "学生任务参与数"
            ));

            Set<String> headersCourseInfo = new HashSet<>(Arrays.asList(
                    "课程名称", "课程编号/代码", "课程学期", "建课人工号/学号", "班级总数", "课程学生总数"
            ));

            Set<String> headersClassInfo = new HashSet<>(Arrays.asList(
                    "课程名称", "课程ID", "课程编号", "课程类型", "课程学生总数", "班级id", "教务课班级id"
            ));

            Set<String> headersCourseActivity = new HashSet<>(Arrays.asList(
                    "学号/工号", "课程名称", "课程编号", "课程创建时间", "教学班id",
                    "讨论总数", "发帖总数", "回帖总数", "课程积分",
                    "学生投票问卷参与数", "学生投票问卷参与率", "选人被选中次数", "选人被选中次率",
                    "学生抢答参与数", "学生抢答参与率", "学生评分参与数", "学生评分参与率",
                    "学生任务参与数", "学生任务参与率", "综合成绩", "证书发放状态"
            ));

            Set<String> headerSet = new HashSet<>(headers);
            String fileType = null;
            if (headerSet.containsAll(headersStudentParticipation)) {
                fileType = "student_participation";
            } else if (headerSet.containsAll(headersCourseInfo)) {
                fileType = "course_info";
            } else if (headerSet.containsAll(headersClassInfo)) {
                fileType = "class_info";
            } else if (headerSet.containsAll(headersCourseActivity)) {
                fileType = "course_activity";
            }

            if (fileType == null) {
                System.out.println("跳过文件: " + file.getName() + "（未知格式）");
                workbook.close();
                return;
            }

            int totalRows = sheet.getPhysicalNumberOfRows();
            List<Map<String, String>> rows = new ArrayList<>();

            for (int i = 1; i < totalRows; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> rowData = new LinkedHashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    String header = headers.get(j);
                    Cell cell = row.getCell(j);
                    String raw = (cell == null) ? null : getCellString(cell);
                    // 1) 将 '--' / '-' / 空字符串 规范化为 "0"
                    String normalized = normalizeCell(raw);
                    // 2) 如果是百分比字段，做百分号转数值（0-1）
                    if (percentFields.contains(header) && normalized != null && !normalized.isEmpty()) {
                        String pct = parsePercent(normalized); // 返回 "0.12" 形式的字符串，或 "0"、"" 等
                        rowData.put(header, pct);
                    } else {
                        rowData.put(header, normalized);
                    }
                }
                rows.add(rowData);
            }

            // 根据文件类型执行不同存储逻辑（调用你自己的保存方法）
            switch (fileType) {
                case "student_participation":
                    saveStudentParticipation(rows);
                    break;
                case "course_info":
                    saveCourseInfo(rows);
                    break;
                case "class_info":
                    saveClassInfo(rows);
                    break;
                case "course_activity":
                    saveCourseActivity(rows);
                    break;
            }

            System.out.println("文件处理完成: " + file.getName() + "，共 " + rows.size() + " 条记录。");
            workbook.close();

        } catch (IOException e) {
            System.err.println("处理文件出错: " + e.getMessage());
        }
    }

    /**
     * 把单元格内容取为字符串（尽量稳健）
     */
    private String getCellString(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.STRING) {
                return cell.getStringCellValue().trim();
            } else if (cell.getCellType() == CellType.NUMERIC) {
                if (DateUtil.isCellDateFormatted(cell)) {
                    // 日期按 ISO 字符串保存（按需调整）
                    return cell.getLocalDateTimeCellValue().toString();
                } else {
                    // 避免科学计数法、千分符等问题
                    double d = cell.getNumericCellValue();
                    // 如果是整数，去掉小数点
                    if (d == (long) d) {
                        return String.valueOf((long) d);
                    } else {
                        return String.valueOf(d);
                    }
                }
            } else if (cell.getCellType() == CellType.BOOLEAN) {
                return String.valueOf(cell.getBooleanCellValue());
            } else if (cell.getCellType() == CellType.FORMULA) {
                // 评估公式结果为字符串或数字
                try {
                    FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                    CellValue cellValue = evaluator.evaluate(cell);
                    if (cellValue.getCellType() == CellType.STRING) return cellValue.getStringValue().trim();
                    if (cellValue.getCellType() == CellType.NUMERIC) {
                        double d = cellValue.getNumberValue();
                        if (d == (long) d) return String.valueOf((long) d);
                        return String.valueOf(d);
                    }
                } catch (Exception ignored) { }
                return cell.toString().trim();
            } else {
                return cell.toString().trim();
            }
        } catch (Exception e) {
            return cell.toString().trim();
        }
    }

    /**
     * 规范化单元格字符串：将 '--', '-', '—', 空串 等替换成 "0"，并去掉前后空白和千分位逗号
     */
    private String normalizeCell(String s) {
        if (s == null) return "0";
        String t = s.trim();
        if (t.isEmpty()) return "0";
        // 常见表示缺失值的符号
        if (t.equals("--") || t.equals("-") || t.equals("—") || t.equals("NaN") || t.equals("null")) {
            return "0";
        }
        // 去掉千位分隔符(逗号)、全角空格
        t = t.replace(",", "").replace("，", "").replace("\u00A0", "").trim();
        return t;
    }

    /**
     * 解析百分比字符串，返回 0~1 之间的字符串（例如 "12%" -> "0.12"，"12" -> "0.12"）
     * 如果解析失败则返回 "0"
     */
    private String parsePercent(String s) {
        if (s == null) return "0";
        String t = s.trim();
        if (t.isEmpty()) return "0";
        // 去掉百分号及空格
        t = t.replace("%", "").replace("％", "").trim();
        t = t.replace(",", "").replace("，", ""); // 去千分符
        if (t.isEmpty()) return "0";
        try {
            double val = Double.parseDouble(t);
            // 如果原始值看起来像 0~1 之间（例如 0.12），就直接返回
            if (Math.abs(val) <= 1.0 && t.contains(".")) {
                return String.valueOf(val);
            }
            // 否则按百分数解释，除以100
            double result = val / 100.0;
            return String.valueOf(result);
        } catch (NumberFormatException e) {
            return "0";
        }
    }

    // 通用解析函数
    private Long parseLong(String s) {
        try {
            return (s == null || s.trim().isEmpty() || "--".equals(s.trim()))
                    ? 0L
                    : Long.parseLong(s.trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }


    private BigDecimal parseDecimal(String s) {
        try {
            return (s == null || s.trim().isEmpty() || "--".equals(s.trim()))
                    ? BigDecimal.ZERO : new BigDecimal(s.trim());
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal parseDecimalPercent(String s) {
        try {
            if (s == null || s.trim().isEmpty() || "--".equals(s.trim())) return BigDecimal.ZERO;
            s = s.replace("%", "").trim();
            return new BigDecimal(s).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }


    /** 以下四个方法分别对应四类数据的入库逻辑 **/

    private void saveCourseInfo(List<Map<String, String>> rows) {
        List<CourseInfo> list = new ArrayList<>();
        for (Map<String, String> row : rows) {
            CourseInfo info = new CourseInfo();
            info.setCourseName(row.getOrDefault("课程名称", ""));
            info.setCourseNum(row.getOrDefault("课程编号/代码", ""));
            info.setCourseSemester(row.getOrDefault("课程学期", ""));
            info.setCreatorId(row.getOrDefault("建课人工号/学号", ""));
            info.setClassCount(parseLong(row.get("班级总数")));
            info.setTotalStudents(parseLong(row.get("课程学生总数")));
            list.add(info);
        }
        courseInfoMapper.deleteAll();
        if (!list.isEmpty()) {
            courseInfoMapper.batchInsert(list);
        }
    }

    private void saveClassInfo(List<Map<String, String>> rows) {
        List<ClassInfo> list = new ArrayList<>();
        for (Map<String, String> row : rows) {
            ClassInfo info = new ClassInfo();
            info.setCourseName(row.getOrDefault("课程名称", ""));
            info.setCourseId(row.getOrDefault("课程ID", ""));
            info.setCourseNum(row.getOrDefault("课程编号", ""));
            info.setCourseType(row.getOrDefault("课程类型", ""));
            info.setTotalStudents(parseLong(row.get("课程学生总数")));
            info.setClassId(row.getOrDefault("班级id", ""));
            info.setEaclassId(row.getOrDefault("教务课班级id", ""));
            list.add(info);
        }
        classInfoMapper.deleteAll();
        if (!list.isEmpty()) {
            classInfoMapper.batchInsert(list);
        }
    }

    private void saveStudentParticipation(List<Map<String, String>> rows) {
        List<Student> list = new ArrayList<>();
        Set<String> seenIds = new HashSet<>();  // 用于去重

        for (Map<String, String> row : rows) {
            String studentId = row.getOrDefault("学号/工号", "").trim();
            if (studentId.isEmpty() || seenIds.contains(studentId)) {
                // 空值或已经存在的 studentId，跳过
                continue;
            }
            seenIds.add(studentId);  // 标记已加入
            Student s = new Student();
            s.setStudentId(row.getOrDefault("学号/工号", ""));
            s.setCourseCount(parseLong(row.get("课程数量")));
            s.setTasksCompleted(parseLong(row.get("任务点完成数")));
            s.setVideoTasksCompleted(parseLong(row.get("视频任务点完成数")));
            s.setChapterTestsCompleted(parseLong(row.get("章节测验完成数")));
            s.setHomeworkCompleted(parseLong(row.get("作业完成数")));
            s.setExamsCompleted(parseLong(row.get("考试完成数")));
            s.setChapterStudyCount(parseLong(row.get("章节学习次数")));
            s.setTotalDiscussions(parseLong(row.get("讨论总数")));
            s.setTotalPosts(parseLong(row.get("发帖总数")));
            s.setTotalReplies(parseLong(row.get("回帖总数")));
            s.setCoursePoints(parseDecimal(row.get("课程积分")));
            s.setStudentVotes(parseLong(row.get("学生问卷参与数")));
            s.setSelectedCount(parseLong(row.get("选人被选中次数")));
            s.setStudentAnswers(parseLong(row.get("学生抢答参与数")));
            s.setStudentRatings(parseLong(row.get("学生评分参与数")));
            s.setStudentTasks(parseLong(row.get("学生任务参与数")));
            list.add(s);
        }
        studentMapper.deleteAll();
        if (!list.isEmpty()) {
            studentMapper.batchInsert(list);
        }
    }

    private void saveCourseActivity(List<Map<String, String>> rows) {
        List<CourseActivity> list = new ArrayList<>();
        Set<String> uniqueKeys = new HashSet<>();// 用于去重

        for (Map<String, String> row : rows) {
            String studentId = row.getOrDefault("学号/工号", "").trim();
            String classId = row.getOrDefault("教学班id", "").trim();

            // 组合唯一键
            String uniqueKey = studentId + "-" + classId;

            // 如果为空或已经出现过，跳过
            if (studentId.isEmpty() || classId.isEmpty() || !uniqueKeys.add(uniqueKey)) {
                continue;
            }

            CourseActivity ca = new CourseActivity();
            ca.setStudentId(row.getOrDefault("学号/工号", ""));
            ca.setCourseName(row.getOrDefault("课程名称", ""));
            ca.setClassId(row.getOrDefault("教学班id", ""));
            ca.setTotalDiscussions(parseLong(row.get("讨论总数")));
            ca.setTotalPosts(parseLong(row.get("发帖总数")));
            ca.setTotalReplies(parseLong(row.get("回帖总数")));
            ca.setCoursePoints(parseDecimal(row.get("课程积分")));
            ca.setStudentVotes(parseLong(row.get("学生投票问卷参与数")));
            ca.setStudentVoteRate(parseDecimalPercent(row.get("学生投票问卷参与率")));
            ca.setSelectedCount(parseLong(row.get("选人被选中次数")));
            ca.setSelectedRate(parseDecimalPercent(row.get("选人被选中次率")));
            ca.setStudentAnswers(parseLong(row.get("学生抢答参与数")));
            ca.setStudentAnswerRate(parseDecimalPercent(row.get("学生抢答参与率")));
            ca.setStudentRatings(parseLong(row.get("学生评分参与数")));
            ca.setStudentRatingRate(parseDecimalPercent(row.get("学生评分参与率")));
            ca.setStudentTasks(parseLong(row.get("学生任务参与数")));
            ca.setStudentTaskRate(parseDecimalPercent(row.get("学生任务参与率")));
            ca.setFinalScore(parseDecimal(row.get("综合成绩")));
            ca.setCertificateStatus(row.getOrDefault("证书发放状态", "否"));
            list.add(ca);
        }
        courseActivityMapper.deleteAll();
        if (!list.isEmpty()) {
            courseActivityMapper.batchInsert(list);
        }
    }

}
