<template>
  <div class="study-info-container">
    <!-- 调试面板 -->
    <!-- <div class="debug-panel" v-if="debugMode">
      <h3>调试信息</h3>
      <div class="debug-section" v-for="(data, key) in debugData" :key="key">
        <h4>{{ key }}</h4>
        <pre>{{ JSON.stringify(data, null, 2) }}</pre>
      </div>
    </div> -->

    <!-- 背景层 -->
    <div class="whole-box"></div>

    <!-- 图表弹窗（通用：柱状图/折线图） -->
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
      <h1>课程数据及优化建议</h1>

      <div class="section">
        <h2><i class="fas fa-comments"></i> 1. 教师创建课程情况</h2>
        <ul>
          <li><strong>总览</strong>: {{ this.msg1 }} </li>
          <li><strong>建议</strong>: {{ this.AI1 }} </li>
        </ul>
      </div>

      <div class="section">
        <h2><i class="fas fa-tasks"></i> 2. 课程设置情况</h2>
        <ul>
          <li><strong>总览</strong>: {{ this.msg2 }}
          </li>
          <li><strong>建议</strong>: {{ this.AI2 }} </li>
        </ul>
      </div>

      <div class="section">
        <h2><i class="fas fa-star"></i> 3. 学生学习情况</h2>
        <ul>
          <li><strong>总览</strong>: {{ this.msg3 }}
          </li>
          <li><strong>建议</strong>: {{ this.AI3 }} </li>
        </ul>
      </div>

      <div class="summary">
        <h2><i class="fas fa-chart-line"></i> 总结</h2>
        <p> {{ this.AI4 }} </p>
      </div>
    </div>

    <!-- 主内容层（磨砂玻璃效果） -->
    <div class="frosted-layer">
      <!-- 返回按钮 -->
      <div class="return-button" @click="goBack">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
          <path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z" fill="white" />
        </svg>
      </div>

      <!-- 数据卡片区域 -->
      <div class="dashboard">
        <!-- 创建课程数前5的教师工号（原有：折线图） -->
        <div class="analysis-card attendance" id="chart1" @click="openChartBox('createClassLine')">
          <div class="card-header">
            <h2 class="card-title">📊 创建课程数前5的教师工号</h2>
            <!-- <button class="debug-btn" @click.stop="toggleDebugMode">调试</button> -->
          </div>
          <div class="card-content">
            <div v-if="createclass.length === 0" class="empty-tip">暂无数据</div>
            <!-- 使用动画数据 -->
            <div class="stats-item" v-for="(item, index) in animatedValues.createclass" :key="index" v-if="item">
              <span class="stats-label">教师工号: {{ item.creatorId || '未知' }}</span>
              <span class="stats-value">创建课程总数: {{ item.animatedCount }}</span>
            </div>
          </div>
        </div>

        <!-- 课程的班级数量（新增：柱状图） -->
        <div class="analysis-card performance" @click="openChartBox('classCountBar')">
          <div class="card-header">
            <h2 class="card-title">🏅 课程的班级数量</h2>
          </div>
          <div class="card-content">
            <div v-if="classCountList.length === 0" class="empty-tip">暂无数据</div>
            <!-- 使用动画数据 -->
            <div class="stats-item" v-for="(item, index) in animatedValues.classCountList" :key="index" v-if="item">
              <span class="stats-label">{{ item.courseName || '未知课程' }}</span>
              <span class="stats-value">{{ item.animatedCount }}</span>
            </div>
          </div>
        </div>

        <!-- 课程学生数量（新增：柱状图） -->
        <div class="analysis-card teacher-work" @click="openChartBox('studentCountBar')">
          <div class="card-header">
            <h2 class="card-title">👩 课程学生数量</h2>
          </div>
          <div class="card-content">
            <div v-if="courseStudentList.length === 0" class="empty-tip">暂无数据</div>
            <!-- 使用动画数据 -->
            <div class="stats-item" v-for="(item, index) in animatedValues.courseStudentList" :key="index" v-if="item">
              <span class="stats-label">{{ item.courseName || '未知课程' }}:</span>
              <span class="stats-value">{{ item.animatedCount }}</span>
            </div>
          </div>
        </div>

        <!-- 课程信息统计 - 修复字段不匹配问题 -->
        <div class="analysis-card course-progress">
          <div class="card-header">
            <h2 class="card-title">📚 课程信息统计</h2>
          </div>
          <div class="card-content">
            <div class="stats-item">
              <span class="stats-label">不同班级ID总数:</span>
              <span class="stats-value">{{ animatedValues.classInfo.distinct_class_ids || 0 }}</span>
            </div>
            <div class="stats-item">
              <span class="stats-label">不同课程名称总数:</span>
              <span class="stats-value">{{ animatedValues.classInfo.distinct_course_names || 0 }}</span>
            </div>
            <div class="stats-item">
              <span class="stats-label">学生数量最多的课程:</span>
              <div v-if="animatedValues.classInfo.top_course_name">
                <span class="stats-value">{{ animatedValues.classInfo.top_course_name }}: {{ animatedValues.classInfo.top_course_students || 0 }}</span>
              </div>
              <div v-else class="empty-tip">暂无数据</div>
            </div>
            <div class="stats-item">
              <span class="stats-label">每个课程下的班级数量（最多）:</span>
              <div v-if="animatedValues.classInfo.max_class_course">
                <span class="stats-value">{{ animatedValues.classInfo.max_class_course }}: {{ animatedValues.classInfo.max_class_count || 0 }}</span>
              </div>
              <div v-else class="empty-tip">暂无数据</div>
            </div>
            <div class="stats-item">
              <span class="stats-label">每个课程的平均学生数（最高）:</span>
              <div v-if="animatedValues.classInfo.avg_student_course">
                <span class="stats-value">{{ animatedValues.classInfo.avg_student_course }}: {{ animatedValues.classInfo.avg_student_count || 0 }}</span>
              </div>
              <div v-else class="empty-tip">暂无数据</div>
            </div>
          </div>
        </div>

        <!-- 智能报告生成按钮（与其他卡片布局一致） -->
        <div class="analysis-card attendance">
          <div class="card-header">
            <h2 class="card-title">📑 报告中心</h2>
          </div>
          <div class="card-content">
            <div id="smartReportBtn" @click="openMarkdownBox">智能报告生成</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Vue from 'vue'
