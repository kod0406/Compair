var Calendar = {
    // 초기화
    init: function() {
        $("#todo-title").show();
        $("#calendarList").show();
        $("#todo-list-section").show();
		$(".todo-input-area").show();
    },

    // 표시 함수
    showCalendarTODO: function(feeds) {
        var calStr = "";
        for (var i = 0; i < feeds.length; i++) {
            calStr += Calendar.getFeedCode(feeds[i]);
        }
        $("#todo-list-section").append(calStr);
    },

    showCalendarNothing: function() {
        var calStr = "아직 아무 데이터도 존재하지 않습니다.";
        $("#todo-list-section").append(calStr);
    },

    // 할 일 관리 함수
    todoPlus: function() {
        var todoInput = document.getElementById("todo-input");
        var tagInput = document.getElementById("todo-tag-input");

        if (todoInput && tagInput) {
            var todoValue = todoInput.value.trim();
            var tagValues = tagify.value; // Tagify 값 저장 변수
            var tags = tagValues.map((item) => item.value).join(","); // 태그를 문자열로 변환

            if (todoValue === "") {
                alert("할 일을 입력해주세요.");
                return;
            } else if (AllSession.serverGet() == 'null') {
                alert("서버를 선택해주세요");
                return;
            } else if (AllSession.dateGet() == null) {
                alert("날짜를 선택해주세요");
                return;
            }

            var todoParams = {
                "server_code": AllSession.serverGet(),
                "todo_title": todoValue,
                "tags": tags,
                "post_date": AllSession.dateGet()
            };

            AJAX.call("../Todo/MakeTodoController.jsp", todoParams, function(data) {
                var response = JSON.parse(data.trim());
                if (response.result === "OK") {
                    alert(tags);
                    alert(AllSession.dateGet() + "에 할 일이 추가되었습니다.");
                    todoInput.value = "";
                    tagify.removeAllTags(); // 태그 입력 필드 초기화
					Calendar.reloadTodoList();
                } else {
                    alert("네트워크 에러가 발생되었습니다: " + response.error);
                }
            });
        } else {
            alert("todo-input 또는 todo-tag-input 요소를 찾을 수 없습니다.");
        }
    },

    getFeedCode: function(date) {
        var params = {
            "server_code": AllSession.serverGet(),
            "post_date": date
        };

        AJAX.call("../Todo/GetTodosByDateController.jsp", params, function(data) {
            var feeds = JSON.parse(data.trim());

            if (feeds.length != 0) {
                var calStr = "";
                for (var i = 0; i < feeds.length; i++) {
                    var feed = feeds[i];
                    var clickCode = feed.todo_code;
                    var content = feed.title;
                    var writer = feed.writer;
                    var isDone = feed.check ? "done" : "";

                    var str = `<div class="todo-item ${isDone}" id="todo-${clickCode}">`;
                    str += `<span class="todo-writer" id="todo-writer-${clickCode}">[${writer}]</span> `;
                    str += `<span class="todo-text" id="todo-text-${clickCode}" onclick="toggleExpand(${clickCode})">${content}</span>`;

                    if (feed.tag && feed.tag.length > 0) {
                        let tagsHTML = feed.tag
                            .map(tag => `<span class="tag">${tag}</span>`)
                            .join(" ");
                        str += `<div class="todo-tags" id="todo-tags-${clickCode}">${tagsHTML}</div>`;
                    }

                    str += `<div>`;
                    str += `<button onclick="Calendar.toggleTodo(${clickCode})"><i class="fa-solid ${isDone ? 'fa-rotate-left' : 'fa-check'}"></i></button>`;
                    str += `<button onclick="Calendar.editTodo(${clickCode})"><i class="fa-solid fa-pen"></i></button>`;
                    str += `<button onclick="Calendar.deleteTodo(${clickCode})"><i class="fa-solid fa-trash-can"></i></button>`;
                    str += `</div>`;
                    str += `</div>`;

                    calStr += str;
                }
                $("#todo-list-section").append(calStr);
            } else {
                Calendar.showCalendarNothing();
            }
        });
    },

    // 할 일 상태 함수
    toggleTodo: function(todoCode) {
        var todoItem = document.getElementById(`todo-${todoCode}`);
        var isDone = todoItem.classList.contains("done");

        // UI를 낙관적으로 업데이트
        if (isDone) {
            todoItem.classList.remove("done");
        } else {
            todoItem.classList.add("done");
        }

        var params = {
            "todo_code": todoCode,
            "server_code": AllSession.serverGet()
        };

        AJAX.call("../Todo/CheckTodoController.jsp", params, function(data) {
            var response = JSON.parse(data.trim());
            if (response.success) {
                // 새로운 상태에 따라 UI 업데이트
                if (response.newState) {
                    todoItem.classList.add("done");
                } else {
                    todoItem.classList.remove("done");
                }
                // 할 일 목록을 지우고 다시 로드
                Calendar.reloadTodoList();
            } else {
                // AJAX 호출이 실패하면 변경 사항을 되돌림
                if (isDone) {
                    todoItem.classList.add("done");
                } else {
                    todoItem.classList.remove("done");
                }
                alert("할 일 항목을 업데이트하지 못했습니다.");
            }
        });
    },

    reloadTodoList: function() {
        var todoListSection = document.getElementById("todo-list-section");
        todoListSection.innerHTML = ""; // 할 일 목록 컨테이너를 지움
        Calendar.getFeedCode(AllSession.dateGet()); // 할 일 항목을 다시 로드
    },

    // 할 일 항목 편집 함수
    editTodo: function(todoCode) {
        var todoItem = document.getElementById(`todo-${todoCode}`);
        var todoText = document.getElementById(`todo-text-${todoCode}`);
        var todoTags = document.getElementById(`todo-tags-${todoCode}`);
        var writer = document.getElementById(`todo-writer-${todoCode}`).innerText;

        // 원래 상태 저장
        var originalText = todoText.innerText;
        var originalTags = Array.from(todoTags.children).map(tag => tag.innerText).join(", ");

        // 편집을 위한 입력 요소 생성
        var inputElement = document.createElement("input");
        inputElement.type = "text";
        inputElement.className = "input_style";
        inputElement.id = `edit-text-${todoCode}`;
        inputElement.value = originalText;

        // 태그를 위한 Tagify 입력 생성
        var tagInputElement = document.createElement("input");
        tagInputElement.type = "text";
        tagInputElement.className = "input_style";
        tagInputElement.id = `edit-tags-${todoCode}`;
        tagInputElement.value = originalTags;

        // 텍스트와 태그를 입력 요소로 교체
        todoText.replaceWith(inputElement);
        todoTags.replaceWith(tagInputElement);

        // Tagify 초기화
        var tagify = new Tagify(tagInputElement, {
            delimiters: ", ",
            dropdown: {
                enabled: 1,
                position: "text"
            }
        });

        // 저장 버튼 추가
        var saveButton = document.createElement("button");
        saveButton.innerText = "Save";
        saveButton.onclick = function() {
            Calendar.updateTodo(todoCode, inputElement.value, tagify.value.map(tag => tag.value).join(", "), writer);
        };
        todoItem.appendChild(saveButton);

        // 취소 버튼 추가
        var cancelButton = document.createElement("button");
        cancelButton.innerText = "Cancel";
        cancelButton.onclick = function() {
            // 원래 상태로 되돌림
            inputElement.replaceWith(todoText);
            tagInputElement.replaceWith(todoTags);
            todoText.innerText = originalText;
            todoTags.innerHTML = originalTags.split(", ").map(tag => `<span class="tag">${tag}</span>`).join(" ");
            saveButton.remove();
            cancelButton.remove();
        };
        todoItem.appendChild(cancelButton);
    },

    updateTodo: function(todoCode, newContent, newTags, writer) {
        if (!newContent.trim()) {
            alert("할 일을 입력하세요!");
            return;
        }

        var params = {
            "todo_code": todoCode,
            "server_code": AllSession.serverGet(),
            "new_title": newContent,
            "new_tag": newTags,
            "uid": writer
        };

        AJAX.call("../Todo/UpdateTodoController.jsp", params, function(data) {
            var response = JSON.parse(data.trim());
            if (response.success) {
                alert("할 일이 수정되었습니다.");
                Calendar.reloadTodoList();
            } else {
                alert("수정 실패: " + response.error);
                Calendar.reloadTodoList(); // 오류 발생 시 원래 상태로 되돌림
            }
        });
    },

    // 할 일 항목 삭제 함수
    deleteTodo: function(todoCode) {
        if (confirm("삭제하시겠습니까?")) {
            var params = {
                "todo_code": todoCode,
                "server_code": AllSession.serverGet(),
                "uid": AllSession.uidGet()
            };

            AJAX.call("../Todo/DeleteTodoController.jsp", params, function(data) {
                var response = JSON.parse(data.trim());
                if (response.success) {
                    alert("할 일이 삭제되었습니다.");
                    Calendar.reloadTodoList();
                } else {
                    alert("삭제 실패: " + response.error);
                    Calendar.reloadTodoList(); // 오류 발생 시 원래 상태로 되돌림
                }
            });
        }
    }
	/*
		// 완료 상태 토글 함수
			function toggleTodo(id) {
			  todoList = todoList.map((todo) =>
				todo.id === id ? { ...todo, isDone: !todo.isDone } : todo
			  );
			  renderTodoList();
			}

			// 할 일 삭제 함수
			function deleteTodo(id) {
			  todoList = todoList.filter((todo) => todo.id !== id);
			  renderTodoList();
			}

			function editTodo(id) {
			  const todoItem = todoList.find((todo) => todo.id === id);
			  if (!todoItem) return;

			  // 기존 <span> 요소를 찾아서 <input>으로 변환
			  const todoTextElement = document.getElementById(`todo-text-${id}`);
			  if (!todoTextElement) return; // 요소가 없으면 종료

			  // 새로운 <input> 요소 생성
			  const inputElement = document.createElement("input");
			  inputElement.type = "text";
			  inputElement.className = "input_style";
			  inputElement.id = `edit-${id}`;
			  inputElement.value = todoItem.content;

			  // 입력한 내용을 저장하는 이벤트 추가
			  inputElement.addEventListener("keypress", function (event) {
				if (event.key === "Enter") {
				  updateTodo(id, inputElement.value);
				}
			  });

			  // 기존 <span>을 <input>으로 교체
			  todoTextElement.replaceWith(inputElement);

			  // 입력창에 자동 포커스
			  inputElement.focus();
			}

			function updateTodo(id, newContent) {
			  if (!newContent.trim()) {
				alert("할 일을 입력하세요!");
				return;
			  }

			  // todoList에서 해당 id를 가진 항목 찾고 업데이트
			  todoList = todoList.map((todo) =>
				todo.id === id ? { ...todo, content: newContent } : todo
			  );

			  // 화면 다시 렌더링
			  renderTodoList();
			}

			// 긴 내용 펼치기 함수 + 이쪽 아래부턴 수정 안해도 됨
			function toggleExpand(id) {
			  const todoText = document.getElementById(`todo-text-${id}`);
			  if (!todoText) return;
			  todoText.classList.toggle("expanded");
			}

			
			function initializeTodo() {
			  const addTodoButton = document.getElementById("add-todo-button");
			  const todoInput = document.getElementById("todo-input");

			  addTodoButton.addEventListener("click", function () {
				addTodo(todoInput.value);
				todoInput.value = ""; // 입력 필드 초기화
			  });

			  renderTodoList(); // 초기 렌더링
			}
		
		 */

};