<!-- /src/components/BasicTable.vue -->

<template>
  <table class="basic-table">
    <thead>
      <tr>
        <!-- [핵심 1] 부모로부터 받은 'headers' 배열을 사용하여 a a a를 동적으로 생성 -->
        <th v-for="header in headers" :key="header.key" :style="{ width: header.width }">
          {{ header.text }}
        </th>
      </tr>
    </thead>
    <tbody>
      <!-- [핵심 2] 부모로부터 받은 'items' 배열을 사용하여 행(row)들을 동적으로 생성 -->
      <tr v-for="(item, index) in items" :key="index" @click="onRowClick(item)">
        <!-- headers 배열의 순서에 맞춰 각 셀(cell)의 데이터를 동적으로 렌더링 -->
        <td v-for="header in headers" :key="header.key">
          <!-- [핵심 3] 슬롯(slot)을 사용하여 부모가 셀의 디자인을 커스터마이징 할 수 있도록 함 -->
          <!-- 예: 제목(title) 컬럼을 단순 텍스트가 아닌 링크(<a>)로 보여주기 -->
          <slot :name="`cell(${header.key})`" :item="item">
            <!-- 기본값: 그냥 item의 데이터를 텍스트로 보여줌 -->
            {{ item[header.key] }}
          </slot>
        </td>
      </tr>
      <!-- 데이터가 없을 때의 처리 -->
      <tr v-if="items.length === 0">
        <td :colspan="headers.length">
          표시할 데이터가 없습니다.
        </td>
      </tr>
    </tbody>
  </table>
</template>

<script>
export default {
  props: {
    // 부모로부터 받을 데이터의 종류와 타입을 정의
    headers: {
      type: Array, // [{ key: 'id', text: '번호', width: '10%' }, ...] 형태의 배열
      required: true,
    },
    items: {
      type: Array, // [{ id: 1, title: '...' }, ...] 형태의 데이터 배열
      required: true,
    },
  },
  methods: {
    // 행이 클릭되었을 때, 부모에게 'row-click' 이라는 이름의 이벤트를 발생시킴
    // 클릭된 행의 데이터(item)를 함께 전달
    onRowClick(item) {
      this.$emit('row-click', item);
    }
  }
}
</script>

<style scoped>
/* BasicTable에만 적용되는 스타일 */
.basic-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}
.basic-table th, .basic-table td {
  border: 1px solid #ddd;
  padding: 12px;
  text-align: center;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.basic-table thead {
  background-color: #f8f9fa;
  font-weight: 600;
}
.basic-table tbody tr {
  cursor: pointer;
  transition: background-color 0.2s;
}
.basic-table tbody tr:hover {
  background-color: #f1f1f1;
}
</style>