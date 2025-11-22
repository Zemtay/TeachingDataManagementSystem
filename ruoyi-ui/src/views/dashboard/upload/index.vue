<template>
  <div class="upload-page">
    <!-- 返回按钮 -->
    <!-- <div class="return-button" @click="goBack">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
        <path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z" fill="white" />
      </svg>
    </div> -->

    <!-- 上传容器 -->
    <div class="upload-container">
      <!-- 拖拽区域 -->
      <div
        class="drop-zone"
        ref="dropZone"
        @click="triggerFileSelect"
        @dragenter.prevent="handleDragEnter"
        @dragover.prevent="handleDragOver"
        @dragleave.prevent="handleDragLeave"
        @drop.prevent="handleDrop"
      >
        <!-- 上传图标 -->
        <div class="upload-icon">
          <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24" fill="currentColor">
            <path d="M19.35 10.04C18.67 6.59 15.64 4 12 4 9.11 4 6.6 5.64 5.35 8.04 2.34 8.36 0 10.91 0 14c0 3.31 2.69 6 6 6h13c2.76 0 5-2.24 5-5 0-2.64-2.05-4.78-4.65-4.96zM14 13v4h-4v-4H7l5-5 5 5h-3z" />
          </svg>
        </div>

        <!-- 提示文字 -->
        <p class="prompt-text">拖拽文件到此处，或 <span class="browse-link" @click.stop="triggerFileSelect">浏览文件</span></p>
        <p class="format-hint">支持 PNG, JPG, PDF, CSV, XLS, XLSX (最大 30MB)</p>

        <!-- 隐藏的文件输入 -->
        <input
          type="file"
          id="fileInput"
          ref="fileInput"
          @change="handleFileSelect"
          style="display: none"
        >
      </div>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'
export default {
  name: 'FileUpload',
  data() {
    return {
      // 用于跟踪拖拽状态
      isDragging: false
    }
  },
  methods: {
    // 返回首页
    goBack() {
      // this.$router.push({ path: '/' })
      this.$router.push('/home')
    },

    // 触发文件选择
    triggerFileSelect() {
      this.$refs.fileInput.click()
    },

    // 拖拽进入
    handleDragEnter() {
      this.isDragging = true
      this.$refs.dropZone.classList.add('highlight')
    },

    // 拖拽悬停
    handleDragOver() {
      this.isDragging = true
      this.$refs.dropZone.classList.add('highlight')
    },

    // 拖拽离开
    handleDragLeave() {
      this.isDragging = false
      this.$refs.dropZone.classList.remove('highlight')
    },

    // 处理文件拖放
    handleDrop(e) {
      this.isDragging = false
      this.$refs.dropZone.classList.remove('highlight')
      const files = e.dataTransfer.files
      this.handleFiles(files)
    },

    // 处理文件选择
    handleFileSelect(e) {
      const files = e.target.files
      this.handleFiles(files)
      // 重置input值，避免重复选择同一文件不触发事件
      e.target.value = ''
    },

    // 处理文件逻辑
    handleFiles(files) {
      if (files.length > 0) {
        const file = files[0]
        if (this.validateFile(file)) {
          console.log('Valid file:', file)
          this.showUploadStatus(`已选择文件: ${file.name}`)

          // 这里可以添加实际上传逻辑（调用后端接口）
          this.uploadFile(file)
        }
      }
    },

    // 文件验证
    validateFile(file) {
      const allowedTypes = [
        'image/png',
        'image/jpeg',
        'application/pdf',
        'text/csv',
        'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        'application/vnd.ms-excel'
      ]

      // 验证文件类型
      if (!allowedTypes.includes(file.type)) {
        this.$message.error('不支持的文件格式')
        return false
      }

      // 验证文件大小（30MB）
      if (file.size > 30 * 1024 * 1024) {
        this.$message.error('文件大小超过30MB限制')
        return false
      }

      return true
    },

    // 显示上传状态
    showUploadStatus(message) {
      const statusEl = document.createElement('div')
      statusEl.textContent = message
      statusEl.style.color = '#3b82f6'
      statusEl.style.marginTop = '1rem'
      statusEl.style.transition = 'opacity 0.3s ease'
      this.$refs.dropZone.appendChild(statusEl)

      // 3秒后自动消失
      setTimeout(() => {
        statusEl.style.opacity = '0'
        setTimeout(() => statusEl.remove(), 300)
      }, 3000)
    },

    // 实际上传文件的方法（示例）
    uploadFile(file) {
      const formData = new FormData()
      formData.append('file', file)

      request({
        url: '/api/upload',
        method: 'post',
        data: formData,
        headers: { 'Content-Type': 'multipart/form-data' }
      })
        .then(res => {
          this.$message.success('上传成功: ' + res.data)
          console.log('上传返回：', res)
        })
        .catch(err => {
          this.$message.error('上传失败')
          console.error('上传失败：', err)
        })
    }

  }
}
</script>

<style scoped>
/* 页面容器 */
.upload-page {
  width: 100%;
  min-height: 100vh;
  background: #1c2c57;
  padding: 20px;
  box-sizing: border-box;
}

/* 现代卡片式容器 */
.upload-container {
  background: #8eabd4;
  padding: 2rem;
  border-radius: 16px;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
  width: 90%;
  max-width: 480px;
  margin: 0 auto;
  margin-top: 100px; /* 避开顶部导航 */
}

/* 拖拽区域样式 */
.drop-zone {
  border: 2px dashed #cbd5e1;
  border-radius: 8px;
  padding: 2.5rem;
  text-align: center;
  transition: all 0.2s ease;
  background: #89a5cc;
  cursor: pointer;
}

.drop-zone.highlight {
  border-color: #3b82f6;
  background: rgba(59, 130, 246, 0.05);
}

/* 图标动画 */
.upload-icon {
  font-size: 2.5rem;
  color: #3b82f6;
  margin-bottom: 1rem;
  animation: float 3s ease-in-out infinite;
}

/* 提示文字 */
.prompt-text {
  color: white;
  margin: 0.5rem 0;
  font-size: 1rem;
}

.browse-link {
  color: #3b82f6;
  font-weight: 500;
  text-decoration: underline;
  cursor: pointer;
}

/* 支持格式提示 */
.format-hint {
  color: #94a3b8;
  font-size: 0.875rem;
  margin-top: 1rem;
}

/* 悬浮动画 */
@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-8px); }
}

/* 返回按钮样式 */
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
  z-index: 100; /* 确保在容器上方 */
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

/* 适配若依框架的响应式调整 */
@media (max-width: 768px) {
  .upload-container {
    margin-top: 80px;
    padding: 1.5rem;
  }

  .drop-zone {
    padding: 2rem 1rem;
  }

  .prompt-text {
    font-size: 0.9rem;
  }
}
</style>
