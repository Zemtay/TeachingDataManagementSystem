<template>
  <div class="study-info-container">
    <!-- 背景层 -->
    <div class="whole-box"></div>

    <!-- 图表弹窗 -->
    <div class="chart-box" v-show="chartBoxVisible">
      <div class="close">
        <svg @click="closeChartBox" width="34" height="34" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12 19 6.41Z" fill="currentColor"/>
        </svg>
      </div>
      <canvas id="courseStatsChart" ref="courseStatsChart"></canvas>
    </div>

    <!-- 智能报告弹窗 -->
    <div class="markdown-box" v-show="markdownBoxVisible">
      <div class="close">
        <svg @click="closeMarkdownBox" width="34" height="34" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12 19 6.41Z" fill="currentColor"/>
        </svg>
      </div>
      <h1>班级与学生活跃度分析报告</h1>

      <div class="section">
        <h2><i class="fas fa-users-viewfinder"></i> 1. 班级结构分析</h2>
        <ul>
          <li><strong>总览</strong>: {{ msg1 }} </li>
          <li><strong>建议</strong>: {{ AI1 }} </li>
        </ul>
      </div>

      <div class="section">
        <h2><i class="fas fa-user-minus"></i> 2. 学生学业分析</h2>
        <ul>
          <li><strong>总览</strong>: {{ msg2 }} </li>
          <li><strong>建议</strong>: {{ AI2 }}</li>
        </ul>
      </div>

      <div class="summary">
        <h2><i class="fas fa-chart-pie"></i> 总结</h2>
        <p> {{AI3}} </p>
      </div>
    </div>

    <!-- 主内容层 -->
    <div class="frosted-layer">
      <!-- 返回按钮 -->
      <div class="return-button" @click="goBack">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
          <path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z" fill="white" />
        </svg>
      </div>

      <!-- 数据卡片区域 -->
      <div class="dashboard">
        <!-- 1. 固定教师创建的班数 -->
        <div class="analysis-card attendance" @click="openChartBox('fixedTeacherClassBar')">
          <div class="card-header">
            <h2 class="card-title">📊 教学统计</h2>
          </div>
          <div class="card-content">
            <div class="stats-item">
              <span class="stats-label">教师工号: {{ fixedTeacher.teacherId }}</span>
            </div>
            <div class="stats-item">
              <span class="stats-value">创建班数: {{ classSum }}</span>
              <span class="stats-value">覆盖课程数: {{ courseSum }}</span>
              <span class="stats-value">总学生数: {{ studentSum }}</span>
            </div>
          </div>
        </div>

        <!-- 2. 每个班的人数 -->
        <div class="analysis-card performance" @click="openChartBox('classStudentBar')">
          <div class="card-header">
            <h2 class="card-title">🏫 班级详情</h2>
          </div>
          <div class="card-content">
            <div v-for="(course, index) in courseDetails" :key="index">
              <div class="stats-item">
                <span class="stats-label">课程名称：{{ course.name }}</span>
              </div>
              <div class="stats-item">
                <span class="stats-value">班级数: {{ course.classCount }}<br></span>
                <span class="stats-value">总学生数: {{ course.totalStudents }}<br></span>
                <span class="stats-value">平均每班学生数: {{ course.avgStudent }}<br></span>
              </div>
            </div>
          </div>
        </div>

        <!-- 3. 最不活跃的学生 -->
        <div class="analysis-card teacher-work" @click="openChartBox('inactiveStudentBar')">
          <div class="card-header">
            <h2 class="card-title">😴 学业预警 (任务完成数后5)</h2>
          </div>
          <div class="card-content">
            <div v-for="(course, index) in courseDetails" :key="index">
              <div class="stats-item">
                <span class="stats-label">课程名称：{{ course.name }}</span>
              </div>
              <li v-for="item in course.bottom5studentTasks" :key="item.studentId">
                  <span class="stats-value">学生ID: {{ item.studentId }}（完成数：{{ item.StudentTasks }}） <br> </span>
              </li>
            </div>
          </div>
        </div>

