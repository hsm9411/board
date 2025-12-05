import apiClient from './apiClient';

/**
 * MBTI 검사에 필요한 모든 API 호출을 관리합니다.
 */

export const fetchMbtiQuestions = async () => {
    try {
        const response = await apiClient.get('/api/mbti/questions');
        return response.data;
    } catch (error) {
        console.error('MBTI 질문 로딩 실패:', error);
        throw new Error('질문을 불러오는 데 실패했습니다.');
    }
};

export const submitMbtiAnswers = async (answers) => {
    try {
        const response = await apiClient.post('/api/mbti/result', answers);
        return response.data;
    } catch (error) {
        console.error('MBTI 결과 전송 실패:', error);
        throw new Error('결과를 처리하는 중 오류가 발생했습니다.');
    }
};