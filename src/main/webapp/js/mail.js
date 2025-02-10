var minMailNo = -1;
var recentMailNo = -1;



function loadMailList(type = 'received') {
	const controller = type === 'sent' ? 'SendMailController.jsp' : 'ReciveMailController.jsp';
	$.ajax({
		url: `../Mail/${controller}`,
		type: 'GET',
		dataType: 'json',
		data: { server_code: AllSession.serverGet() }, // Ensure currentServerCode is set
		success: function(mails) {
			if (mails.error) {
				console.error(mails.error);
				return;
			}

			if (mails.length > 0) {
				minMailNo = mails[mails.length - 1].mail_code;
				recentMailNo = mails[0].mail_code;
			}

			showMails(mails, type);
		},
		error: function(xhr, status, error) {
			console.error('메일 목록 로드 실패:', status, error, xhr.responseText);
		}
	});
}


// 외부 CSS 동적 로드 함수
function loadExternalCSS(cssPath) {
	const existingLink = document.querySelector(`link[href="${cssPath}"]`);
	if (!existingLink) {
		const link = document.createElement('link');
		link.rel = 'stylesheet';
		link.type = 'text/css';
		link.href = cssPath;
		document.head.appendChild(link);
	}
}

function showMails(mails, type) {
	loadExternalCSS('../css/email.css');

	const mailTable = `
        <div id="email-page">
            <div class="email-controls">
                <button class="email-button" onclick="writeMailForm()">메일작성</button>
                <button class="email-button" onclick="deleteSelectedMails()">메일삭제</button>
            </div>

            <div class="email-layout">
                <div class="email-sidebar">
                    <button class="email-folder ${type === 'received' ? 'active' : ''}" onclick="loadMailList('received')">받은 메일</button>
                    <button class="email-folder ${type === 'sent' ? 'active' : ''}" onclick="loadMailList('sent')">보낸 메일</button>
                </div>

                <div class="email-content">
                    <table class="email-table">
                        <thead>
                            <tr class="email-header">
                                <th style="width:30px"></th>
                                <th>제목</th>
                                <th style="width:200px">${type === 'sent' ? '송신자' : '수신자'}</th>
                                <th style="width:150px">시간</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${mails.map(mail => getMailCode(mail, type)).join('')}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    `;

	document.getElementById("mail-list-container").innerHTML = mailTable;
}

function getMailCode(mail, type) {
	const contact = type === 'sent' ? mail.receiver : mail.writer;

	return `
        <tr class="email-row" onclick="handleMailClick('${mail.mail_code}', '${mail.server_code}')">
            <td onclick="event.stopPropagation()">
                <input type="checkbox"
                       class="mail-checkbox"
                       value="${mail.mail_code}"
                       data-server-code="${AllSession.serverGet()}">
            </td>
            <td class="email-subject">
                <a href="javascript:void(0)">${mail.mail_title}</a>
            </td>
            <td>${contact}</td>
            <td>${new Date(mail.post_date).toLocaleTimeString()}</td>
        </tr>
    `;
}

// 상세 메일 뷰 렌더링 함수
function renderMailDetail(mail) {
	// CSS 동적 로드
	loadExternalCSS('../css/e_view.css');

	// 메일 제목을 mail.title 또는 mail.mail_title에서 가져오며, 없으면 '제목 없음'으로 대체
	var title = mail.title || mail.mail_title || '제목 없음';

	const html = `
        <div id="email-view-page">
            <h2 class="email-view-title">${title}</h2>
            
            <table class="email-view-table">
                <tr>
                    <th class="email-view-header">보낸 사람</th>
                    <td>${mail.writer || '알 수 없음'}</td>
                </tr>
                <tr>
                    <th class="email-view-header">받은 시간</th>
                    <td>${mail.post_date ? new Date(mail.post_date).toLocaleString() : ''}</td>
                </tr>
                <tr>
                    <th class="email-view-header">내용</th>
                    <td class="email-view-content">${mail.content || ''}</td>
                </tr>
                ${mail.attachment ? `
                <tr>
                    <th class="email-view-header">첨부파일</th>
                    <td>
                        <img src="../mail_attachments/${mail.attachment}" class="email-attachment" alt="첨부파일">
                    </td>
                </tr>` : ''}
            </table>

            <div class="email-view-buttons">
                <button class="email-view-button" onclick="writeMailForm()">
                    답장하기
                </button>
                <button class="email-view-button" onclick="goBackToList()">
                    돌아가기
                </button>
            </div>
        </div>
    `;

	$("#mail-list-container").html(html);
}

// 삭제 함수

