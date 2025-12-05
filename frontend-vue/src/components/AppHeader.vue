<!-- /src/components/AppHeader.vue -->

<template>
  <header class="app-header">
    <nav>
      <!-- [수정] 링크 영역 그룹화 -->
      <div class="main-links">
        <router-link to="/boards">게시판</router-link>
        <!-- [신규] MBTI 검사 링크 추가 -->
        <router-link to="/mbti">MBTI 검사</router-link>
      </div>

      <div v-if="isLoggedIn" class="user-info">
        <span>환영합니다, {{ userName }}님!</span>
        <a href="#" @click.prevent="handleLogout">로그아웃</a>
      </div>
      <div v-else class="guest-info">
        <router-link to="/login">로그인</router-link>
        <router-link to="/signup">회원가입</router-link>
      </div>
    </nav>
  </header>
</template>

<script>
import { mapState, mapActions } from 'pinia';
import { useUserStore } from '@/store/userStore';

export default {
  computed: {
    ...mapState(useUserStore, ['isLoggedIn', 'userName'])
  },
  methods: {
    ...mapActions(useUserStore, ['logout']),
    
    async handleLogout() {
      await this.logout();
      if (this.$route.path !== '/boards') {
        this.$router.push('/boards');
      }
    }
  }
}
</script>

<style scoped>
.app-header {
  background-color: #f8f9fa;
  padding: 10px 20px;
  border-bottom: 1px solid #dee2e6;
}
nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
/* [신규] 메인 링크 스타일 */
.main-links a {
  margin-right: 20px;
  font-size: 1.1em;
  color: #343a40;
  text-decoration: none;
  font-weight: 500;
}
/* [신규] 현재 활성화된 라우터 링크 스타일 */
.main-links a.router-link-exact-active {
  color: #007bff;
}

.user-info span, .guest-info a {
  margin: 0 10px;
}
a {
  text-decoration: none;
  color: #007bff;
}
a:hover {
  text-decoration: underline;
}
</style>