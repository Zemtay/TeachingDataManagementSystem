package com.ruoyi.web.controller.system;

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
import com.ruoyi.system.domain.ClassInfo;
import com.ruoyi.system.service.IClassInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.CourseInfo;
import com.ruoyi.system.service.ICourseInfoService;
import java.util.stream.Collectors;
import java.util.ArrayList;

/**
 * 班级信息Controller
 * 
 * @author ruoyi
 * @date 2025-11-05
 */
@RestController
@RequestMapping("/system/classinfo")
public class ClassInfoController extends BaseController
{
    @Autowired
    private IClassInfoService classInfoService;

    @Autowired
    private ICourseInfoService courseInfoService;

    /**
     * 查询班级信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:classinfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(ClassInfo classInfo)
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

            classInfo.setCourseNameList(courseNames);
        }

        startPage();
        List<ClassInfo> list = classInfoService.selectClassInfoList(classInfo);
        return getDataTable(list);
    }


    /**
     * 导出班级信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:classinfo:export')")
    @Log(title = "班级信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ClassInfo classInfo)
    {
        List<ClassInfo> list = classInfoService.selectClassInfoList(classInfo);
        ExcelUtil<ClassInfo> util = new ExcelUtil<ClassInfo>(ClassInfo.class);
        util.exportExcel(response, list, "班级信息数据");
    }

    /**
     * 获取班级信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:classinfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(classInfoService.selectClassInfoById(id));
    }

    /**
     * 新增班级信息
     */
    @PreAuthorize("@ss.hasPermi('system:classinfo:add')")
    @Log(title = "班级信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ClassInfo classInfo)
    {
        return toAjax(classInfoService.insertClassInfo(classInfo));
    }

    /**
     * 修改班级信息
     */
    @PreAuthorize("@ss.hasPermi('system:classinfo:edit')")
    @Log(title = "班级信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ClassInfo classInfo)
    {
        return toAjax(classInfoService.updateClassInfo(classInfo));
    }

    /**
     * 删除班级信息
     */
    @PreAuthorize("@ss.hasPermi('system:classinfo:remove')")
    @Log(title = "班级信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(classInfoService.deleteClassInfoByIds(ids));
    }

    /**
     * 新增：查询各课程的班级数（供前端学情分析页面调用）
     * 权限与查询班级详情一致，无需额外配置权限
     */
    @PreAuthorize("@ss.hasPermi('system:classinfo:query')")
    @GetMapping("/courseClassCount")
    public AjaxResult getCourseClassCount()
    {
        // 调用Service层方法，获取统计结果并返回
        List<Map<String, Object>> list = classInfoService.getCourseClassCount();
        return success(list);
    }
}