<template>
  <div id="main">
    <!-- 下拉菜单（用户管理/登出） -->
    <div class="dropdown">
      <button 
        id="admin" 
        class="dropbtn" 
        @click.stop="toggleDropdown"
      >
        用户
      </button>
      <div class="dropdown-content" v-show="dropdownVisible">
        <a @click="goToManage">管理</a>
        <a @click="logout">登出</a>
      </div>
    </div>

    <!-- 三个功能模块：自适应同一行排列 -->
    <div class="modules-container">
      <div 
        class="module-item" 
        v-for="(item, index) in cardList" 
        :key="index"
        @click="goToPath(item.path)"
      >
        <p class="module-title">{{ item.title }}</p>
        <img :src="item.icon" class="module-icon" :alt="item.title">
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HomePage',
  data() {
    return {
      dropdownVisible: false, // 控制下拉菜单显示/隐藏
      // 功能卡片配置
      cardList: [
        {
          title: '上传文件',
          icon: require('@/assets/pic/folder.png'),
          path: '/upload' // 对应上传文件页面路由
        },
        {
          title: '学情分析',
          icon: require('@/assets/pic/chart.png'),
          path: '/study-info' // 对应学情分析页面路由
        },
        {
          title: '智能助手',
          icon: require('@/assets/pic/brain.png'),
          path: '/ai-assistant/index' // 对应AI助手页面路由
        }
      ]
    }
  },
  mounted() {
    // 点击页面空白处关闭下拉菜单
    document.addEventListener('click', this.closeDropdownOnOutsideClick)
  },
  beforeDestroy() {
    // 移除事件监听，避免内存泄漏
    document.removeEventListener('click', this.closeDropdownOnOutsideClick)
  },
  methods: {
    // 切换下拉菜单显示/隐藏
    toggleDropdown() {
      this.dropdownVisible = !this.dropdownVisible
    },
    // 点击空白处关闭下拉菜单
    closeDropdownOnOutsideClick(e) {
      const dropdown = document.querySelector('.dropdown')
      if (!dropdown.contains(e.target)) {
        this.dropdownVisible = false
      }
    },
    // 跳转到管理页面
    goToManage() {
      this.$router.push({ path: '/update/executive' })
      this.dropdownVisible = false
    },
    // 登出
    logout() {
      this.$router.push({ path: '/logout' })
      this.dropdownVisible = false
    },
    // 功能卡片跳转
    goToPath(path) {
      this.$router.push({ path })
    }
  }
}
</script>

<style scoped>
/* 基础样式重置 */
body, html {
  margin: 0;
  padding: 0;
  background-color: #1c2c57;
  background-attachment: fixed;
  background-position: center center;
  background-repeat: no-repeat;
  background-size: cover;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
  height: 100%;
}

/* 下拉菜单样式 */
.dropdown {
  position: relative;
  display: block;
  float: right;
  margin: 20px 30px 0 0; /* 调整位置，避免遮挡模块 */
  z-index: 100;
}

.dropbtn {
  width: 55px;
  height: 55px;
  border: 1px solid rgb(128, 125, 125);
  border-radius: 50%;
  background-color: rgba(17, 68, 112, 0.5);
  color: white;
  font-size: 16px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.3s ease;
}

.dropdown-content {
  position: absolute;
  right: 0;
  margin-top: 10px;
  border-radius: 10px;
  background-color: #8eabd4;
  min-width: 120px;
  box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
  overflow: hidden;
}

.dropdown-content a {
  color: black;
  font-size: 16px;
  text-align: center;
  padding: 12px 10px;
  text-decoration: none;
  display: block;
  transition: background-color 0.2s;
}

.dropdown-content a:hover {
  background-color: #f1f1f1;
}

.dropbtn:hover {
  background-color: #8eabd4;
}

/* 模块容器：核心自适应布局 */
.modules-container {
  display: flex;          /* 启用Flex布局 */
  flex-wrap: wrap;        /* 屏幕较小时自动换行 */
  justify-content: center; /* 水平居中 */
  align-items: center;    /* 垂直居中 */
  gap: 30px;              /* 模块间距 */
  padding: 40px 20px;     /* 容器内边距 */
  max-width: 1400px;      /* 最大宽度限制 */
  margin: 0 auto;         /* 容器居中 */
  height: calc(100vh - 120px); /* 高度自适应屏幕，减去顶部空间 */
}

/* 单个模块样式 */
.module-item {
  flex: 1;                /* 自适应分配宽度 */
  min-width: 250px;       /* 最小宽度，避免过小 */
  max-width: 350px;       /* 最大宽度，避免过大 */
  height: 320px;          /* 固定高度，保持一致性 */
  background-color: #7483ac;
  border-radius: 15px;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  transition: all 0.3s ease; /* 动画过渡 */
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15); /* 基础阴影 */
}

/* 模块hover效果 */
.module-item:hover {
  transform: translateY(-8px) scale(1.02); /* 上浮+轻微放大 */
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.25); /* 加深阴影 */
  background-color: #8e9dc8; /* 颜色微亮 */
}

/* 模块标题 */
.module-title {
  font-size: 28px;
  color: white;
  text-align: center;
  margin: 0 0 25px 0; /* 与图标间距 */
  font-weight: bold;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 模块图标 */
.module-icon {
  width: 100px; /* 图标大小 */
  height: 100px;
  object-fit: contain; /* 保持图标比例 */
  transition: transform 0.3s ease;
}

.module-item:hover .module-icon {
  transform: scale(1.1); /* 图标hover放大 */
}

/* 响应式适配：小屏幕 */
@media (max-width: 900px) {
  .modules-container {
    gap: 20px;
    padding: 20px 15px;
    height: auto; /* 小屏幕高度自适应内容 */
    margin-top: 80px; /* 避开下拉菜单 */
  }

  .module-item {
    min-width: 100%; /* 小屏幕占满一行 */
    max-width: 100%;
    height: 280px;
    margin-bottom: 10px;
  }

  .module-title {
    font-size: 24px;
  }

  .module-icon {
    width: 80px;
    height: 80px;
  }

  .dropdown {
    margin: 15px 20px 0 0;
  }
}
</style>