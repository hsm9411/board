<template>
  <!-- template 부분은 변경 없습니다. -->
  <div class="board-edit">
    <h2>게시글 수정</h2>
    <div v-if="loading"><p>데이터를 불러오는 중입니다...</p></div>
    <form v-else @submit.prevent="handleSubmit">
      <div class="form-group">
        <label>게시판 종류</label>
        <input type="text" :value="board.boardTypeName" readonly>
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
        <button type="submit">수정</button>
      </div>
    </form>
  </div>
</template>

<script>
// [수정] apiClient 대신 boardApi 모듈을 가져옵니다.
import { fetchBoardDetail, updateBoard } from '@/api/boardApi';
import { useUserStore } from '@/store/userStore';

export default {
  props: {
    boardType: { type: String, required: true },
    boardNum: { type: [String, Number], required: true }
  },
  data() {
    return {
      board: { boardTitle: '', boardComment: '', creator: '', boardTypeName: '' },
      loading: true
    };
  },
  created() {
    this.fetchBoardData();
  },
  methods: {
    async fetchBoardData() {
      this.loading = true;
      try {
        // [수정] boardApi의 함수를 사용합니다.
        const fetchedBoard = await fetchBoardDetail(this.boardType, this.boardNum);
        const userStore = useUserStore();
        if (!userStore.isLoggedIn || userStore.user.userName !== fetchedBoard.creator) {
          alert('수정 권한이 없습니다.');
          this.$router.push(`/boards/view/${this.boardType}/${this.boardNum}`);
          return;
        }
        this.board = fetchedBoard;
      } catch (error) {
        console.error("게시글 데이터 로딩 실패:", error);
        alert('게시글 정보를 불러오는 데 실패했습니다.');
        this.$router.go(-1);
      } finally {
        this.loading = false;
      }
    },
    async handleSubmit() {
      if (!this.board.boardTitle.trim() || !this.board.boardComment.trim()) {
        alert('제목과 내용을 모두 입력해주세요.');
        return;
      }
      try {
        // [수정] boardApi의 함수를 사용합니다.
        const boardData = {
          boardTitle: this.board.boardTitle,
          boardComment: this.board.boardComment
        };
        await updateBoard(this.boardType, this.boardNum, boardData);
        alert('게시글이 성공적으로 수정되었습니다.');
        this.$router.push(`/boards/view/${this.boardType}/${this.boardNum}`);
      } catch (error) {
        console.error("게시글 수정 실패:", error);
        alert(error.response?.data?.message || '게시글 수정에 실패했습니다.');
      }
    },
    cancel() {
      this.$router.push(`/boards/view/${this.boardType}/${this.boardNum}`);
    }
  }
}
</script>

<style scoped>
/* style 부분은 변경 없습니다. */
.board-edit{max-width:600px;margin:0 auto}.form-group{margin-bottom:15px}.form-group label{display:block;margin-bottom:5px}.form-group input,.form-group textarea{width:100%;padding:8px;box-sizing:border-box}.form-group input[readonly]{background-color:#e9ecef;cursor:not-allowed}.button-group{text-align:right}.button-group button{margin-left:10px}
</style>