<!--        &lt;!&ndash; 班级信息汇总 &ndash;&gt;-->
<!--        <div class="analysis-card course-progress">-->
<!--          <div class="card-header">-->
<!--            <h2 class="card-title">📈 班级信息汇总</h2>-->
<!--          </div>-->
<!--          <div class="card-content">-->
<!--            <div class="stats-item">-->
<!--              <span class="stats-label">班级总数:</span>-->
<!--              <span class="stats-value">{{ animatedValues.fixedTeacherClassCount }}</span>-->
<!--            </div>-->
<!--            <div class="stats-item">-->
<!--              <span class="stats-label">学生总数:</span>-->
<!--              <span class="stats-value">{{ animatedValues.total_students }}</span>-->
<!--            </div>-->
<!--            <div class="stats-item">-->
<!--              <span class="stats-label">平均班额:</span>-->
<!--              <span class="stats-value">{{ animatedValues.avg_class_students }}</span>-->
<!--            </div>-->
<!--            <div class="stats-item">-->
<!--              <span class="stats-label">最低活跃度:</span>-->
<!--              <span class="stats-value">{{ animatedValues.min_activity }}</span>-->
<!--            </div>-->
<!--            <div class="stats-item">-->
<!--              <span class="stats-label">不活跃学生数:</span>-->
<!--              <span class="stats-value">{{ inactiveStudents.length }}</span>-->
<!--            </div>-->
<!--          </div>-->
<!--        </div>-->

        <!-- 智能报告生成按钮 -->
        <div class="analysis-card attendance">
          <div class="card-header">
            <h2 class="card-title">📑 报告中心</h2>
          </div>
          <div class="card-content">
            <div id="smartReportBtn" @click="openMarkdownBox">生成班级分析报告</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Vue from 'vue'
import Chart from 'chart.js'
import { getInfo } from "@/api/login";
import { getTeacherStudyinfo, getAiReport } from "@/api/studyInfo"

