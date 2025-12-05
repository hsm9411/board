<template>
  <div class="mbti-container">
    <div v-if="loading" class="status-message">질문을 불러오는 중입니다...</div>
    <div v-else-if="error" class="status-message error">{{ error }}</div>
    <div v-else class="test-wrapper">
      <h1>MBTI 검사 ({{ currentPage + 1 }} / {{ totalPages }})</h1>
      <form @submit.prevent="handleSubmit">
        <div class="question-page">
          <div
            v-for="(question, index) in currentQuestions"
            :key="question.boardComment"
            class="question-item"
            :class="{ unanswered: isUnanswered(globalIndex(index)) }"
          >
            <p>{{ globalIndex(index) + 1 }}. {{ question.boardComment }}</p>
            <div class="options">
              <span class="text-disagree">비동의</span>
              <label v-for="value in 7" :key="value" class="option-label">
                <input type="radio" :name="'q' + globalIndex(index)" :value="value" v-model="answers[globalIndex(index)]" required />
              </label>
              <span class="text-agree">동의</span>
            </div>
          </div>
        </div>

        <div class="navigation-controls">
          <!-- 첫 페이지에서는 '이전' 버튼이 보이지 않도록 v-if 사용 -->
          <button type="button" @click="prevPage" v-if="currentPage > 0">이전</button>
          
          <button v-if="currentPage < totalPages - 1" type="button" @click="nextPage">다음</button>
          <button v-else type="submit">결과 확인</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
// <script> 부분은 변경 없습니다.
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { fetchMbtiQuestions, submitMbtiAnswers } from '@/api/mbtiApi';

const router = useRouter();
const allQuestions = ref([]);
const answers = ref([]);
const unansweredQuestions = ref(new Set());
const currentPage = ref(0);
const questionsPerPage = 5;
const loading = ref(true);
const error = ref(null);

const totalPages = computed(() => Math.ceil(allQuestions.value.length / questionsPerPage));
const currentQuestions = computed(() => {
  const start = currentPage.value * questionsPerPage;
  const end = start + questionsPerPage;
  return allQuestions.value.slice(start, end);
});

const globalIndex = (index) => currentPage.value * questionsPerPage + index;

const validateCurrentPage = () => {
  unansweredQuestions.value.clear();
  let isValid = true;
  currentQuestions.value.forEach((_, index) => {
    const idx = globalIndex(index);
    if (answers.value[idx] == null) {
      unansweredQuestions.value.add(idx);
      isValid = false;
    }
  });
  return isValid;
};

const isUnanswered = (index) => unansweredQuestions.value.has(index);
const nextPage = () => { if (validateCurrentPage()) currentPage.value++; };
const prevPage = () => { if (currentPage.value > 0) currentPage.value--; };

const handleSubmit = async () => {
  if (!validateCurrentPage()) return;
  const formattedAnswers = allQuestions.value.map((q, i) => ({
    boardType: q.boardType,
    answerValue: answers.value[i],
  }));
  try {
    const result = await submitMbtiAnswers(formattedAnswers);
    router.push({ name: 'MbtiResult', query: { data: JSON.stringify(result) } });
  } catch (err) {
    alert(err.message);
  }
};

onMounted(async () => {
  try {
    const questions = await fetchMbtiQuestions();
    allQuestions.value = questions;
    answers.value = Array(questions.length).fill(null);
  } catch (err) {
    error.value = err.message;
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.mbti-container { display: flex; justify-content: center; padding: 40px 20px; }
.status-message { font-size: 1.2em; color: #555; padding: 40px; }
.status-message.error { color: #e74c3c; }

.test-wrapper { width: 100%; max-width: 800px; padding: 30px; background-color: #fff; border-radius: 12px; box-shadow: 0 6px 20px rgba(0,0,0,0.08); }
.test-wrapper h1 { text-align: center; margin-bottom: 30px; }

.question-item { margin-bottom: 25px; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #fafafa; transition: all 0.3s; }
.question-item.unanswered { border-color: #e74c3c; background-color: #fff5f5; animation: shake 0.5s; }
@keyframes shake { 0%, 100% { transform: translateX(0); } 10%, 50%, 90% { transform: translateX(-5px); } 30%, 70% { transform: translateX(5px); } }
.question-item p { font-size: 1.1em; font-weight: 500; margin: 0 0 15px 0; line-height: 1.5; }

.options { display: flex; justify-content: space-between; align-items: center; }
.option-label { display: flex; flex-direction: column; align-items: center; cursor: pointer; }
.option-label input[type="radio"] { width: 20px; height: 20px; }
.text-agree { color: #2980b9; font-weight: bold; }
.text-disagree { color: #c0392b; font-weight: bold; }

/* [개선] 네비게이션 버튼 UX 개선 */
.navigation-controls {
  display: flex;
  justify-content: center; /* 버튼 그룹을 중앙으로 정렬 */
  align-items: center;
  gap: 20px; /* 버튼 사이의 간격 */
  margin-top: 30px;
}
.navigation-controls button {
  font-size: 16px;
  border-radius: 8px;
  border: none;
  background-color: #667eea;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
  
  /* [개선] 고정 너비와 패딩으로 크기 유지 */
  width: 140px;
  padding: 12px 0; /* 세로 패딩만 주고 가로는 너비로 제어 */
  text-align: center;
}
.navigation-controls button:hover {
  background-color: #5a6ed8;
}
</style>