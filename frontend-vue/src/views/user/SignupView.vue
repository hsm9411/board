<template>
  <!-- template 부분은 변경 없습니다. -->
  <div class="auth-container">
    <div class="auth-form-wrapper signup">
      <h2 class="form-title">회원가입</h2>
      <form @submit.prevent="handleSubmit">
        <fieldset>
          <legend>필수 정보</legend>
          <div class="form-group">
            <label for="userId">아이디</label>
            <div class="input-group">
              <input type="text" id="userId" v-model="form.userId" @input="validateUserIdRealtime" maxlength="15" placeholder="영문자로 시작하는 4~15자 영문, 숫자" required>
              <button type="button" @click="checkId" :disabled="!form.userId || !idValidation.isValidFormat">중복 확인</button>
            </div>
            <ul class="validation-list"><li :class="{ 'valid': idValidation.isValidFormat }">✔ 영문자로 시작하는 4~15자 영문, 숫자</li></ul>
            <div class="message-box">
              <p v-if="errors.userId" class="error-message">{{ errors.userId }}</p>
              <p v-else-if="idStatus.isChecked && !idStatus.isDuplicate" class="success-message">사용 가능한 아이디입니다.</p>
            </div>
          </div>
          <div class="form-group">
            <label for="userName">이름</label>
            <input type="text" id="userName" v-model="form.userName" maxlength="15" placeholder="한글 5자, 영문 15자 이내" required>
            <div class="message-box"><p v-if="errors.userName" class="error-message">{{ errors.userName }}</p></div>
          </div>
          <div class="form-group">
            <label for="userPw">비밀번호</label>
            <input type="password" id="userPw" v-model="form.userPw" @input="validatePasswordRealtime" maxlength="16" placeholder="8~16자 영문, 숫자 조합" required>
            <ul class="validation-list">
              <li :class="{ 'valid': passwordValidation.isLengthValid }">✔ 8~16자 길이</li>
              <li :class="{ 'valid': passwordValidation.hasLetter }">✔ 영문자 포함</li>
              <li :class="{ 'valid': passwordValidation.hasNumber }">✔ 숫자 포함</li>
            </ul>
            <div class="message-box"><p v-if="errors.userPw" class="error-message">{{ errors.userPw }}</p></div>
          </div>
          <div class="form-group">
            <label for="userPwCheck">비밀번호 확인</label>
            <input type="password" id="userPwCheck" v-model="form.userPwCheck" @input="validatePasswordCheckRealtime" maxlength="16" required>
            <div class="message-box">
              <p v-if="errors.userPwCheck" class="error-message">{{ errors.userPwCheck }}</p>
              <p v-else-if="passwordCheckValidation.message" :class="passwordCheckValidation.isValid ? 'success-message' : 'error-message'">
                {{ passwordCheckValidation.message }}
              </p>
            </div>
          </div>
          <div class="form-group">
            <label for="userPhone1">전화번호</label>
            <div class="phone-group">
              <select id="userPhone1" v-model="form.userPhone1">
                <option v-for="code in phoneCodes" :key="code.codeId" :value="code.codeId">{{ code.codeName }}</option>
              </select>
              - <input type="text" v-model="form.userPhone2" maxlength="4" placeholder="1234" required>
              - <input type="text" v-model="form.userPhone3" maxlength="4" placeholder="5678" required>
            </div>
            <div class="message-box"><p v-if="errors.userPhone" class="error-message">{{ errors.userPhone }}</p></div>
          </div>
        </fieldset>
        <fieldset>
          <legend>추가 정보 (선택)</legend>
          <div class="form-group">
            <label for="userAddr1">우편번호</label>
            <input type="text" id="userAddr1" v-model="form.userAddr1" @input="formatZipcode" placeholder="123-456">
          </div>
          <div class="form-group">
            <label for="userAddr2">주소</label>
            <input type="text" id="userAddr2" v-model="form.userAddr2">
          </div>
          <div class="form-group">
            <label for="userCompany">회사명</label>
            <input type="text" id="userCompany" v-model="form.userCompany">
          </div>
        </fieldset>
        <p v-if="successMessage" class="success-message">{{ successMessage }}</p>
        <button type="submit" class="submit-button">가입하기</button>
      </form>
    </div>
  </div>
</template>

<script>
// [수정] apiClient 대신 userApi 모듈을 가져옵니다.
import { fetchCommonCodes, checkIdDuplicate, signup } from '@/api/userApi';

