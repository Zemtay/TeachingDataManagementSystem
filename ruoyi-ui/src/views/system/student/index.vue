<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="课程数量" prop="courseCount">
        <el-input
          v-model="queryParams.courseCount"
          placeholder="请输入课程数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务点完成数" prop="tasksCompleted">
        <el-input
          v-model="queryParams.tasksCompleted"
          placeholder="请输入任务点完成数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="视频任务点完成数" prop="videoTasksCompleted">
        <el-input
          v-model="queryParams.videoTasksCompleted"
          placeholder="请输入视频任务点完成数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="章节测验完成数" prop="chapterTestsCompleted">
        <el-input
          v-model="queryParams.chapterTestsCompleted"
          placeholder="请输入章节测验完成数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="作业完成数" prop="homeworkCompleted">
        <el-input
          v-model="queryParams.homeworkCompleted"
          placeholder="请输入作业完成数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="考试完成数" prop="examsCompleted">
        <el-input
          v-model="queryParams.examsCompleted"
          placeholder="请输入考试完成数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="章节学习次数" prop="chapterStudyCount">
        <el-input
          v-model="queryParams.chapterStudyCount"
          placeholder="请输入章节学习次数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="讨论总数" prop="totalDiscussions">
        <el-input
          v-model="queryParams.totalDiscussions"
          placeholder="请输入讨论总数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发帖总数" prop="totalPosts">
        <el-input
          v-model="queryParams.totalPosts"
          placeholder="请输入发帖总数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="回帖总数" prop="totalReplies">
        <el-input
          v-model="queryParams.totalReplies"
          placeholder="请输入回帖总数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="课程积分" prop="coursePoints">
        <el-input
          v-model="queryParams.coursePoints"
          placeholder="请输入课程积分"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="学生问卷参与数" prop="studentVotes">
        <el-input
          v-model="queryParams.studentVotes"
          placeholder="请输入学生问卷参与数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="选人被选中次数" prop="selectedCount">
        <el-input
          v-model="queryParams.selectedCount"
          placeholder="请输入选人被选中次数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="学生抢答参与数" prop="studentAnswers">
        <el-input
          v-model="queryParams.studentAnswers"
          placeholder="请输入学生抢答参与数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="学生评分参与数" prop="studentRatings">
        <el-input
          v-model="queryParams.studentRatings"
          placeholder="请输入学生评分参与数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="学生任务参与数" prop="studentTasks">
        <el-input
          v-model="queryParams.studentTasks"
          placeholder="请输入学生任务参与数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:student:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:student:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:student:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:student:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="studentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="学号/工号" align="center" prop="studentId" />
      <el-table-column label="课程数量" align="center" prop="courseCount" />
      <el-table-column label="任务点完成数" align="center" prop="tasksCompleted" />
      <el-table-column label="视频任务点完成数" align="center" prop="videoTasksCompleted" />
      <el-table-column label="章节测验完成数" align="center" prop="chapterTestsCompleted" />
      <el-table-column label="作业完成数" align="center" prop="homeworkCompleted" />
      <el-table-column label="考试完成数" align="center" prop="examsCompleted" />
      <el-table-column label="章节学习次数" align="center" prop="chapterStudyCount" />
      <el-table-column label="讨论总数" align="center" prop="totalDiscussions" />
      <el-table-column label="发帖总数" align="center" prop="totalPosts" />
      <el-table-column label="回帖总数" align="center" prop="totalReplies" />
      <el-table-column label="课程积分" align="center" prop="coursePoints" />
      <el-table-column label="学生问卷参与数" align="center" prop="studentVotes" />
      <el-table-column label="选人被选中次数" align="center" prop="selectedCount" />
      <el-table-column label="学生抢答参与数" align="center" prop="studentAnswers" />
      <el-table-column label="学生评分参与数" align="center" prop="studentRatings" />
      <el-table-column label="学生任务参与数" align="center" prop="studentTasks" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:student:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:student:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改学生信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="课程数量" prop="courseCount">
          <el-input v-model="form.courseCount" placeholder="请输入课程数量" />
        </el-form-item>
        <el-form-item label="任务点完成数" prop="tasksCompleted">
          <el-input v-model="form.tasksCompleted" placeholder="请输入任务点完成数" />
        </el-form-item>
        <el-form-item label="视频任务点完成数" prop="videoTasksCompleted">
          <el-input v-model="form.videoTasksCompleted" placeholder="请输入视频任务点完成数" />
        </el-form-item>
        <el-form-item label="章节测验完成数" prop="chapterTestsCompleted">
          <el-input v-model="form.chapterTestsCompleted" placeholder="请输入章节测验完成数" />
        </el-form-item>
        <el-form-item label="作业完成数" prop="homeworkCompleted">
          <el-input v-model="form.homeworkCompleted" placeholder="请输入作业完成数" />
        </el-form-item>
        <el-form-item label="考试完成数" prop="examsCompleted">
          <el-input v-model="form.examsCompleted" placeholder="请输入考试完成数" />
        </el-form-item>
        <el-form-item label="章节学习次数" prop="chapterStudyCount">
          <el-input v-model="form.chapterStudyCount" placeholder="请输入章节学习次数" />
        </el-form-item>
        <el-form-item label="讨论总数" prop="totalDiscussions">
          <el-input v-model="form.totalDiscussions" placeholder="请输入讨论总数" />
        </el-form-item>
        <el-form-item label="发帖总数" prop="totalPosts">
          <el-input v-model="form.totalPosts" placeholder="请输入发帖总数" />
        </el-form-item>
        <el-form-item label="回帖总数" prop="totalReplies">
          <el-input v-model="form.totalReplies" placeholder="请输入回帖总数" />
        </el-form-item>
        <el-form-item label="课程积分" prop="coursePoints">
          <el-input v-model="form.coursePoints" placeholder="请输入课程积分" />
        </el-form-item>
        <el-form-item label="学生问卷参与数" prop="studentVotes">
          <el-input v-model="form.studentVotes" placeholder="请输入学生问卷参与数" />
        </el-form-item>
        <el-form-item label="选人被选中次数" prop="selectedCount">
          <el-input v-model="form.selectedCount" placeholder="请输入选人被选中次数" />
        </el-form-item>
        <el-form-item label="学生抢答参与数" prop="studentAnswers">
          <el-input v-model="form.studentAnswers" placeholder="请输入学生抢答参与数" />
        </el-form-item>
        <el-form-item label="学生评分参与数" prop="studentRatings">
          <el-input v-model="form.studentRatings" placeholder="请输入学生评分参与数" />
        </el-form-item>
        <el-form-item label="学生任务参与数" prop="studentTasks">
          <el-input v-model="form.studentTasks" placeholder="请输入学生任务参与数" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listStudent, getStudent, delStudent, addStudent, updateStudent } from "@/api/system/student"

