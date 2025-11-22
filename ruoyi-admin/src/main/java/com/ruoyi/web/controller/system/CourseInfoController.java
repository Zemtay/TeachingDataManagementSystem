package com.ruoyi.web.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.CourseInfo;
import com.ruoyi.system.service.ICourseInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 课程信息Controller
 * 
 * @author ruoyi
 * @date 2025-11-05
 */
@RestController
@RequestMapping("/system/courseinfo") // 保持原有路径，避免前端修改
public class CourseInfoController extends BaseController
{
    @Autowired
    private ICourseInfoService courseInfoService;

    /**
     * 查询课程信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:courseinfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(CourseInfo courseInfo)
    {
        startPage();
        List<CourseInfo> list = courseInfoService.selectCourseInfoList(courseInfo);
        return getDataTable(list);
    }

    /**
     * 导出课程信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:courseinfo:export')")
    @Log(title = "课程信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CourseInfo courseInfo)
    {
        List<CourseInfo> list = courseInfoService.selectCourseInfoList(courseInfo);
        ExcelUtil<CourseInfo> util = new ExcelUtil<CourseInfo>(CourseInfo.class);
        util.exportExcel(response, list, "课程信息数据");
    }

    /**
     * 获取课程信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:courseinfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(courseInfoService.selectCourseInfoById(id));
    }

    /**
     * 新增课程信息
     */
    @PreAuthorize("@ss.hasPermi('system:courseinfo:add')")
    @Log(title = "课程信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CourseInfo courseInfo)
    {
        return toAjax(courseInfoService.insertCourseInfo(courseInfo));
    }

    /**
     * 修改课程信息
     */
    @PreAuthorize("@ss.hasPermi('system:courseinfo:edit')")
    @Log(title = "课程信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CourseInfo courseInfo)
    {
        return toAjax(courseInfoService.updateCourseInfo(courseInfo));
    }

    /**
     * 删除课程信息
     */
    @PreAuthorize("@ss.hasPermi('system:courseinfo:remove')")
    @Log(title = "课程信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(courseInfoService.deleteCourseInfoByIds(ids));
    }

    // ==============================================
    // 新增：供前端学情分析页面联调的3个接口
    // 权限统一用 query 权限，与原有查询权限保持一致
    // ==============================================

    /**
     * 接口1：获取创建课程数前5的教师
     */
    @PreAuthorize("@ss.hasPermi('system:courseinfo:query')")
    @GetMapping("/top5Creators")
    public AjaxResult getTop5CourseCreators()
    {
        Map<String, Object> info = new HashMap<> ();
        List<Map<String, Object>> list = courseInfoService.getTop5CourseCreators();
        String msg = "创建课程数量前5的教师名单如下：";
        for(Map<String, Object> map:list){
            msg += String.format("工号%s，创建课程总数%d；", map.get("creatorId"), map.get("courseCount"));
        }
        info.put("list", list);
        info.put("msg", msg);
        return success(info); // 用若依自带的success方法，保持格式统一
    }

    /**
     * 接口2：获取各课程的学生总数
     */
    @PreAuthorize("@ss.hasPermi('system:courseinfo:query')")
    @GetMapping("/studentCount")
    public AjaxResult getCourseStudentCount()
    {
        List<Map<String, Object>> list = courseInfoService.getCourseStudentCount();
        return success(list);
    }

    /**
     * 接口3：获取课程统计信息（对应页面ClassInfo）
     */
    @PreAuthorize("@ss.hasPermi('system:courseinfo:query')")
    @GetMapping("/classInfoStats")
    public AjaxResult getClassInfoStats()
    {
        Map<String, Object> stats = courseInfoService.getClassInfoStats();
        String msg = String.format("本学习总共开设%d门课程，开设了%d个班级。" +
                        "学生数量最多的课程是%s，有%d名学生。" +
                        "班级数量最多的课程是%s，有%d个班级。",
                stats.get("distinct_course_names"), stats.get("distinct_class_ids"),
                stats.get("top_course_name"), stats.get("top_course_students"),
                stats.get("avg_student_course"), stats.get("max_class_count"));

        stats.put("msg", msg);
        return success(stats);
    }
}