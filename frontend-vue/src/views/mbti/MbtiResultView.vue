<template>
  <div class="mbti-container">
    <div v-if="resultData" class="result-card">
      <div class="result-header">
        <h2>검사 결과</h2>
        <p>당신의 MBTI 유형은 <br /><strong class="mbti-type">{{ resultData.mbtiType }}</strong> 입니다.</p>
      </div>
      <div class="result-details">
        <!-- 각 지표별 결과 Progress Bar -->
        <div class="result-dimension">
          <div class="score-text"><span>E (외향)</span><span>I (내향)</span></div>
          <div class="progress-bar">
            <div class="progress e-progress" :style="{ width: resultData.eScore + '%' }"></div>
          </div>
          <div class="score-percent"><span>{{ resultData.eScore }}%</span><span>{{ resultData.iScore }}%</span></div>
        </div>
        <div class="result-dimension">
          <div class="score-text"><span>S (감각)</span><span>N (직관)</span></div>
          <div class="progress-bar">
            <div class="progress s-progress" :style="{ width: resultData.sScore + '%' }"></div>
          </div>
          <div class="score-percent"><span>{{ resultData.sScore }}%</span><span>{{ resultData.nScore }}%</span></div>
        </div>
        <div class="result-dimension">
          <div class="score-text"><span>T (사고)</span><span>F (감정)</span></div>
          <div class="progress-bar">
            <div class="progress t-progress" :style="{ width: resultData.tScore + '%' }"></div>
          </div>
          <div class="score-percent"><span>{{ resultData.tScore }}%</span><span>{{ resultData.fScore }}%</span></div>
        </div>
        <div class="result-dimension">
          <div class="score-text"><span>J (판단)</span><span>P (인식)</span></div>
          <div class="progress-bar">
            <div class="progress j-progress" :style="{ width: resultData.jScore + '%' }"></div>
          </div>
          <div class="score-percent"><span>{{ resultData.jScore }}%</span><span>{{ resultData.pScore }}%</span></div>
        </div>
      </div>
      <button @click="restartTest" class="restart-button">다시 검사하기</button>
    </div>
    <div v-else class="status-message error">
        결과 데이터가 없습니다. 다시 검사를 진행해주세요.
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

defineProps({
  resultData: {
    type: Object,
    default: () => null,
  },
});

const router = useRouter();
const restartTest = () => {
  router.push('/mbti');
};
</script>

<style scoped>
.mbti-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px 20px;
  min-height: calc(100vh - 200px);
}
.result-card {
  width: 100%;
  max-width: 600px;
  padding: 40px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
}
.result-header { text-align: center; }
.result-header h2 { margin-bottom: 10px; }
.result-header p { font-size: 1.5em; margin: 0 0 25px 0; }
.mbti-type { color: #667eea; font-size: 2.2em; }
.result-dimension { margin-bottom: 20px; }
.score-text, .score-percent { display: flex; justify-content: space-between; font-weight: bold; font-size: 0.9em; margin-bottom: 5px; color: #444; }
.progress-bar { width: 100%; background-color: #e0e0e0; border-radius: 10px; height: 20px; overflow: hidden; }
.progress { height: 100%; transition: width 0.8s ease-in-out; }
.e-progress, .s-progress, .t-progress, .j-progress { background-color: #3498db; }
.restart-button {
  display: block;
  /* [개선] 버튼 너비 조절 및 중앙 정렬 */
  max-width: 250px;
  width: 100%;
  margin: 40px auto 0; /* 상단 여백 추가, 좌우 auto로 중앙 정렬 */
  
  font-size: 16px;
  font-weight: 500;
  padding: 12px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s;
  
  /* [개선] 버튼 색상 변경 (산뜻한 녹색 계열) */
  background-color: #27ae60;
  color: white;
}
.restart-button:hover {
  background-color: #229954;
}
.status-message.error {
    color: #e74c3c;
}
</style>