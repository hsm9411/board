<template>
  <!-- template 부분은 변경 없습니다. -->
  <div class="board-write">
    <h2>새 게시글 작성</h2>
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="boardType">게시판 종류</label>
        <select id="boardType" v-model="board.boardType" required>
          <option disabled value="">게시판을 선택하세요</option>
          <option v-for="type in boardTypes" :key="type.codeId" :value="type.codeId">
            {{ type.codeName }}
          </option>
        </select>
      </div>
      <div class="form-group">
        <label for="boardTitle">제목</label>
        <input type="text" id="boardTitle" v-model="board.boardTitle" required>
      </div>
      <div class="form-group">
        <label for="boardComment">내용</label>
        <textarea id="boardComment" v-model="board.boardComment" rows="10" required></textarea>
      </div>
      <div class="button-group">
        <button type="button" @click="cancel">취소</button>
        <button type="submit">작성</button>
      </div>
    </form>
  </div>
</template>

<script>
// [수정] apiClient 대신 boardApi 모듈을 가져옵니다.
import { fetchBoardTypes, writeBoard } from '@/api/boardApi';

export default {
  data() {
    return {
      board: { boardType: '', boardTitle: '', boardComment: '' },
      boardTypes: []
    };
  },
  created() {
    this.fetchBoardTypes();
  },
  methods: {
    async fetchBoardTypes() {
      try {
        // [수정] boardApi의 함수를 사용합니다.
        this.boardTypes = await fetchBoardTypes();
        if (this.boardTypes.length > 0) {
          this.board.boardType = this.boardTypes[0].codeId;
        }
      } catch (error) {
        console.error("게시판 종류 로딩 실패:", error);
      }
    },
    async handleSubmit() {
      if (!this.board.boardType || !this.board.boardTitle.trim() || !this.board.boardComment.trim()) {
        alert('모든 필드를 입력해주세요.');
        return;
      }
      try {
        // [수정] boardApi의 함수를 사용합니다.
        await writeBoard(this.board);
        alert('게시글이 성공적으로 등록되었습니다.');
        this.$router.push('/boards');
      } catch (error) {
        console.error("게시글 등록 실패:", error);
        alert(error.response?.data?.message || '게시글 등록에 실패했습니다.');
      }
    },
    cancel() {
      this.$router.go(-1);
    }
  }
}
</script>

<style scoped>
/* style 부분은 변경 없습니다. */
.board-write{max-width:600px;margin:0 auto}.form-group{margin-bottom:15px}.form-group label{display:block;margin-bottom:5px}.form-group input,.form-group select,.form-group textarea{width:100%;padding:8px;box-sizing:border-box}.button-group{text-align:right}.button-group button{margin-left:10px}
</style>