function deleteSelectedMails() {
    const checkboxes = document.querySelectorAll('.mail-checkbox:checked');
    const mailCodes = [];
    const serverCodes = [];

    checkboxes.forEach(checkbox => {
        mailCodes.push(checkbox.value);
        serverCodes.push(checkbox.dataset.serverCode);
    });

    if (mailCodes.length === 0) {
        alert("삭제할 메일을 선택하세요.");
        return;
    }

    // Show confirmation dialog
    if (!confirm("삭제하시겠습니까?")) {
        return;
    }

    // AJAX request to delete mails
    $.ajax({
        url: '../Mail/deleteMails.jsp',
        method: 'POST',
        traditional: true,
        data: {
            mailCodes: mailCodes,
            serverCodes: AllSession.serverGet()
        },
        success: function() {
            alert("삭제되었습니다.");
            $("#mail-list-container").empty();
            loadMailList(); // Refresh the mail list
        },
        error: function(xhr) {
            console.error('삭제 실패:', xhr.status);
        }
    });
}

// 메일 작성 폼 렌더링
function writeMailForm() {

	const serverCode = allSession.ServerGet();

	if (serverCode === null) {
		alert("서버가 선택되지 않았습니다.");
		return;
	}//0 값이면 작성X -> 나중에 NUll이면 으로 수정할 예정

	loadExternalCSS('../css/e_write.css');

	const html = `
        <div id="email-write-page">
            <h2 class="email-write-title">메일 작성</h2>
            <form id="mailForm" onsubmit="event.preventDefault(); sendMail()">
                <table class="email-write-table">
                    <tr>
                        <td class="email-write-header">받는 사람</td>
                    </tr>
                    <tr>
                        <td>
                            <input type="text" 
                                   class="email-write-input" 
                                   id="receiver"
                                   placeholder="받는 사람 ID 입력"
                                   name="recipient"
                                   required>
                        </td>
                    </tr>

                    <tr>
                        <td class="email-write-header">제목</td>
                    </tr>
                    <tr>
                        <td>
                            <input type="text" 
                                   class="email-write-input"
                                   id="mailTitle"
                                   placeholder="메일 제목 입력"
                                   name="subject"
                                   required>
                        </td>
                    </tr>

                    <tr>
                        <td class="email-write-header">내용</td>
                    </tr>
                    <tr>
                        <td>
                            <textarea class="email-write-textarea"
                                      id="mailContent"
                                      placeholder="메일 내용 입력"
                                      name="content"
                                      rows="10"
                                      required></textarea>
                        </td>
                    </tr>

                    <tr>
                        <td class="email-write-header">첨부파일</td>
                    </tr>
                    <tr>
                        <td>
                            <input type="file" 
                                   class="email-write-file-input"
                                   id="mailAttachment"
                                   name="attachment">
                        </td>
                    </tr>

                    <tr>
                        <td class="email-write-button-container">
                            <input type="submit" 
                                   class="email-write-button" 
                                   value="보내기">
                            <button type="button" 
                                    class="cancel-button" 
                                    onclick="goBackToList()">돌아가기</button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    `;

	$("#mail-list-container").html(html);
}

// 메일 전송 처리


function sendMail() {
	const receiver = $('#receiver').val();
	const mailTitle = $('#mailTitle').val();
	const content = $('#mailContent').val();

	const formData = new FormData();
	formData.append('receiver', receiver);
	formData.append('subject', mailTitle); // Ensure the key is 'subject'
	formData.append('content', content);
	formData.append('server_code', AllSession.serverGet());

	// Handle attachment
	const fileInput = document.getElementById('mailAttachment');
	if (fileInput.files.length > 0) {
		formData.append('attachment', fileInput.files[0]);
	}

	$.ajax({
		url: '../Mail/WriteAction.jsp',
		type: 'POST',
		data: formData,
		processData: false,
		contentType: false,
		success: function(response) {
			if (response.success) {
				alert('메일 전송 성공!');
				goBackToList();
			} else {
				alert('전송 실패: ' + response.error);
				console.error('Error:', response.error);
			}
		},
		error: function(xhr) {
			console.error('전송 오류:', xhr.status, xhr.responseText);
			alert('네트워크 오류입니다. 네트워크를 확인해주세요.');
		}
	});
}



// handleMailClick 함수 수정
function handleMailClick(mailCode, serverCode) {
	$.ajax({
		url: '../Mail/ReadMailController.jsp',
		type: 'GET',
		data: {
			mail_code: mailCode,
			server_code: serverCode
		},
		dataType: 'json', // JSON 응답 강제 지정
		success: function(mailData) {
			renderMailDetail(mailData);
		},
		error: function(xhr) {
			console.error("상세 정보 로드 실패:", xhr.status);
		}
	});
}

// 브라우저 뒤로 가기 처리
window.goBackToList = function() {
	$("#mail-list-container").empty();
	loadMailList();
};

function getNextMails() {
	var params = { maxNo: minMailNo };
	$.ajax({
		url: '../Mail/ReciveMailController.jsp',
		type: 'GET',
		data: params,
		dataType: 'json',
		success: function(mails) {
			if (mails.length > 0) {
				minMailNo = mails[mails.length - 1].mail_code;
			}

			showMails(mails);
		},
		error: function(xhr, status, error) {
			console.error('추가 메일 로드 실패:', status, error, xhr.responseText);
		}
	});
}