export default {
  name: 'StudyInfo',
  data() {
    return {
      chartBoxVisible: false,
      markdownBoxVisible: false,
      currentChartType: '',
      // 静态数据 - 固定教师信息
      fixedTeacher: { teacherId: ''},
      courseSum: 0,
      classSum: 0,
      studentSum: 0,
      courseDetails: [],
      classStudentCount: [],
      inactiveStudents: [],
      msg1: '',
      AI1: '智能助手正在分析中，请稍后......',
      msg2: '',
      AI2: '智能助手正在分析中，请稍后......',
      AI3: '智能助手正在分析中，请稍后......',
      chartInstance: null,
      // 动画数据
      animatedValues: {
        courseSum: 0,
        classSum: 0,
        studentSum: 0,
        courseDetails: [],
      }
    }
  },
  created(){
    this.loadInfo();
  },
  mounted() {
    // console.log('静态数据版本加载完成')
    // 直接初始化静态数据的动画，无需调用接口
    window.addEventListener('resize', this.resizeChart)
  },
  beforeDestroy() {
    if (this.chartInstance) {
      this.chartInstance.destroy()
    }
    window.removeEventListener('resize', this.resizeChart)
  },
  methods: {
    // 初始化动画数据（基于静态数据）
    loadInfo() {
      getInfo().then(res => {
        this.fixedTeacher.teacherId = res.user.userName;
        // console.log('getInfo返回的数据:', res)
      });
      getTeacherStudyinfo().then(res => {
        const data = res.data
        this.courseSum = data.course_sum
        this.classSum = data.class_sum
        this.studentSum = data.student_sum
        this.courseDetails = data.detail
        this.msg1 = data.msg1
        this.msg2 = data.msg2
        // console.log(this.courseDetails)

        const detail = data.detail
        this.classStudentCount = detail.map(item => ({
          className: item.name,
          studentCount: item.totalStudents
        }))

        this.inactiveStudents = detail.flatMap(item =>
          item.bottom5FinalScore.map(stu => ({
          studentId: stu.studentId,
          className: item.name,
          activity: stu.finalScore
        }))
        )

      }).catch(err => {
        console.error("获取教师课程信息失败", err)
      })
    },

    // 初始化图表（使用静态数据）
    initChart() {
      Vue.nextTick(() => {
        const ctx = this.$refs.courseStatsChart.getContext('2d')
        if (this.chartInstance) {
          this.chartInstance.destroy()
        }
        const chartConfig = this.getChartConfigByType()
        this.chartInstance = new Chart(ctx, chartConfig)
      })
    },

    // 图表配置（静态数据适配）
    getChartConfigByType() {
      const commonOptions = {
        animation: true,
        scales: {
          y: { beginAtZero: true, grid: { color: 'rgba(255,255,255,0.1)' }, ticks: { color: 'white' } },
          x: { grid: { display: false }, ticks: { color: 'white' } }
        },
        plugins: {
          legend: { display: true, labels: { font: { size: 14 }, color: 'white' } }
        }
      }

      switch (this.currentChartType) {

        // 2. 班级人数柱状图
        case 'classStudentBar':
          return {
            type: 'bar',
            data: {
              labels: this.classStudentCount.map(item => item.className),
              datasets: [{
                label: '课程人数',
                data: this.classStudentCount.map(item => item.studentCount),
                backgroundColor: 'rgba(153, 102, 255, 0.7)',
                borderColor: 'rgba(153, 102, 255, 1)',
                borderWidth: 2,
                borderRadius: 4
              }]
            },
            options: commonOptions
          }

        // 3. 不活跃学生柱状图
        case 'inactiveStudentBar':
          return {
            type: 'bar',
            data: {
              labels: this.inactiveStudents.map(item => `${item.studentId}(${item.className})`),
              datasets: [{
                label: '学生总成绩',
                data: this.inactiveStudents.map(item => item.finalScore),
                backgroundColor: 'rgba(255, 159, 64, 0.7)',
                borderColor: 'rgba(255, 159, 64, 1)',
                borderWidth: 2,
                borderRadius: 4
              }]
            },
            options: commonOptions
          }

        default:
          return {
            type: 'line',
            data: { labels: ['数据'], datasets: [{ label: '暂无数据', data: [0] }] },
            options: commonOptions
          }
      }
    },

    resizeChart() {
      if (this.chartInstance && this.chartBoxVisible) {
        this.chartInstance.resize()
      }
    },

    openChartBox(chartType) {
      this.currentChartType = chartType
      this.chartBoxVisible = true
      this.initChart()
    },

    closeChartBox() {
      this.chartBoxVisible = false
    },

    async openMarkdownBox() {
      this.markdownBoxVisible = true
      this.loading = true
      // console.log("输入：", this.msg1)
      this.AI1 = "智能助手正在分析中，请稍后......"
      this.AI2 = "智能助手正在分析中，请稍后......"
      this.AI3 = "智能助手正在分析中，请稍后......"
      const res = await getAiReport(this.msg1)
      // console.log("AI1返回：", res)
      this.AI1 = res.msg
      const res2 = await getAiReport(this.msg2)
      this.AI2 = res2.msg
      const res3 = await getAiReport(this.msg1 + this.msg2)
      this.AI3 = res3.msg
      this.loading = false
    },

    closeMarkdownBox() {
      this.markdownBoxVisible = false
    },

    goBack() {
      this.$router.push('/index')
    }
  }
}
</script>

<style scoped>
/* 样式保持不变，与之前一致 */
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
  font-family: 'Microsoft YaHei', sans-serif;
}

body {
  background: #1c2c57;
  padding: 20px;
}

.empty-tip {
  color: #fff;
  text-align: center;
  width: 100%;
  padding: 20px 0;
  font-size: 14px;
  opacity: 0.7;
}

.frosted-layer {
  position: fixed;
  top: 0 !important;
  left: 0 !important;
  width: 100vw;
  height: 100vh;
  overflow: auto;
  background: rgba(28, 44, 87, 0.8);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  z-index: 10000;
}

