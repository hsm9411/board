<template>
  <!-- template 부분은 변경 없습니다. -->
  <div>
    <h1>게시판 목록</h1>
    <div class="board-info">
      <span>총 게시물: {{ pageInfo.totalCnt || 0 }}건</span>
    </div>
    <div class="filter-section">
      <div class="filter-options">
        <label>
          <input type="checkbox" v-model="isAllSelected"> 전체
        </label>
        <label v-for="type in boardTypes" :key="type.codeId">
          <input type="checkbox" :value="type.codeId" v-model="selectedTypes"> {{ type.codeName }}
        </label>
      </div>
      <button @click="fetchBoardList(1)" class="search-button">검색</button>
    </div>
    <BasicTable 
      :headers="tableHeaders" 
      :items="boardList"
      @row-click="goToDetail"
    />
    <div v-if="pageInfo.totalCnt > 0" class="pagination">
      <a href="#" class="page-link" @click.prevent="fetchBoardList(1)">&laquo;</a>
      <a href="#" v-if="pageInfo.prev" class="page-link" @click.prevent="fetchBoardList(pageInfo.startPage - 1)">&lsaquo;</a>
      <span v-else class="page-link disabled">&lsaquo;</span>
      <a 
        href="#" 
        v-for="page in pageRange" 
        :key="page" 
        class="page-link"
        :class="{ active: page === pageInfo.pageNo }"
        @click.prevent="fetchBoardList(page)"
      >
        {{ page }}
      </a>
      <a href="#" v-if="pageInfo.next" class="page-link" @click.prevent="fetchBoardList(pageInfo.endPage + 1)">&rsaquo;</a>
      <span v-else class="page-link disabled">&rsaquo;</span>
      <a href="#" class="page-link" @click.prevent="fetchBoardList(pageInfo.totalPage)">&raquo;</a>
    </div>
    <div class="table-footer">
      <router-link v-if="isLoggedIn" to="/boards/write" class="write-button">글쓰기</router-link>
    </div>
  </div>
</template>

<script>
// [수정] apiClient 대신 boardApi 모듈을 가져옵니다.
import { fetchBoardTypes, fetchBoardList } from '@/api/boardApi';
import { mapState } from 'pinia';
import { useUserStore } from '@/store/userStore';
import BasicTable from '@/components/BasicTable.vue';

export default {
  components: { BasicTable },
  data() {
    return {
      boardList: [], pageInfo: {}, boardTypes: [], selectedTypes: [],
      tableHeaders: [
        { text: '타입', key: 'boardTypeName', width: '15%' },
        { text: '번호', key: 'boardNum', width: '10%' },
        { text: '제목', key: 'boardTitle', width: '50%' },
        { text: '작성자', key: 'creator', width: '25%' },
      ]
    };
  },
  computed: {
    ...mapState(useUserStore, ['isLoggedIn']),
    pageRange() {
      if (!this.pageInfo.startPage) return [];
      const pages = [];
      for (let i = this.pageInfo.startPage; i <= this.pageInfo.endPage; i++) {
        pages.push(i);
      }
      return pages;
    },
    isAllSelected: {
      get() { return this.boardTypes.length > 0 && this.selectedTypes.length === this.boardTypes.length; },
      set(value) { this.selectedTypes = value ? this.boardTypes.map(type => type.codeId) : []; }
    }
  },
  created() {
    this.initializePage();
  },
  methods: {
    goToDetail(item) { this.$router.push(`/boards/view/${item.boardType}/${item.boardNum}`); },
    async initializePage() {
      await this.fetchBoardTypes();
      const pageNo = this.$route.query.page || 1;
      this.fetchBoardList(pageNo);
    },
    async fetchBoardTypes() {
      try {
        // [수정] boardApi의 함수를 사용합니다.
        this.boardTypes = await fetchBoardTypes();
        this.selectedTypes = this.boardTypes.map(type => type.codeId);
      } catch (error) { console.error("게시판 타입 로딩 실패:", error); }
    },
    async fetchBoardList(pageNo) {
      if (pageNo < 1 || (this.pageInfo.totalPage && pageNo > this.pageInfo.totalPage)) {
        return;
      }
      try {
        // [수정] boardApi의 함수를 사용합니다.
        const data = await fetchBoardList(pageNo, this.selectedTypes);
        this.boardList = data.boardList;
        this.pageInfo = data.pageInfo;
        
        const query = { page: pageNo };
        if(this.selectedTypes.length !== this.boardTypes.length) {
            query.types = this.selectedTypes;
        }
        this.$router.push({ query });

      } catch (error) {
        console.error("게시글 목록 로딩 실패:", error);
        alert('게시글을 불러오는 데 실패했습니다.');
      }
    }
  }
}
</script>

<style scoped>
/* style 부분은 변경 없습니다. */
.board-info{text-align:right;margin-bottom:10px;color:#555}.filter-section{display:flex;justify-content:space-between;align-items:center;padding:10px;background-color:#f5f5f5;border-radius:5px;margin-bottom:20px}.filter-options label{margin-right:15px;cursor:pointer}.search-button{padding:5px 15px;background-color:#4CAF50;color:#fff;border:none;border-radius:3px;cursor:pointer}.pagination{display:flex;justify-content:center;align-items:center;margin-top:30px}.page-link{color:#333;padding:8px 16px;text-decoration:none;border:1px solid #ddd;margin:0 4px;border-radius:4px;transition:background-color .2s,color .2s;-webkit-user-select:none;-moz-user-select:none;user-select:none}.page-link:hover{background-color:#f5f5f5;border-color:#ccc}.page-link.active{background-color:#007bff;color:#fff;border-color:#007bff;font-weight:700;cursor:default}.page-link.disabled{color:#ccc;pointer-events:none;background-color:#f9f9f9}.table-footer{margin-top:20px;display:flex;justify-content:flex-end}.write-button{padding:10px 20px;background-color:#007bff;color:#fff;text-decoration:none;border-radius:5px}
</style>