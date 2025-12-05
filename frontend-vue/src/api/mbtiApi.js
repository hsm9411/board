import apiClient from './apiClient';

/**
 * MBTI 검사에 필요한 모든 API 호출을 관리합니다.
 * (axios/apiClient 사용으로 리팩토링)
 */

/**
 * 서버로부터 MBTI 질문 목록을 가져옵니다.
 * @returns {Promise<Array>} 질문 객체 배열
 */
export const fetchMbtiQuestions = async () => {
    try {
        // [수정] 새로 만든 Vue 전용 API(/api/mbti/questions)를 호출합니다.
        const response = await apiClient.get('/api/mbti/questions');
        // axios는 응답 데이터를 response.data에 담아줍니다.
        return response.data;
    } catch (error) {
        console.error('MBTI 질문 로딩 실패:', error);
        // 에러를 상위로 전파하여 컴포넌트에서 처리할 수 있도록 합니다.
        throw new Error('질문을 불러오는 데 실패했습니다.');
    }
};

/**
 * 사용자의 답변 목록을 서버로 전송하고 결과를 받습니다.
 * @param {Array} answers - 사용자의 답변 객체 배열
 * @returns {Promise<Object>} MBTI 결과 객체
 */
export const submitMbtiAnswers = async (answers) => {
    try {
        // [수정] 새로 만든 Vue 전용 API(/api/mbti/result)를 호출합니다.
        const response = await apiClient.post('/api/mbti/result', answers);
        return response.data;
    } catch (error) {
        console.error('MBTI 결과 전송 실패:', error);
        throw new Error('결과를 처리하는 중 오류가 발생했습니다.');
    }
};