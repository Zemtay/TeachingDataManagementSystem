<template>
  <div class="container">
    <!-- 成功图标（完全保留原SVG路径和尺寸） -->
    <div class="checkmark-circle">
      <svg viewBox="0 0 52 52">
        <path class="checkmark__check" d="M14 27l7 7 17-17"/>
      </svg>
    </div>

    <!-- 成功提示文字 -->
    <div class="message">文件上传成功！</div>

    <!-- 继续操作按钮（跳转回上传页面） -->
    <button class="btn" id="continueBtn" @click="goToUpload">继续操作</button>
  </div>
</template>

<script>
export default {
  name: 'UploadSuccess',
  methods: {
    // 继续操作：跳转回上传页面（匹配原{% url 'upload' %}逻辑）
    goToUpload() {
      this.$router.push({ path: '/upload' })
    }
  }
}
</script>

<style scoped>
/* 完全保留你提供的所有CSS，无修改 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
html, body {
  height: 100%;
  font-family: '微软雅黑', Arial, sans-serif;
  background: #1c2c57;
}
/* 主容器居中显示 */
.container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
  text-align: center;
  padding: 20px;
}
/* 成功图标容器 */
.checkmark-circle {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background-color: #8eabd4;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 20px;
  animation: popIn 0.5s ease-out;
  position: relative;
}
/* SVG 图标样式（保留原viewBox和路径） */
.checkmark-circle svg {
  width: 60px;
  height: 60px;
}
/* 动画：图标弹出 */
@keyframes popIn {
  0% { transform: scale(0); }
  100% { transform: scale(1); }
}
/* 检查标记的绘制动画 */
.checkmark__check {
  fill: none;
  stroke: #fff;
  stroke-width: 5;
  stroke-linecap: round;
  stroke-linejoin: round;
  stroke-dasharray: 48;
  stroke-dashoffset: 48;
  animation: stroke 0.5s 0.5s forwards;
}
@keyframes stroke {
  to { stroke-dashoffset: 0; }
}
/* 成功提示文字 */
.message {
  font-size: 24px;
  color: white;
  margin-bottom: 20px;
}
/* 按钮样式（保留原ID和hover效果） */
.btn {
  background-color: #8eabd4;
  color: #fff;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}
.btn:hover {
  background-color: #748baa;
}

/* 适配若依框架顶部导航，避免内容被遮挡 */
@media (max-width: 768px) {
  .container {
    height: calc(100vh - 80px);
    margin-top: 80px;
  }
  .message {
    font-size: 20px;
  }
  .checkmark-circle {
    width: 80px;
    height: 80px;
  }
  .checkmark-circle svg {
    width: 50px;
    height: 50px;
  }
}
</style>
