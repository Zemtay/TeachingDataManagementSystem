# API 文档
本 API 文档基于我们的代码仓库 `ruoyi-system/src/main/java/com/ruoyi/system`，梳理 domain（实体对象）、service（业务接口）、mapper（数据访问/SQL）的典型能力。每个模块均按 RESTful 标准，可批量、条件、高级统计，涵盖常见通用及教务业务场景。

---

## 1. 用户管理接口（SysUser）

**数据模型：**
见 `SysUser` （用户ID、用户名、部门、状态、角色等字段）。

**主要API能力（接口 + SQL逻辑）：**
- 列表查询（支持分页、条件筛选）
    - `GET /system/user/list`
    - 参数示例：`userName`, `deptId`, `status` 等
    - SQL：`SysUserMapper.selectUserList(SysUser sysUser)`
- 查看单个用户
    - `GET /system/user/{userId}`
    - SQL：`SysUserMapper.selectUserById(Long userId)`
- 新增/修改/删除
    - `POST /system/user`, `PUT /system/user`, `DELETE /system/user/{userId}`
- 批量删除
    - `DELETE /system/user` + ID数组

> 返回对象均为 SysUser 或 List<SysUser>，响应结构标准 AjaxResult。

---

## 2. 角色管理接口（SysRole）

**数据模型：**
见 `SysRole` （角色ID、名称、权限码、状态等）

**主要API能力：**
- 列表查询/单条查询
    - `GET /system/role/list`
    - SQL：`SysRoleMapper.selectRoleList(SysRole role)`
    - `GET /system/role/{roleId}`
- 新增/修改/删除
    - `POST /system/role`, `PUT /system/role`, `DELETE /system/role/{roleId}`
- 校验唯一性、分配用户等功能
    - `checkRoleNameUnique`, `countUserRoleByRoleId`
- 权限点查询
    - `GET /system/role/permission/{userId}`

---

## 3. 菜单权限接口（SysMenu）

**数据模型：**
`SysMenu`（菜单ID、父ID、名称、路由、权限、类型等）

**API能力：**
- 菜单树/权限点查询
    - `GET /system/menu/list`, `GET /system/menu/{menuId}`, `GET /system/menu/treeselect`, `GET /system/menu/permissions/{userId}`
    - SQL：`SysMenuMapper.selectMenuList(SysMenu menu)` 等
- 新增/编辑/删除菜单
    - `POST /system/menu`, `PUT /system/menu`, `DELETE /system/menu/{menuId}`

---

## 4. 部门管理接口（SysDept）

**数据模型：**
`SysDept`（部门ID、名称、排序、状态、父ID等）

**API能力：**
- 部门树、列表/详情
    - `GET /system/dept/list`, `GET /system/dept/tree`
    - SQL：`ISysDeptService.selectDeptList(SysDept dept)`
- 新增/编辑/删除
    - `POST /system/dept`, `PUT /system/dept`, `DELETE /system/dept/{deptId}`
- 校验唯一、是否有用户/子部门

---

## 5. 用户岗位接口（SysPost）

**数据模型：**
`SysPost`（岗位ID、编码、名称、排序、状态）

**典型API：**
- 列表/详情：`GET /system/post/list`, `GET /system/post/{postId}`
- 新增/编辑/删除（批量支持）：`POST/PUT/DELETE /system/post`
- 用户岗位分配统计相关：`selectPostListByUserId`

---

## 6. 参数配置接口（SysConfig）

**数据模型：**
`SysConfig`（参数ID、key、value、类型）

**API能力：**
- 列表/详情/按key查值
    - `GET /system/config/list`（支持模糊条件）
    - `GET /system/config/{configId}`
    - `GET /system/config/configKey/{key}`
- 新增/编辑/删除
    - `POST/PUT/DELETE /system/config`
- 缓存管理
    - `DELETE /system/config/refreshCache`

---

## 7. 字典管理接口（SysDictType、SysDictData）

**数据模型：**
- `SysDictType`（类型ID、名称、编码等）
- `SysDictData`（数据ID、类型、标签、值、排序）

**API能力：**
- 类型/数据分页/条件查询、新增/修改/删除
    - `GET /system/dict/type/list`, `GET /system/dict/data/list`
    - `POST/PUT/DELETE /system/dict/type`, `POST/PUT/DELETE /system/dict/data`

---

## 8. 教务业务对象接口示例（ClassInfo、CourseInfo、Student）

### `ClassInfo`（班级信息）
- 主键 id，课程名、编号、类型、班级学生数等字段
- API：`GET /system/classinfo/list`, `GET /system/classinfo/{id}`，批量增删改（列表均支持组合条件查询）

### `CourseInfo`（课程信息）
- 课程id、名称、编号、创建人等
- API：同上，含 TOP N 创建榜、统计聚合 SQL

### `Student`（学生信息）
- 学号、课程/任务/积分/活跃度统计
- API：条件/排名/动态统计
    - `GET /system/student/list`, `GET /system/student/{studentId}`

---

## 9. 操作日志与登入日志接口

- `SysOperLog`（操作日志），`SysLogininfor`（系统访问记录）
- API能力：
    - 日志增删查分页，筛选
    - 清空日志
    - 仅后台管理可用

---

## 10. AI助手相关

- `AiChatHistory`（对话历史，userId, messages, updateTime）
- API能力：
    - 查找、保存、删除用户历史
    - SQL：`AiChatHistoryMapper.selectByUserId(userId)`, `insertOrUpdate`, `deleteByUserId`

---

## 11. 动态SQL/高级查询接口

- `DynamicQueryMapper.dynamicSelect(tableName, conditions)`
- `dynamicQuery(sql)`
- 返回任意表、任意条件的数据集，适用于高级报表、灵活联表分析

---

## 12. 典型通用响应&错误处理

- AjaxResult、R 对象封装
- "code","msg","data" 标准返回
- 错误、参数合法性、权限、唯一校验均在 service 层集中处理

---

## 13. 查询/统计类 SQL示例

- Top榜、聚合分组、复杂条件都在 mapper 层实现，Service 调用对应方法返回 Map/List<Map>
    - 例：`selectTop5CourseCreators()`，`selectCourseStudentCount()`等

---

## 14. 支持批量操作与分页

所有 CRUD mapper均支持主键数组批量删除、分页参数自动处理。

---

## 查阅方式建议

查阅 API 能能力及模型时，建议优先关注：
- domain 下的实体类定义（决定所有接口参数和返回结构）
- service 接口注释和方法描述（代表 API 业务能力）
- mapper 方法与注释（直接映射所有 SQL 语句和操作）
- controller 层暴露的 URL 路径/参数对应（前端请求参考）

如需针对某对象/业务/SQL接口进一步详细分析，请指定 domain/service/mapper 名称。

---

[查看更多 service 文件](https://github.com/Zemtay/manage_system/search?q=service&type=code)  
[更多 mapper SQL 能力参考](https://github.com/Zemtay/manage_system/search?q=mapper&type=code)  
[所有 domain 数据模型定义](https://github.com/Zemtay/manage_system/search?q=domain&type=code)  