.whole-box {
  position: fixed;
  top: 50% !important;
  left: 50% !important;
  height: 100%;
  width: 100%;
  transform: translate(-50%, -50%);
  background: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(8px);
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  z-index: 100 !important;
}

.chart-box, .markdown-box {
  position: fixed;
  top: 50% !important;
  left: 50% !important;
  height: 90%;
  width: 80%;
  border-left: 4px solid #409eff;
  transform: translate(-50%, -50%);
  background: #1c2c57;
  background-color: #0e1f36;
  padding: 20px;
  z-index: 100010;
  color: white;
  font-size: small;
  overflow: auto;
  border-radius: 8px;
}

::-webkit-scrollbar {
  width: 7px;
  background-color: transparent;
}
::-webkit-scrollbar-thumb {
  background-color: #8eabd4;
  border-radius: 5px;
  box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
}
::-webkit-scrollbar-track {
  background-color: transparent;
  border-radius: 5px;
}
::-webkit-scrollbar-thumb:hover {
  background-color: #2c3e50;
}

.markdown-box h1 {
  text-align: center;
  padding: 20px;
  background-color: #1a3a6b;
  margin: 0;
  font-size: 24px;
}
.section {
  padding: 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}
.section:last-child {
  border-bottom: none;
}
.section h2 {
  display: flex;
  align-items: center;
  color: #4facfe;
  margin-top: 0;
  font-size: 18px;
}
.section h2 i {
  margin-right: 10px;
}
.section ul {
  padding-left: 20px;
  margin: 10px 0;
}
.section ul li {
  margin-bottom: 5px;
  color: #e1e1e1;
  line-height: 1.5;
}
.section ul li strong {
  color: white;
}
.summary {
  padding: 20px;
  background-color: rgba(255, 255, 255, 0.05);
}
.summary p {
  color: #e1e1e1;
  line-height: 1.6;
}

.dashboard {
  max-width: 1200px;
  margin: 80px auto 40px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  overflow: visible;
}
.analysis-card {
  background: #8eabd4;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s;
  cursor: pointer;
}
.analysis-card:hover {
  transform: translateY(-3px);
}
.card-header {
  border-bottom: 2px solid #e4e7ed;
  padding-bottom: 12px;
  margin-bottom: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.card-title {
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}
.card-content {
  height: 40vh;
  color: white;
  line-height: 1.6;
  overflow: auto;
}

.attendance { border-left: 4px solid #409eff; }
.performance { border-left: 4px solid #67c23a; }
.teacher-work { border-left: 4px solid #e6a23c; }
.course-progress { border-left: 4px solid #f56c6c; }

.stats-item {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px solid #ebeef5;
}
.stats-label {
  color: white;
}
.stats-value {
  color: #303133;
  font-weight: 500;
}

.return-button {
  position: fixed;
  top: 20px;
  left: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 56px;
  height: 56px;
  border-radius: 50%;
  cursor: pointer;
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
  z-index: 10001;
}
.return-button:hover {
  background-color: #8c93e260;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}
.return-button:active {
  background-color: #7d82c960;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}
.return-button svg {
  width: 30px;
  height: 30px;
  fill: white;
}

#smartReportBtn {
  margin: 20px 0 0;
  background-color: #8b99a9;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 15px 30px;
  font-size: 20px;
  cursor: pointer;
  writing-mode: initial;
  transition: all 0.3s ease;
  width: 100%;
  text-align: center;
}

#smartReportBtn:hover {
  background-color: #607992;
  transform: scale(1.02);
}

#smartReportBtn:active {
  background-color: #435f7c;
  transform: scale(0.98);
}

.close {
  padding-left: 95%;
  margin-bottom: 10px;
}
.close svg:hover {
  cursor: pointer;
  color: #e1e1e1;
}

@media (max-width: 768px) {
  .dashboard {
    grid-template-columns: 1fr;
    margin: 60px 20px 40px;
  }
  .chart-box, .markdown-box {
    width: 95%;
    height: 85%;
  }
  .close {
    padding-left: 90%;
  }
  #smartReportBtn {
    padding: 12px 24px;
    font-size: 18px;
  }
}
</style>