export default {
  name: "Student",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 学生信息表格数据
      studentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        courseCount: null,
        tasksCompleted: null,
        videoTasksCompleted: null,
        chapterTestsCompleted: null,
        homeworkCompleted: null,
        examsCompleted: null,
        chapterStudyCount: null,
        totalDiscussions: null,
        totalPosts: null,
        totalReplies: null,
        coursePoints: null,
        studentVotes: null,
        selectedCount: null,
        studentAnswers: null,
        studentRatings: null,
        studentTasks: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询学生信息列表 */
    getList() {
      this.loading = true
      listStudent(this.queryParams).then(response => {
        this.studentList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        studentId: null,
        courseCount: null,
        tasksCompleted: null,
        videoTasksCompleted: null,
        chapterTestsCompleted: null,
        homeworkCompleted: null,
        examsCompleted: null,
        chapterStudyCount: null,
        totalDiscussions: null,
        totalPosts: null,
        totalReplies: null,
        coursePoints: null,
        studentVotes: null,
        selectedCount: null,
        studentAnswers: null,
        studentRatings: null,
        studentTasks: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.studentId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加学生信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const studentId = row.studentId || this.ids
      getStudent(studentId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改学生信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.studentId != null) {
            updateStudent(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addStudent(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const studentIds = row.studentId || this.ids
      this.$modal.confirm('是否确认删除学生信息编号为"' + studentIds + '"的数据项？').then(function() {
        return delStudent(studentIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/student/export', {
        ...this.queryParams
      }, `student_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
