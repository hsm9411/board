import { useContext } from 'react';
import { UserContext } from '../context/UserContext';

// useUser 커스텀 훅을 별도의 파일로 분리하여 export 합니다.
export const useUser = () => {
    const context = useContext(UserContext);
    // Provider 범위 밖에서 훅을 사용하는 실수를 방지합니다.
    if (context === undefined) {
        throw new Error('useUser must be used within a UserProvider');
    }
    return context;
};