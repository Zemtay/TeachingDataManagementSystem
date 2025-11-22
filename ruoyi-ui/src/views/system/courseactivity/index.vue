<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="学号/工号" prop="studentId">
        <el-input
          v-model="queryParams.studentId"
          placeholder="请输入学号/工号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="课程名称" prop="courseName">
        <el-input
          v-model="queryParams.courseName"
          placeholder="请输入课程名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="教学班ID" prop="classId">
        <el-input
          v-model="queryParams.classId"
          placeholder="请输入教学班ID"
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
      <el-form-item label="学生投票问卷参与数" prop="studentVotes">
        <el-input
          v-model="queryParams.studentVotes"
          placeholder="请输入学生投票问卷参与数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="学生投票问卷参与率" prop="studentVoteRate">
        <el-input
          v-model="queryParams.studentVoteRate"
          placeholder="请输入学生投票问卷参与率"
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
      <el-form-item label="选人被选中次率" prop="selectedRate">
        <el-input
          v-model="queryParams.selectedRate"
          placeholder="请输入选人被选中次率"
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
      <el-form-item label="学生抢答参与率" prop="studentAnswerRate">
        <el-input
          v-model="queryParams.studentAnswerRate"
          placeholder="请输入学生抢答参与率"
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
      <el-form-item label="学生评分参与率" prop="studentRatingRate">
        <el-input
          v-model="queryParams.studentRatingRate"
          placeholder="请输入学生评分参与率"
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
      <el-form-item label="学生任务参与率" prop="studentTaskRate">
        <el-input
          v-model="queryParams.studentTaskRate"
          placeholder="请输入学生任务参与率"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="综合成绩" prop="finalScore">
        <el-input
          v-model="queryParams.finalScore"
          placeholder="请输入综合成绩"
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
          v-hasPermi="['system:courseactivity:add']"
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
          v-hasPermi="['system:courseactivity:edit']"
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
          v-hasPermi="['system:courseactivity:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:courseactivity:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="courseactivityList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="学号/工号" align="center" prop="studentId" />
      <el-table-column label="课程名称" align="center" prop="courseName" />
      <el-table-column label="教学班ID" align="center" prop="classId" />
      <el-table-column label="讨论总数" align="center" prop="totalDiscussions" />
      <el-table-column label="发帖总数" align="center" prop="totalPosts" />
      <el-table-column label="回帖总数" align="center" prop="totalReplies" />
      <el-table-column label="课程积分" align="center" prop="coursePoints" />
      <el-table-column label="学生投票问卷参与数" align="center" prop="studentVotes" />
      <el-table-column label="学生投票问卷参与率" align="center" prop="studentVoteRate" />
      <el-table-column label="选人被选中次数" align="center" prop="selectedCount" />
      <el-table-column label="选人被选中次率" align="center" prop="selectedRate" />
      <el-table-column label="学生抢答参与数" align="center" prop="studentAnswers" />
      <el-table-column label="学生抢答参与率" align="center" prop="studentAnswerRate" />
      <el-table-column label="学生评分参与数" align="center" prop="studentRatings" />
      <el-table-column label="学生评分参与率" align="center" prop="studentRatingRate" />
      <el-table-column label="学生任务参与数" align="center" prop="studentTasks" />
      <el-table-column label="学生任务参与率" align="center" prop="studentTaskRate" />
      <el-table-column label="综合成绩" align="center" prop="finalScore" />
      <el-table-column label="证书发放状态" align="center" prop="certificateStatus" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:courseactivity:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:courseactivity:remove']"
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

    <!-- 添加或修改课程活动对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="学号/工号" prop="studentId">
          <el-input v-model="form.studentId" placeholder="请输入学号/工号" />
        </el-form-item>
        <el-form-item label="课程名称" prop="courseName">
          <el-input v-model="form.courseName" placeholder="请输入课程名称" />
        </el-form-item>
        <el-form-item label="教学班ID" prop="classId">
          <el-input v-model="form.classId" placeholder="请输入教学班ID" />
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
        <el-form-item label="学生投票问卷参与数" prop="studentVotes">
          <el-input v-model="form.studentVotes" placeholder="请输入学生投票问卷参与数" />
        </el-form-item>
        <el-form-item label="学生投票问卷参与率" prop="studentVoteRate">
          <el-input v-model="form.studentVoteRate" placeholder="请输入学生投票问卷参与率" />
        </el-form-item>
        <el-form-item label="选人被选中次数" prop="selectedCount">
          <el-input v-model="form.selectedCount" placeholder="请输入选人被选中次数" />
        </el-form-item>
        <el-form-item label="选人被选中次率" prop="selectedRate">
          <el-input v-model="form.selectedRate" placeholder="请输入选人被选中次率" />
        </el-form-item>
        <el-form-item label="学生抢答参与数" prop="studentAnswers">
          <el-input v-model="form.studentAnswers" placeholder="请输入学生抢答参与数" />
        </el-form-item>
        <el-form-item label="学生抢答参与率" prop="studentAnswerRate">
          <el-input v-model="form.studentAnswerRate" placeholder="请输入学生抢答参与率" />
        </el-form-item>
        <el-form-item label="学生评分参与数" prop="studentRatings">
          <el-input v-model="form.studentRatings" placeholder="请输入学生评分参与数" />
        </el-form-item>
        <el-form-item label="学生评分参与率" prop="studentRatingRate">
          <el-input v-model="form.studentRatingRate" placeholder="请输入学生评分参与率" />
        </el-form-item>
        <el-form-item label="学生任务参与数" prop="studentTasks">
          <el-input v-model="form.studentTasks" placeholder="请输入学生任务参与数" />
        </el-form-item>
        <el-form-item label="学生任务参与率" prop="studentTaskRate">
          <el-input v-model="form.studentTaskRate" placeholder="请输入学生任务参与率" />
        </el-form-item>
        <el-form-item label="综合成绩" prop="finalScore">
          <el-input v-model="form.finalScore" placeholder="请输入综合成绩" />
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
import { listCourseactivity, getCourseactivity, delCourseactivity, addCourseactivity, updateCourseactivity } from "@/api/system/courseactivity"

