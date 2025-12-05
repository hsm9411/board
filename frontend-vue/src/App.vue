<template>
  <div id="app">
    <AppHeader />
    <main class="container">
      <router-view/>
    </main>
  </div>
</template>

<script>
import AppHeader from '@/components/AppHeader.vue';
import { useUserStore } from '@/store/userStore';

export default {
  components: {
    AppHeader
  },
  created() {
    // [매우 중요] 앱이 생성될 때 (새로고침 포함), 서버에 세션이 유효한지 확인합니다.
    // 이를 통해 페이지를 새로고침해도 로그인 상태가 유지됩니다.
    const userStore = useUserStore();
    userStore.checkSession();
  }
}
</script>

<style>
/* 전역 스타일 */
.container {
  padding: 20px;
  max-width: 960px;
  margin: 0 auto;
}
</style>