export default {
  data() {
    return {
      form: {
        userId: '', userName: '', userPw: '', userPwCheck: '',
        userPhone1: '010', userPhone2: '', userPhone3: '',
        userAddr1: '', userAddr2: '', userCompany: ''
      },
      errors: {},
      idStatus: { isChecked: false, isDuplicate: false, },
      idValidation: { isValidFormat: false },
      passwordValidation: { isLengthValid: false, hasLetter: false, hasNumber: false },
      passwordCheckValidation: { isValid: false, message: '' },
      phoneCodes: [],
      successMessage: '',
    };
  },
  created() {
    this.fetchPhoneCodes();
  },
  methods: {
    async fetchPhoneCodes() {
      try {
        // [수정] userApi의 함수를 사용합니다.
        this.phoneCodes = await fetchCommonCodes('phone');
      } catch (error) { console.error("전화번호 국번 목록 로딩 실패:", error); }
    },
    validateUserIdRealtime() {
      this.onIdInput();
      const idRegex = /^[a-zA-Z][a-zA-Z0-9]{3,14}$/;
      this.idValidation.isValidFormat = idRegex.test(this.form.userId);
    },
    validatePasswordRealtime() {
      const { userPw } = this.form;
      this.passwordValidation.isLengthValid = userPw.length >= 8 && userPw.length <= 16;
      this.passwordValidation.hasLetter = /[a-zA-Z]/.test(userPw);
      this.passwordValidation.hasNumber = /[0-9]/.test(userPw);
      this.validatePasswordCheckRealtime();
    },
    validatePasswordCheckRealtime() {
      const { userPw, userPwCheck } = this.form;
      if (!userPwCheck) { this.passwordCheckValidation.message = ''; this.passwordCheckValidation.isValid = false; return; }
      if (userPw === userPwCheck) { this.passwordCheckValidation.message = '비밀번호가 일치합니다.'; this.passwordCheckValidation.isValid = true; }
      else { this.passwordCheckValidation.message = '비밀번호가 일치하지 않습니다.'; this.passwordCheckValidation.isValid = false; }
    },
    formatZipcode(event) {
      let value = event.target.value.replace(/[^0-9]/g, '');
      if (value.length > 6) value = value.slice(0, 6);
      if (value.length > 3 && value.indexOf('-') === -1) { value = value.slice(0, 3) + '-' + value.slice(3); }
      this.form.userAddr1 = value;
    },
    onIdInput() { this.idStatus.isChecked = false; this.errors.userId = ''; },
    async checkId() {
      if (!this.form.userId || !this.idValidation.isValidFormat) { this.errors.userId = '아이디 형식이 올바르지 않습니다.'; return; }
      try {
        // [수정] userApi의 함수를 사용합니다.
        const data = await checkIdDuplicate(this.form.userId);
        this.idStatus.isDuplicate = data.isDuplicate;
        this.idStatus.isChecked = true;
        this.errors.userId = this.idStatus.isDuplicate ? '이미 사용 중인 아이디입니다.' : '';
      } catch (error) { this.errors.userId = '아이디 중복 확인 중 오류가 발생했습니다.'; }
    },
    async handleSubmit() {
      if (!this.validateForm()) return;
      try {
        // [수정] userApi의 함수를 사용합니다.
        const data = await signup(this.form);
        if (data.status === 'success') {
          alert(data.message);
          this.$router.push('/login');
        } else { this.errors = data.errors || { submit: '알 수 없는 오류가 발생했습니다.' }; }
      } catch (error) { alert(error.response?.data?.message || '회원가입 처리 중 서버 오류가 발생했습니다.'); }
    },
    validateForm() {
      this.errors = {}; let isValid = true;
      if (!this.idValidation.isValidFormat) { this.errors.userId = '아이디 형식이 올바르지 않습니다.'; isValid = false; }
      else if (!this.idStatus.isChecked || this.idStatus.isDuplicate) { this.errors.userId = '아이디 중복 확인을 통과해야 합니다.'; isValid = false; }
      if (!this.form.userName) { this.errors.userName = '이름을 입력해주세요.'; isValid = false; }
      const isPasswordValid = Object.values(this.passwordValidation).every(Boolean);
      if (!isPasswordValid) { this.errors.userPw = '비밀번호 조건을 모두 만족해야 합니다.'; isValid = false; }
      if (!this.passwordCheckValidation.isValid) { this.errors.userPwCheck = '비밀번호가 일치하지 않습니다.'; isValid = false; }
      if (!this.form.userPhone2 || !this.form.userPhone3) { this.errors.userPhone = '전화번호를 모두 입력해주세요.'; isValid = false; }
      if(!isValid) { alert('입력 내용을 다시 확인해주세요.'); }
      return isValid;
    }
  }
}
</script>

<style scoped>
/* style 부분은 변경 없습니다. */
.message-box{min-height:22px;padding-top:4px}.message-box p{margin:0}.auth-container{display:flex;justify-content:center;align-items:center;padding:40px 0}.auth-form-wrapper{width:100%;max-width:500px;padding:40px;background:#fff;border-radius:8px;box-shadow:0 4px 12px #0000001a}.auth-form-wrapper.signup{max-width:600px}.form-title{text-align:center;font-size:24px;margin-bottom:30px;color:#333}fieldset{margin-bottom:20px;border:1px solid #ddd;border-radius:5px;padding:20px}legend{font-weight:700}.form-group{margin-bottom:10px}.form-group label{display:block;margin-bottom:8px;font-weight:600;color:#555}.form-group input,.form-group select{width:100%;padding:12px;border:1px solid #ccc;border-radius:4px;box-sizing:border-box;transition:border-color .3s}.form-group input:focus,.form-group select:focus{outline:none;border-color:#007bff}.input-group{display:flex}.input-group input{flex-grow:1;border-top-right-radius:0;border-bottom-right-radius:0}.input-group button{flex-shrink:0;border-top-left-radius:0;border-bottom-left-radius:0;padding:0 15px}.phone-group{display:flex;align-items:center;gap:10px}.phone-group select{width:100px}.phone-group input{width:100%}.error-message{color:#dc3545;font-size:14px}.success-message{color:#28a745;font-size:14px}.submit-button{width:100%;padding:12px;background-color:#007bff;color:#fff;border:none;border-radius:4px;cursor:pointer;font-size:16px;font-weight:600;margin-top:20px}.validation-list{list-style:none;padding:0;margin:8px 0 0;font-size:14px}.validation-list li{color:#6c757d;transition:color .3s}.validation-list li.valid{color:#28a745;font-weight:600}
</style>