export default {
  name: "Courseactivity",
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
      // 课程活动表格数据
      courseactivityList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        studentId: null,
        courseName: null,
        classId: null,
        totalDiscussions: null,
        totalPosts: null,
        totalReplies: null,
        coursePoints: null,
        studentVotes: null,
        studentVoteRate: null,
        selectedCount: null,
        selectedRate: null,
        studentAnswers: null,
        studentAnswerRate: null,
        studentRatings: null,
        studentRatingRate: null,
        studentTasks: null,
        studentTaskRate: null,
        finalScore: null,
        certificateStatus: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        studentId: [
          { required: true, message: "学号/工号不能为空", trigger: "blur" }
        ],
        classId: [
          { required: true, message: "教学班ID不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询课程活动列表 */
    getList() {
      this.loading = true
      listCourseactivity(this.queryParams).then(response => {
        this.courseactivityList = response.rows
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
        id: null,
        studentId: null,
        courseName: null,
        classId: null,
        totalDiscussions: null,
        totalPosts: null,
        totalReplies: null,
        coursePoints: null,
        studentVotes: null,
        studentVoteRate: null,
        selectedCount: null,
        selectedRate: null,
        studentAnswers: null,
        studentAnswerRate: null,
        studentRatings: null,
        studentRatingRate: null,
        studentTasks: null,
        studentTaskRate: null,
        finalScore: null,
        certificateStatus: null
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加课程活动"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getCourseactivity(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改课程活动"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCourseactivity(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addCourseactivity(this.form).then(response => {
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
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除课程活动编号为"' + ids + '"的数据项？').then(function() {
        return delCourseactivity(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/courseactivity/export', {
        ...this.queryParams
      }, `courseactivity_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
