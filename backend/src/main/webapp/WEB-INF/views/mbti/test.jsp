<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>MBTI 성격 유형 검사</title>
<style>
    body {
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
        background-color: #f4f4f9;
        display: flex;
        justify-content: center;
        align-items: center;
        min-height: 100vh;
        padding: 40px 20px;
        box-sizing: border-box;
        margin: 0;
    }
    .container {
        background-color: white;
        padding: 30px 40px;
        border-radius: 12px;
        box-shadow: 0 6px 20px rgba(0,0,0,0.1);
        width: 100%;
        max-width: 800px;
        transition: all 0.3s ease-in-out;
    }
    h1 {
        color: #333;
        text-align: center;
        border-bottom: 2px solid #eee;
        padding-bottom: 15px;
        margin-bottom: 30px;
    }
    #question-form {
        display: none;
    }
    .question-page {
        display: none;
    }
    .question-item {
        margin-bottom: 30px;
        padding: 20px;
        border: 1px solid #e0e0e0;
        border-radius: 8px;
        background-color: #fafafa;
        transition: all 0.3s ease; /* 부드러운 전환 효과 */
    }
    .question-item p {
        font-size: 1.1em;
        font-weight: 500;
        margin: 0 0 15px 0;
    }
    .options {
        display: flex;
        justify-content: space-between;
        align-items: center;
        flex-wrap: wrap;
    }
    .option-label {
        display: flex;
        flex-direction: column;
        align-items: center;
        cursor: pointer;
        text-align: center;
        font-size: 0.9em;
        color: #555;
    }
    .option-label input[type="radio"] {
        margin-bottom: 5px;
    }
    .text-agree { color: #2980b9; font-weight: bold; }
    .text-disagree { color: #c0392b; font-weight: bold; }
    
    /* [개선] 미답변 질문에 대한 시각적 피드백 스타일 */
    .unanswered {
        border-color: #e74c3c;
        background-color: #fff5f5;
        animation: shake 0.5s;
    }
    @keyframes shake {
        0%, 100% { transform: translateX(0); }
        10%, 30%, 50%, 70%, 90% { transform: translateX(-5px); }
        20%, 40%, 60%, 80% { transform: translateX(5px); }
    }

    /* 버튼 스타일 */
    #start-btn, .nav-btn, #submit-btn, #restart-btn {
        font-size: 16px;
        padding: 10px 20px;
        border-radius: 8px;
        border: none;
        background-color: #667eea;
        color: white;
        cursor: pointer;
        margin: 0 10px;
        transition: background-color 0.3s;
    }
    #start-btn:hover, .nav-btn:hover, #submit-btn:hover, #restart-btn:hover {
        background-color: #5a6ed8;
    }
    #submit-btn:disabled, .nav-btn:disabled {
        background-color: #ccc;
        cursor: not-allowed;
    }
    /* [추가] 다시 검사하기 버튼 스타일 */
    #restart-btn {
        background-color: #7f8c8d;
        display: block;
        width: calc(100% - 20px);
        margin-top: 25px;
    }
    #restart-btn:hover {
        background-color: #6c7a7b;
    }

    #pagination-controls {
        display: flex;
        justify-content: center;
        align-items: center;
        margin-top: 20px;
    }
    #page-indicator {
        font-size: 1.1em;
        font-weight: bold;
        color: #555;
    }

    /* 결과 및 로딩 영역 스타일 */
    .status {
        font-weight: bold;
        text-align: center;
        padding: 20px;
        border-radius: 8px;
        background-color: #f0f0f0;
    }
    #result-area {
        display: none;
        padding: 30px;
        border: 1px solid #ddd;
        border-radius: 8px;
        margin-top: 20px;
    }
    #result-header {
        text-align: center;
    }
    #result-header h2 { margin: 0 0 5px 0; color: #333; }
    #result-header p { font-size: 1.5em; margin: 0 0 25px 0; }
    #result-header p strong { color: #667eea; font-size: 1.8em; }

    .result-dimension { margin-bottom: 20px; }
    .score-text, .score-percent { display: flex; justify-content: space-between; font-weight: bold; font-size: 0.9em; margin-bottom: 5px; color: #444; }
    .progress-bar { width: 100%; background-color: #e0e0e0; border-radius: 10px; height: 20px; overflow: hidden; display: flex; }
    .progress { height: 100%; transition: width 0.8s ease-in-out; }
    .e-progress, .s-progress, .t-progress, .j-progress { background-color: #3498db; }
    .i-progress, .n-progress, .f-progress, .p-progress { background-color: #e74c3c; }

</style>
</head>
<body>

<div class="container">
    <h1>MBTI 성격 유형 검사</h1>

    <div id="intro-area">
        <p class="status">총 20개의 질문에 답변하여 자신의 성격 유형을 알아보세요.<br>아래 버튼을 눌러 검사를 시작하세요.</p>
        <button id="start-btn" class="nav-btn" style="width: 100%; font-size: 18px;">검사 시작하기</button>
    </div>

    <form id="question-form">
        <div id="question-list"></div>
        <div id="pagination-controls">
            <button type="button" id="prev-btn" class="nav-btn">이전</button>
            <span id="page-indicator"></span>
            <button type="button" id="next-btn" class="nav-btn">다음</button>
            <button id="submit-btn" type="submit" style="display: none;">결과 확인</button>
        </div>
    </form>

    <div id="loading-area" class="status" style="display: none;"></div>

    <div id="result-area">
        <div id="result-header">
            <h2>검사 결과</h2>
            <p>당신의 MBTI 유형은 <br><strong id="mbti-type"></strong> 입니다.</p>
        </div>
        <div class="result-details">
            <!-- 결과 프로그레스 바 영역 -->
            <div class="result-dimension">
                <div class="score-text"><span>E (외향)</span><span>I (내향)</span></div>
                <div class="progress-bar"><div class="progress e-progress" id="e-progress"></div></div>
                <div class="score-percent"><span id="e-percent"></span><span id="i-percent"></span></div>
            </div>
            <div class="result-dimension">
                <div class="score-text"><span>S (감각)</span><span>N (직관)</span></div>
                <div class="progress-bar"><div class="progress s-progress" id="s-progress"></div></div>
                <div class="score-percent"><span id="s-percent"></span><span id="n-percent"></span></div>
            </div>
            <div class="result-dimension">
                <div class="score-text"><span>T (사고)</span><span>F (감정)</span></div>
                <div class="progress-bar"><div class="progress t-progress" id="t-progress"></div></div>
                <div class="score-percent"><span id="t-percent"></span><span id="f-percent"></span></div>
            </div>
            <div class="result-dimension">
                <div class="score-text"><span>J (판단)</span><span>P (인식)</span></div>
                <div class="progress-bar"><div class="progress j-progress" id="j-progress"></div></div>
                <div class="score-percent"><span id="j-percent"></span><span id="p-percent"></span></div>
            </div>
        </div>
        <!-- [추가] 다시 검사하기 버튼 -->
        <button type="button" id="restart-btn">다시 검사하기</button>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {

    // --- DOM 요소 가져오기 ---
    var startBtn = document.getElementById('start-btn');
    var introArea = document.getElementById('intro-area');
    var questionForm = document.getElementById('question-form');
    var questionList = document.getElementById('question-list');
    var loadingArea = document.getElementById('loading-area');
    var resultArea = document.getElementById('result-area');
    var prevBtn = document.getElementById('prev-btn');
    var nextBtn = document.getElementById('next-btn');
    var submitBtn = document.getElementById('submit-btn');
    var pageIndicator = document.getElementById('page-indicator');
    var restartBtn = document.getElementById('restart-btn'); // 추가

    // --- 페이지 나누기 관련 변수 ---
    var allQuestions = [];
    var currentPage = 0;
    var questionsPerPage = 5;
    var totalPages = 0;
    var questionPages = [];

    // --- 1. 검사 시작 이벤트 ---
    startBtn.addEventListener('click', startTest);

    function startTest() {
        introArea.style.display = 'none';
        resultArea.style.display = 'none';
        loadingArea.textContent = '질문을 불러오는 중입니다...';
        loadingArea.style.display = 'block';

        fetch('/mbti/api/questions.do')
            .then(function(response) { if (!response.ok) throw new Error('서버 오류'); return response.json(); })
            .then(function(questions) {
                allQuestions = questions;
                displayQuestions(allQuestions);
                loadingArea.style.display = 'none';
                questionForm.style.display = 'block';
                showPage(0);
            })
            .catch(function(error) {
                console.error('질문 로딩 오류:', error);
                loadingArea.textContent = '오류가 발생했습니다. 새로고침 해주세요.';
            });
    }

    // --- 2. 질문 HTML 생성 ---
    function displayQuestions(questions) {
        questionList.innerHTML = '';
        totalPages = Math.ceil(questions.length / questionsPerPage);
        
        for (var i = 0; i < totalPages; i++) {
            var pageDiv = document.createElement('div');
            pageDiv.className = 'question-page';
            
            var start = i * questionsPerPage;
            var end = start + questionsPerPage;
            var pageQuestions = questions.slice(start, end);

            pageQuestions.forEach(function(question, index) {
                var questionIndex = start + index;
                var questionItem = document.createElement('div');
                questionItem.className = 'question-item';

                var questionText = document.createElement('p');
                questionText.textContent = (questionIndex + 1) + '. ' + question.boardComment;

                var optionsDiv = document.createElement('div');
                optionsDiv.className = 'options';
                optionsDiv.dataset.boardType = question.boardType;

                var optionLabels = [
                    { text: '매우<br>비동의', value: 1, class: 'text-disagree' }, { text: '비동의', value: 2, class: 'text-disagree' },
                    { text: '약간<br>비동의', value: 3, class: 'text-disagree' }, { text: '보통', value: 4, class: '' },
                    { text: '약간<br>동의', value: 5, class: 'text-agree' }, { text: '동의', value: 6, class: 'text-agree' },
                    { text: '매우<br>동의', value: 7, class: 'text-agree' }
                ];

                optionLabels.forEach(function(opt) {
                    var label = document.createElement('label');
                    label.className = 'option-label';
                    var radio = document.createElement('input');
                    radio.type = 'radio';
                    radio.name = 'question_' + questionIndex;
                    radio.value = opt.value;
                    radio.required = true;
                    var span = document.createElement('span');
                    span.innerHTML = opt.text;
                    span.className = opt.class;
                    label.appendChild(radio);
                    label.appendChild(span);
                    optionsDiv.appendChild(label);
                });
                
                questionItem.appendChild(questionText);
                questionItem.appendChild(optionsDiv);
                pageDiv.appendChild(questionItem);
            });
            questionList.appendChild(pageDiv);
        }
        questionPages = document.querySelectorAll('.question-page');
    }

    // --- 3. 특정 페이지 보여주기 ---
    function showPage(pageIndex) {
        questionPages.forEach(function(page, index) { page.style.display = (index === pageIndex) ? 'block' : 'none'; });
        currentPage = pageIndex;
        pageIndicator.textContent = (currentPage + 1) + ' / ' + totalPages;
        prevBtn.style.display = (currentPage === 0) ? 'none' : 'inline-block';
        nextBtn.style.display = (currentPage === totalPages - 1) ? 'none' : 'inline-block';
        submitBtn.style.display = (currentPage === totalPages - 1) ? 'inline-block' : 'none';
    }

    // --- 4. [개선] 현재 페이지 유효성 검사 (부드러운 피드백) ---
    function validateCurrentPage() {
        var isValid = true;
        var currentPageDiv = questionPages[currentPage];
        var questionsOnPage = currentPageDiv.querySelectorAll('.question-item');

        questionsOnPage.forEach(function(item) {
            var isChecked = item.querySelector('input[type="radio"]:checked');
            if (!isChecked) {
                isValid = false;
                item.classList.add('unanswered'); // 미답변 항목에 클래스 추가
            } else {
                item.classList.remove('unanswered'); // 답변하면 클래스 제거
            }
        });

        // 미답변 항목이 있을 경우, 잠시 후 흔들림 효과 클래스 제거
        if (!isValid) {
            setTimeout(function() {
                var unansweredItems = currentPageDiv.querySelectorAll('.unanswered');
                unansweredItems.forEach(function(item) {
                    item.classList.remove('unanswered');
                });
            }, 600); // 애니메이션 시간보다 약간 길게 설정
        }

        return isValid;
    }

    // --- 5. 페이지 이동 버튼 이벤트 ---
    nextBtn.addEventListener('click', function() {
        if (validateCurrentPage()) {
            showPage(currentPage + 1);
        }
    });

    prevBtn.addEventListener('click', function() {
        showPage(currentPage - 1);
    });

    // --- 6. 폼 제출 (결과 확인) 이벤트 ---
    questionForm.addEventListener('submit', function(event) {
        event.preventDefault();
        if (!validateCurrentPage()) { return; }

        var answers = [];
        var optionDivs = questionList.querySelectorAll('.options');
        optionDivs.forEach(function(div, index) {
            var boardType = div.dataset.boardType;
            var checkedRadio = div.querySelector('input[name="question_' + index + '"]:checked');
            if (checkedRadio) { answers.push({ boardType: boardType, answerValue: parseInt(checkedRadio.value, 10) }); }
        });
        
        questionForm.style.display = 'none';
        loadingArea.textContent = '결과를 분석 중입니다. 잠시만 기다려주세요...';
        loadingArea.style.display = 'block';

        fetch('/mbti/api/result.do', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(answers)
        })
        .then(function(response) { if (!response.ok) throw new Error('결과를 받아오는데 실패했습니다.'); return response.json(); })
        .then(function(result) { displayResult(result); })
        .catch(function(error) {
            console.error('결과 처리 오류:', error);
            loadingArea.textContent = '오류가 발생했습니다. 다시 시도해주세요.';
        });
    });

    // --- 7. 최종 결과 표시 ---
    function displayResult(result) {
        loadingArea.style.display = 'none';
        resultArea.style.display = 'block';
        document.getElementById('mbti-type').textContent = result.mbtiType;
        document.getElementById('e-progress').style.width = result.eScore + '%';
        document.getElementById('e-percent').textContent = result.eScore + '%';
        document.getElementById('i-percent').textContent = result.iScore + '%';
        document.getElementById('s-progress').style.width = result.sScore + '%';
        document.getElementById('s-percent').textContent = result.sScore + '%';
        document.getElementById('n-percent').textContent = result.nScore + '%';
        document.getElementById('t-progress').style.width = result.tScore + '%';
        document.getElementById('t-percent').textContent = result.tScore + '%';
        document.getElementById('f-percent').textContent = result.fScore + '%';
        document.getElementById('j-progress').style.width = result.jScore + '%';
        document.getElementById('j-percent').textContent = result.jScore + '%';
        document.getElementById('p-percent').textContent = result.pScore + '%';
    }

    // --- 8. [추가] 다시 검사하기 버튼 이벤트 ---
    restartBtn.addEventListener('click', function() {
        resultArea.style.display = 'none';
        introArea.style.display = 'block';
        // 변수 초기화
        allQuestions = [];
        currentPage = 0;
    });
});
</script>

</body>
</html>