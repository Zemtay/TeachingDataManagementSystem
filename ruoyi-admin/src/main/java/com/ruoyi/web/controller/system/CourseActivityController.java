package com.ruoyi.web.controller.system;

import java.util.List;
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
import com.ruoyi.system.domain.CourseActivity;
import com.ruoyi.system.service.ICourseActivityService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.CourseInfo;
import com.ruoyi.system.service.ICourseInfoService;
import java.util.stream.Collectors;
import java.util.ArrayList;

/**
 * 课程活动Controller
 * 
 * @author ruoyi
 * @date 2025-11-05
 */
@RestController
@RequestMapping("/system/courseactivity")
public class CourseActivityController extends BaseController
{
    @Autowired
    private ICourseActivityService courseActivityService;

    @Autowired
    private ICourseInfoService courseInfoService;

    /**
     * 查询课程活动列表
     */
    @PreAuthorize("@ss.hasPermi('system:courseactivity:list')")
    @GetMapping("/list")
    public TableDataInfo list(CourseActivity courseActivity)
    {
        // 如果当前用户是 teacher：先查出该教师名下课程列表（不分页）
        if (SecurityUtils.hasRole("teacher")) {
            String teacherNo = SecurityUtils.getUsername();
            CourseInfo q = new CourseInfo();
            q.setCreatorId(teacherNo);
            List<CourseInfo> courseList = courseInfoService.selectCourseInfoList(q);
            if (courseList == null || courseList.isEmpty()) {
                return getDataTable(new ArrayList<>());
            }
            List<String> courseNames = courseList.stream()
                    .map(CourseInfo::getCourseName)
                    .collect(Collectors.toList());
            courseActivity.setCourseNameList(courseNames);
        }

        startPage();
        List<CourseActivity> list = courseActivityService.selectCourseActivityList(courseActivity);
        return getDataTable(list);
    }

    /**
     * 导出课程活动列表
     */
    @PreAuthorize("@ss.hasPermi('system:courseactivity:export')")
    @Log(title = "课程活动", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CourseActivity courseActivity)
    {
        List<CourseActivity> list = courseActivityService.selectCourseActivityList(courseActivity);
        ExcelUtil<CourseActivity> util = new ExcelUtil<CourseActivity>(CourseActivity.class);
        util.exportExcel(response, list, "课程活动数据");
    }

    /**
     * 获取课程活动详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:courseactivity:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(courseActivityService.selectCourseActivityById(id));
    }

    /**
     * 新增课程活动
     */
    @PreAuthorize("@ss.hasPermi('system:courseactivity:add')")
    @Log(title = "课程活动", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CourseActivity courseActivity)
    {
        return toAjax(courseActivityService.insertCourseActivity(courseActivity));
    }

    /**
     * 修改课程活动
     */
    @PreAuthorize("@ss.hasPermi('system:courseactivity:edit')")
    @Log(title = "课程活动", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CourseActivity courseActivity)
    {
        return toAjax(courseActivityService.updateCourseActivity(courseActivity));
    }

    /**
     * 删除课程活动
     */
    @PreAuthorize("@ss.hasPermi('system:courseactivity:remove')")
    @Log(title = "课程活动", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(courseActivityService.deleteCourseActivityByIds(ids));
    }
}
