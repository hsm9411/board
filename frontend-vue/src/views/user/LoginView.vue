<!-- /src/views/LoginView.vue -->

<template>
  <div class="auth-container">
    <div class="auth-form-wrapper">
      <h2 class="form-title">로그인</h2>
      <form @submit.prevent="handleLogin">
        
        <div class="form-group">
          <label for="userId">아이디</label>
          <input type="text" id="userId" v-model="credentials.userId" placeholder="아이디를 입력하세요" required>
        </div>
        
        <div class="form-group">
          <label for="userPw">비밀번호</label>
          <input type="password" id="userPw" v-model="credentials.userPw" @keyup.enter="handleLogin" placeholder="비밀번호를 입력하세요" required>
        </div>
        
        <!-- [개선] 메시지 영역: 항상 고정된 높이를 차지하여 레이아웃 흔들림 방지 -->
        <div class="message-box">
          <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
        </div>
        
        <button type="submit" class="submit-button">로그인</button>
      </form>
      <div class="form-footer">
        <router-link to="/boards">목록으로</router-link>
      </div>
    </div>
  </div>
</template>

<script>
// <script> 부분은 이전과 동일하므로 변경할 필요 없습니다.
import { mapActions } from 'pinia';
import { useUserStore } from '@/store/userStore';

export default {
  data() {
    return {
      credentials: {
        userId: '',
        userPw: ''
      },
      errorMessage: ''
    };
  },
  methods: {
    ...mapActions(useUserStore, ['login']),
    
    async handleLogin() {
      this.errorMessage = '';
      if (!this.credentials.userId || !this.credentials.userPw) {
        this.errorMessage = '아이디와 비밀번호를 모두 입력해주세요.';
        return;
      }

      try {
        const loginSuccess = await this.login(this.credentials);
        if (loginSuccess) {
          this.$router.push('/boards');
        }
      } catch (error) {
        this.errorMessage = error.message || '로그인 처리 중 오류가 발생했습니다.';
      }
    }
  }
}
</script>

<style scoped>
/* [개선] 공통 스타일을 적용하여 깔끔하고 중앙 정렬된 UI 구성 */
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 150px); /* 헤더 높이 등을 제외한 나머지 화면 채우기 */
}

.auth-form-wrapper {
  width: 100%;
  max-width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.form-title {
  text-align: center;
  font-size: 24px;
  margin-bottom: 30px;
  color: #333;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 600;
  color: #555;
}

.form-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  transition: border-color 0.3s;
}

.form-group input:focus {
  outline: none;
  border-color: #007bff;
}

.message-box {
  /* [핵심] 메시지가 있든 없든 항상 높이를 차지하도록 설정 */
  min-height: 20px;
  margin-bottom: 20px;
}

.error-message {
  color: #dc3545;
  font-size: 14px;
}

.submit-button {
  width: 100%;
  padding: 12px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  font-weight: 600;
  transition: background-color 0.3s;
}

.submit-button:hover {
  background-color: #0056b3;
}

.form-footer {
  margin-top: 20px;
  text-align: center;
}

.form-footer a {
  color: #007bff;
  text-decoration: none;
}
</style>