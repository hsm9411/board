import React, { useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { UserProvider } from './context/UserContext';
import { useUser } from './hooks/useUser';
import AppHeader from './components/AppHeader';
import ProtectedRoute from './components/ProtectedRoute';

// Page(View) Components
import BoardListView from './pages/board/BoardListView';
import BoardDetailView from './pages/board/BoardDetailView';
import BoardWriteView from './pages/board/BoardWriteView';
import BoardEditView from './pages/board/BoardEditView';
import LoginView from './pages/user/LoginView';
import SignupView from './pages/user/SignupView';

import './App.css';

function AppContent() {
    const { checkSession } = useUser();
    useEffect(() => { checkSession(); }, [checkSession]);

    return (
        // [수정] 전체 앱을 감싸는 div 추가
        <div className="app-wrapper">
            {/* [수정] Header는 이제 container 밖에 있어 항상 전체 너비를 차지합니다. */}
            <AppHeader />
            
            {/* [수정] 메인 콘텐츠만 container 안에 넣어 중앙 정렬합니다. */}
            <main className="container">
                <Routes>
                    <Route path="/" element={<Navigate to="/boards" replace />} />
                    <Route path="/boards" element={<BoardListView />} />
                    <Route path="/boards/view/:boardType/:boardNum" element={<BoardDetailView />} />
                    <Route path="/login" element={<LoginView />} />
                    <Route path="/signup" element={<SignupView />} />
                    <Route element={<ProtectedRoute />}>
                        <Route path="/boards/write" element={<BoardWriteView />} />
                        <Route path="/boards/edit/:boardType/:boardNum" element={<BoardEditView />} />
                    </Route>
                    <Route path="/mbti" element={<div>MBTI Intro Page</div>} />
                </Routes>
            </main>
        </div>
    );
}

function App() {
    return (
        <Router>
            <UserProvider>
                <AppContent />
            </UserProvider>
        </Router>
    );
}

export default App;