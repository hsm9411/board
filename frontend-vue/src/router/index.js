import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/userStore'
import BoardListView from '../views/board/BoardListView.vue'

const routes = [
  {
    path: '/',
    redirect: '/boards'
  },
  // --- 게시판 라우트 ---
  {
    path: '/boards',
    name: 'BoardList',
    // [수정] 폴더 구조 변경 및 Lazy Loading 적용
    component: BoardListView
  },
  {
    path: '/boards/view/:boardType/:boardNum',
    name: 'BoardDetail',
    // [수정] 폴더 구조 변경 및 Lazy Loading 적용
    component: () => import('../views/board/BoardDetailView.vue'),
    props: true
  },
  {
    path: '/boards/write',
    name: 'BoardWrite',
    // [수정] 폴더 구조 변경 및 Lazy Loading 적용
    component: () => import('../views/board/BoardWriteView.vue'),
    beforeEnter: (to, from, next) => {
      const userStore = useUserStore();
      if (userStore.isLoggedIn) {
        next();
      } else {
        alert('로그인이 필요합니다.');
        next('/login');
      }
    }
  },
  {
    path: '/boards/edit/:boardType/:boardNum',
    name: 'BoardEdit',
    // [수정] 폴더 구조 변경 및 Lazy Loading 적용
    component: () => import('../views/board/BoardEditView.vue'),
    props: true,
    beforeEnter: (to, from, next) => {
      const userStore = useUserStore();
      if (userStore.isLoggedIn) {
        next();
      } else {
        alert('로그인이 필요합니다.');
        next('/login');
      }
    }
  },
  // --- 사용자 인증 라우트 ---
  {
    path: '/login',
    name: 'Login',
    // [수정] 폴더 구조 변경 및 Lazy Loading 적용
    component: () => import('../views/user/LoginView.vue')
  },
  {
    path: '/signup',
    name: 'Signup',
    // [수정] 폴더 구조 변경 및 Lazy Loading 적용
    component: () => import('../views/user/SignupView.vue')
  },

  // --- [신규] MBTI 라우트 ---
  {
    path: '/mbti',
    name: 'MbtiIntro',
    component: () => import('../views/mbti/MbtiIntroView.vue'),
  },
  {
    path: '/mbti/test',
    name: 'MbtiTest',
    component: () => import('../views/mbti/MbtiTestView.vue')
  },
  {
    path: '/mbti/result',
    name: 'MbtiResult',
    component: () => import('../views/mbti/MbtiResultView.vue'),
    // 쿼리 파라미터로 받은 'data'를 파싱하여 resultData prop으로 전달
    props: route => {
        try {
            return { resultData: JSON.parse(route.query.data || 'null') }
        } catch(e) {
            return { resultData: null }
        }
    }
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router