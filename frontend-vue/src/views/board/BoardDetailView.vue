<template>
  <!-- template 부분은 변경 없습니다. -->
  <div class="board-detail">
    <div v-if="loading"><p>게시글을 불러오는 중입니다...</p></div>
    <div v-else-if="board" class="board-content">
      <h2>{{ board.boardTitle }}</h2>
      <div class="meta-info"><span>작성자: {{ board.creator }}</span></div>
      <hr>
      <p class="comment" style="white-space: pre-wrap;">{{ board.boardComment }}</p>
      <div class="button-group">
        <button @click="goToList">목록으로</button>
        <div v-if="board.isOwner" class="owner-buttons">
          <button @click="goToEdit">수정</button>
          <button @click="handleDelete">삭제</button>
        </div>
      </div>
    </div>
    <div v-else><p>해당 게시글을 찾을 수 없습니다.</p></div>
  </div>
</template>

<script>
// [수정] apiClient 대신 boardApi 모듈을 가져옵니다.
import { fetchBoardDetail, deleteBoard } from '@/api/boardApi';

export default {
  props: {
    boardType: { type: String, required: true },
    boardNum: { type: [String, Number], required: true }
  },
  data() {
    return {
      board: null,
      loading: true,
    };
  },
  created() {
    this.fetchBoardDetail();
  },
  methods: {
    async fetchBoardDetail() {
      this.loading = true;
      try {
        // [수정] boardApi의 함수를 사용합니다.
        this.board = await fetchBoardDetail(this.boardType, this.boardNum);
      } catch (error) {
        console.error("게시글 상세 정보 로딩 실패:", error);
        alert('게시글을 불러오는 데 실패했습니다.');
        this.board = null;
      } finally {
        this.loading = false;
      }
    },
    goToList() { this.$router.push('/boards'); },
    goToEdit() { this.$router.push(`/boards/edit/${this.boardType}/${this.boardNum}`); },
    async handleDelete() {
      if (confirm("정말로 이 게시글을 삭제하시겠습니까?")) {
        try {
          // [수정] boardApi의 함수를 사용합니다.
          await deleteBoard(this.boardType, this.boardNum);
          alert('게시글이 삭제되었습니다.');
          this.goToList();
        } catch (error) {
          console.error("게시글 삭제 실패:", error);
          alert('게시글 삭제에 실패했습니다.');
        }
      }
    }
  }
}
</script>

<style scoped>
/* style 부분은 변경 없습니다. */
.board-detail{padding:20px;max-width:800px;margin:0 auto}.meta-info{color:#888;margin-bottom:20px}.comment{min-height:200px;line-height:1.6}.button-group{margin-top:20px;text-align:right}.button-group button{margin-left:10px;padding:8px 16px}.owner-buttons{display:inline-block}
</style>