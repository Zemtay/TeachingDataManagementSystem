<template>
  <div class="app-container chat-container">
    <!-- 聊天记录区域 -->
    <div class="chat-window" ref="chatMessages">
      <div
        v-for="(item, index) in messageList"
        :key="index"
        class="message-item"
        :class="item.sender"
      >
        <div
          class="avatar"
          :style="{ backgroundImage: `url(${index % 2 === 0 ? aiAvatar : userAvatar})` }"
        ></div>
        <!--          "{ backgroundImage: `url(${item.avatar})`}"-->

        <div class="bubble">{{ item.content }}</div>
      </div>

      <div v-if="isTyping" class="typing">
        <span class="dot"></span><span class="dot"></span><span class="dot"></span>
      </div>
    </div>

    <!-- 输入区域 -->
    <el-form :model="form" ref="chatForm" label-width="80px" class="chat-form">
      <el-form-item label="输入内容" prop="input">
        <el-input
          type="textarea"
          v-model="form.input"
          placeholder="请输入您的问题..."
          autosize
          @keyup.enter.native="handleSend"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-s-promotion" @click="handleSend">发送</el-button>
        <el-button icon="el-icon-refresh" @click="resetInput">清空输入</el-button>
        <el-button icon="el-icon-delete" type="danger" plain @click="clearChatHistory">清空记录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getAiAnswer, getChatHistory, saveChatHistory, clearChatHistory } from "@/api/aiAssistant"
import { mapGetters} from "vuex";
import store from "../../../store";

export default {
  computed: {
    ...mapGetters(['avatar']),

    userAvatar() {
      const avatar = store.getters.avatar || require("@/assets/images/user.svg")
      return avatar
      // 如果 avatar 已经是完整 URL，不用拼接
      // if (avatar.startsWith("http")) return avatar
      // return process.env.VUE_APP_BASE_API + (avatar.startsWith("/") ? avatar : "/" + avatar)
    },
    aiAvatar() {
      return require("@/assets/images/robot.svg")
    }
  },
  name: "AiAssistant",
  data() {
    return {
      messageList: [],
      form: {
        input: ""
      },
      isTyping: false
    }
  },
  async created() {
    this.loadChatHistory()
  },
  watch: {
    messageList: {
      handler: async function(newVal) {
        if (this.isSaving) return  // 防止循环
        this.isSaving = true
        try {
          await saveChatHistory(newVal.slice(-50))
        } finally {
          this.isSaving = false
        }
      },
      deep: true
    }
  },
  methods: {
    async loadChatHistory() {
      const res = await getChatHistory()
      if (res.code === 200 && res.data.length > 0) {
        this.messageList = res.data
      } else {
        this.messageList = [
          { sender: "ai", content: "您好！我是AI助手，请问有什么可以帮您？" }
        ]
      }
    },
    /** 发送消息 */
    async handleSend() {
      if (!this.form.input.trim()) return
      const userText = this.form.input

      this.messageList.push({ sender: "user", content: userText})
      this.form.input = ""
      this.isTyping = true
      this.scrollToBottom()

      try {
        const recentMessages = this.messageList.slice(-10)
        const res = await getAiAnswer(recentMessages)
        let aiReply = ""

        if (res && res.msg) {
          aiReply = res.msg
        } else if (typeof res === "string") {
          aiReply = res
        } else {
          // console.log("111返回内容", res)
          aiReply = "AI暂时无法回复，请稍后再试。"
        }

        this.messageList.push({ sender: "ai", content: aiReply})
      } catch (error) {
        console.error(error)
        this.messageList.push({ sender: "ai", content: "接口调用失败，请检查网络或后端服务。"})
      } finally {
        this.isTyping = false
        this.scrollToBottom()
      }
    },

    /** 清空输入框 */
    resetInput() {
      this.form.input = ""
    },

    /** 清空聊天记录 */
    // clearChatHistory() {
    //   this.$confirm("确定要清空聊天记录吗？", "提示", {
    //     confirmButtonText: "确定",
    //     cancelButtonText: "取消",
    //     type: "warning"
    //   }).then(() => {
    //     this.messageList = [
    //       { sender: "ai", content: "您好！我是AI助手，请问有什么可以帮您？" }
    //     ]
    //     localStorage.removeItem("ai_chat_history")
    //     this.$message.success("聊天记录已清空")
    //   }).catch(() => {})
    // },
    clearChatHistory() {
      this.$confirm("确定要清空聊天记录吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(async () => {
        const res = await clearChatHistory()
        if (res.code === 200) {
          this.messageList = [
            { sender: "ai", content: "您好！我是AI助手，请问有什么可以帮您？"}
          ]
          this.$message.success("聊天记录已清空")
        }
      }).catch(() => {})
    },

    /** 聊天滚动到底部 */
    scrollToBottom() {
      this.$nextTick(() => {
        const el = this.$refs.chatMessages
        if (el) el.scrollTop = el.scrollHeight
      })
    }
  }
}
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 100px);
  background: #fff;
  border-radius: 8px;
  padding: 10px;
}

.chat-window {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  border: 1px solid #eee;
  border-radius: 8px;
  margin-bottom: 10px;
}

.message-item {
  display: flex;
  margin-bottom: 10px;
}

.message-item.user {
  flex-direction: row-reverse;
}

.bubble {
  background: #f0f0f0;
  padding: 8px 12px;
  border-radius: 6px;
  max-width: 60%;
  word-break: break-word;
}

.user .bubble {
  background: #c8e6c9;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-size: cover;
  background-position: center;
  //background: #dddddd;
  margin: 0 8px;
  flex-shrink: 0;
}

.typing {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  padding: 8px;
}

.dot {
  width: 6px;
  height: 6px;
  background: #999;
  border-radius: 50%;
  margin-right: 4px;
  animation: blink 1.2s infinite ease-in-out;
}

@keyframes blink {
  0%, 80%, 100% { opacity: 0.2; }
  40% { opacity: 1; }
}
</style>
