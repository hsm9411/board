<template>
  <div class="mbti-container">
    <div class="intro-card">
      <h1>MBTI 성격 유형 검사</h1>
      <p>
        총 20개의 간단한 질문을 통해 자신의 성격 유형을 알아보세요.<br />
        검사는 약 5분 정도 소요됩니다.<br />
        가능한 한 정직하고 솔직하게 답변해주세요.
      </p>
      <button @click="startTest" class="start-button">검사 시작하기</button>
    </div>
  </div>
</template>

<!-- [수정] <script setup> 방식으로 변경하여 안정성 확보 -->
<script setup>
import { useRouter } from 'vue-router';

// useRouter 훅을 사용하여 라우터 인스턴스를 가져옵니다.
const router = useRouter();

const startTest = () => {
  // '/mbti/test' 경로로 라우팅하여 검사 페이지로 이동합니다.
  router.push('/mbti/test');
};
</script>

<style scoped>
.mbti-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px 20px;
}
.intro-card {
  max-width: 600px;
  width: 100%;
  padding: 40px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
  text-align: center;
}
h1 {
  margin-bottom: 20px;
  color: #333;
}
p {
  line-height: 1.8;
  color: #555;
  margin-bottom: 30px;
}
.start-button {
  font-size: 18px;
  padding: 12px 24px;
  border-radius: 8px;
  border: none;
  background-color: #667eea;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}
.start-button:hover {
  background-color: #5a6ed8;
}
</style>