import Chart from 'chart.js'
import request from '@/utils/request'
import { getAiReport } from "@/api/studyInfo"

export default {
  name: 'StudyInfo',
  data() {
    return {
      chartBoxVisible: false,
      markdownBoxVisible: false,
      debugMode: true,
      debugData: {},
      currentChartType: '',
      createclass: [],
      classCountList: [],
      courseStudentList: [],
      ClassInfo: {},
      chartInstance: null,
      msg1: '',
      AI1: '',
      msg2: '',
      AI2: '',
      msg3: '',
      AI3: '',
      AI4: '',
      // 动画数据
      animatedValues: {
        createclass: [],
        classCountList: [],
        courseStudentList: [],
        classInfo: {}
      }
    }
  },
  mounted() {
    console.log('StudyInfo组件已挂载，开始加载数据...')
    this.loadAllData()
    window.addEventListener('resize', this.resizeChart)
  },
  beforeDestroy() {
    if (this.chartInstance) {
      this.chartInstance.destroy()
    }
    window.removeEventListener('resize', this.resizeChart)
  },
  methods: {
    toggleDebugMode() {
      this.debugMode = !this.debugMode
    },

    loadAllData() {
      console.log('开始加载所有数据...')
      Promise.allSettled([
        this.getTop5Creators(),
        this.getCourseClassCount(),
        this.getCourseStudentCount(),
        this.getClassInfoStats(),
        this.getStudentInfo()
      ]).then((results) => {
        console.log('所有数据加载完成:', results)
        // 数据加载完成后初始化动画值
        this.initAnimatedValues()
      }).catch(error => {
        console.error('数据加载失败:', error)
      })
    },

    // 初始化动画值
    initAnimatedValues() {
      console.log('初始化动画值...')

      // 复制真实数据到动画数据，但初始值为0
      this.animatedValues.createclass = this.createclass.slice(0,5).map(item => ({
        ...item,
        animatedCount: 0
      }))

      this.animatedValues.classCountList = this.classCountList.map(item => ({
        ...item,
        animatedCount: 0
      }))

      this.animatedValues.courseStudentList = this.courseStudentList.map(item => ({
        ...item,
        animatedCount: 0
      }))

      // 处理ClassInfo数据
      this.animatedValues.classInfo = {
        distinct_class_ids: 0,
        distinct_course_names: 0,
        top_course_name: this.ClassInfo.top_course_name || '',
        top_course_students: 0,
        max_class_course: this.ClassInfo.max_class_course || '',
        max_class_count: 0,
        avg_student_course: this.ClassInfo.avg_student_course || '',
        avg_student_count: 0
      }

      // 开始动画
      this.startNumberAnimation()
    },

    // 数字动画
    startNumberAnimation() {
      console.log('开始数字动画...')

      // 对每个数据项执行动画
      this.animateValue('createclass', 'courseCount')
      this.animateValue('classCountList', 'count')
      this.animateValue('courseStudentList', 'totalStudents')

      // 对ClassInfo数据执行动画
      this.animateClassInfo()
    },

    animateValue(dataKey, valueKey) {
      this.animatedValues[dataKey].forEach((item, index) => {
        const target = this[dataKey][index][valueKey] || 0
        const duration = 1000 // 动画时长
        const step = target / (duration / 16) // 60fps

        let current = 0
        const animate = () => {
          current += step
          if (current < target) {
            this.$set(this.animatedValues[dataKey][index], 'animatedCount', Math.min(Math.ceil(current), target))
            requestAnimationFrame(animate)
          } else {
            this.$set(this.animatedValues[dataKey][index], 'animatedCount', target)
          }
        }
        animate()
      })
    },

    animateClassInfo() {
      const duration = 1000
      const steps = duration / 16

      // 动画distinct_class_ids
      const classIdsTarget = this.ClassInfo.distinct_class_ids || 0
      const classIdsStep = classIdsTarget / steps
      let classIdsCurrent = 0

      const animateClassIds = () => {
        classIdsCurrent += classIdsStep
        if (classIdsCurrent < classIdsTarget) {
          this.$set(this.animatedValues.classInfo, 'distinct_class_ids', Math.min(Math.ceil(classIdsCurrent), classIdsTarget))
          requestAnimationFrame(animateClassIds)
        } else {
          this.$set(this.animatedValues.classInfo, 'distinct_class_ids', classIdsTarget)
        }
      }
      animateClassIds()

      // 动画distinct_course_names
      const courseNamesTarget = this.ClassInfo.distinct_course_names || 0
      const courseNamesStep = courseNamesTarget / steps
      let courseNamesCurrent = 0

      const animateCourseNames = () => {
        courseNamesCurrent += courseNamesStep
        if (courseNamesCurrent < courseNamesTarget) {
          this.$set(this.animatedValues.classInfo, 'distinct_course_names', Math.min(Math.ceil(courseNamesCurrent), courseNamesTarget))
          requestAnimationFrame(animateCourseNames)
        } else {
          this.$set(this.animatedValues.classInfo, 'distinct_course_names', courseNamesTarget)
        }
      }
      animateCourseNames()

      // 动画top_course_students
      const topStudentsTarget = this.ClassInfo.top_course_students || 0
      const topStudentsStep = topStudentsTarget / steps
      let topStudentsCurrent = 0

      const animateTopStudents = () => {
        topStudentsCurrent += topStudentsStep
        if (topStudentsCurrent < topStudentsTarget) {
          this.$set(this.animatedValues.classInfo, 'top_course_students', Math.min(Math.ceil(topStudentsCurrent), topStudentsTarget))
          requestAnimationFrame(animateTopStudents)
        } else {
          this.$set(this.animatedValues.classInfo, 'top_course_students', topStudentsTarget)
        }
      }
      animateTopStudents()

      // 动画max_class_count
      const maxClassTarget = this.ClassInfo.max_class_count || 0
      const maxClassStep = maxClassTarget / steps
      let maxClassCurrent = 0

      const animateMaxClass = () => {
        maxClassCurrent += maxClassStep
        if (maxClassCurrent < maxClassTarget) {
          this.$set(this.animatedValues.classInfo, 'max_class_count', Math.min(Math.ceil(maxClassCurrent), maxClassTarget))
          requestAnimationFrame(animateMaxClass)
        } else {
          this.$set(this.animatedValues.classInfo, 'max_class_count', maxClassTarget)
        }
      }
      animateMaxClass()

      // 动画avg_student_count
      const avgStudentTarget = this.ClassInfo.avg_student_count || 0
      const avgStudentStep = avgStudentTarget / steps
      let avgStudentCurrent = 0

      const animateAvgStudent = () => {
        avgStudentCurrent += avgStudentStep
        if (avgStudentCurrent < avgStudentTarget) {
          this.$set(this.animatedValues.classInfo, 'avg_student_count', Math.min(Math.ceil(avgStudentCurrent), avgStudentTarget))
          requestAnimationFrame(animateAvgStudent)
        } else {
          this.$set(this.animatedValues.classInfo, 'avg_student_count', avgStudentTarget)
        }
      }
      animateAvgStudent()
    },

    // 修复字段映射问题
    getTop5Creators() {
      console.log('开始请求创建课程数前5的教师...')
      return request.get('/system/courseinfo/top5Creators').then(res => {
        console.log('创建课程数前5的教师响应:', res)
        this.$set(this.debugData, 'top5Creators', res)

        if (res.code === 200) {
          // 修复：尝试多种可能的字段名
          this.createclass = res.data.list || []
          this.msg1 = res.data.msg

          console.log('处理后的createclass数据:', this.createclass)
        } else {
          console.warn('创建课程数前5的教师接口返回非200状态:', res)
        }
      }).catch(err => {
        console.error('获取教师课程数失败:', err)
        this.createclass = []
      })
    },

    getCourseClassCount() {
      console.log('开始请求课程班级数量...')
      return request.get('/system/classinfo/courseClassCount').then(res => {
        console.log('课程班级数量响应:', res)
        this.$set(this.debugData, 'courseClassCount', res)

        if (res.code === 200) {
          // 直接使用数据，不进行字段映射
          this.classCountList = res.data || []

          console.log('处理后的classCountList数据:', this.classCountList)
        } else {
          console.warn('课程班级数量接口返回非200状态:', res)
        }
      }).catch(err => {
        console.error('获取课程班级数失败:', err)
        this.classCountList = []
      })
    },

    getCourseStudentCount() {
      console.log('开始请求课程学生数量...')
      return request.get('/system/courseinfo/studentCount').then(res => {
        console.log('课程学生数量响应:', res)
        this.$set(this.debugData, 'studentCount', res)

        if (res.code === 200) {
          // 直接使用数据，不进行字段映射
          this.courseStudentList = res.data || []

          console.log('处理后的courseStudentList数据:', this.courseStudentList)
        } else {
          console.warn('课程学生数量接口返回非200状态:', res)
        }
      }).catch(err => {
        console.error('获取课程学生数失败:', err)
        this.courseStudentList = []
      })
    },

    getClassInfoStats() {
      console.log('开始请求课程统计信息...')
      return request.get('/system/courseinfo/classInfoStats').then(res => {
        console.log('课程统计信息响应:', res)
        this.$set(this.debugData, 'classInfoStats', res)

        if (res.code === 200) {
          this.ClassInfo = res.data || {}
          this.msg2 = res.data.msg
          console.log('处理后的ClassInfo数据:', this.ClassInfo)
        } else {
          console.warn('课程统计信息接口返回非200状态:', res)
        }
      }).catch(err => {
        console.error('获取课程统计信息失败:', err)
        this.ClassInfo = {}
      })
    },

    getStudentInfo(){
      return request.get('/dashboard/studyinfo/getExecutorStudyinfo').then(res => {
        if (res.code === 200) {
          console.log('学生数据:', res)
          this.msg3 = res.msg
        } else {
          console.warn('学生统计信息接口返回非200状态:', res)
        }
      }).catch(err => {
        console.error('获取学生统计信息失败:', err)
        this.msg3 = "暂无统计信息"
      })
    },

    initChart() {
      Vue.nextTick(() => {
        const ctx = this.$refs.courseStatsChart.getContext('2d')

        if (this.chartInstance) {
          this.chartInstance.destroy()
        }

        let chartConfig = this.getChartConfigByType()
        this.chartInstance = new Chart(ctx, chartConfig)
      })
    },

    getChartConfigByType() {
      const commonOptions = {
        animation: false,
        scales: {
          y: {
            beginAtZero: true,
            grid: { color: 'white' },
            ticks: { color: 'white' }
          },
          x: {
            grid: { display: false },
            ticks: { color: 'white' }
          }
        },
        plugins: {
          legend: {
            display: true,
            labels: {
              font: { size: 14 },
              color: 'white'
            }
          }
        }
      }

      switch (this.currentChartType) {
        case 'createClassLine':
          const validCreators = this.createclass.filter(item => item)
          return {
            type: 'line',
            data: {
              labels: validCreators.length > 0 ? validCreators.map(item => item.creatorId) : ['暂无数据'],
              datasets: [{
                label: '创建课程总数',
                data: validCreators.length > 0 ? validCreators.map(item => item.courseCount) : [0],
                borderColor: 'rgba(54, 162, 235, 1)',
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                borderWidth: 2,
                pointRadius: 6,
                pointBackgroundColor: 'rgba(255, 99, 132, 1)'
              }]
            },
            options: commonOptions
          }

        case 'classCountBar':
          const validClassCount = this.classCountList.filter(item => item)
          return {
            type: 'bar',
            data: {
              labels: validClassCount.length > 0 ? validClassCount.map(item => item.courseName) : ['暂无数据'],
              datasets: [{
                label: '课程班级数量',
                data: validClassCount.length > 0 ? validClassCount.map(item => item.count) : [0],
                backgroundColor: 'rgba(153, 102, 255, 0.7)',
                borderColor: 'rgba(153, 102, 255, 1)',
                borderWidth: 2,
                borderRadius: 4
              }]
            },
            options: commonOptions
          }

        case 'studentCountBar':
          const validStudents = this.courseStudentList.filter(item => item)
          return {
            type: 'bar',
            data: {
              labels: validStudents.length > 0 ? validStudents.map(item => item.courseName) : ['暂无数据'],
              datasets: [{
                label: '课程学生数量',
                data: validStudents.length > 0 ? validStudents.map(item => item.totalStudents) : [0],
                backgroundColor: 'rgba(75, 192, 192, 0.7)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 2,
                borderRadius: 4
              }]
            },
            options: commonOptions
          }

        default:
          return {
            type: 'line',
            data: {
              labels: ['暂无数据'],
              datasets: [{ label: '数据加载失败', data: [0] }]
            },
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
      this.AI1 = "智能助手正在分析中，请稍后......"
      this.AI2 = "智能助手正在分析中，请稍后......"
      this.AI3 = "智能助手正在分析中，请稍后......"
      this.AI4 = "智能助手正在分析中，请稍后......"
      this.loading = true
      const res = await getAiReport(this.msg1)
      this.AI1 = res.msg
      const res2 = await getAiReport(this.msg2)
      this.AI2 = res2.msg
      const res3 = await getAiReport(this.msg3)
      this.AI3 = res3.msg
      const res4 = await getAiReport(this.msg1 + this.msg2 + this.msg3)
      this.AI4 = res4.msg

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
/* 样式保持不变，与之前相同 */
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

.debug-panel {
  position: fixed;
  top: 80px;
  right: 20px;
  background: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 15px;
  border-radius: 8px;
  max-width: 500px;
  max-height: 400px;
  overflow: auto;
  z-index: 10002;
  font-size: 12px;
}

.debug-panel h3 {
  color: #ff6b6b;
  margin-bottom: 10px;
  border-bottom: 1px solid #555;
  padding-bottom: 5px;
}

.debug-section {
  margin-bottom: 15px;
}

.debug-section h4 {
  color: #4facfe;
  margin-bottom: 5px;
}

.debug-section pre {
  background: #2c3e50;
  padding: 8px;
  border-radius: 4px;
  overflow: auto;
  max-height: 150px;
  font-size: 10px;
}

.debug-btn {
  background: #ff6b6b;
  color: white;
  border: none;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
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
  .debug-panel {
    position: relative;
    top: auto;
    right: auto;
    max-width: 100%;
    margin: 10px;
  }
}
</style>
