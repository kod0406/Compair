
var Todo = {
    init: function() {
        $("#todo-title").show();
        $("#calendarList").hide();
        $("#View-Todo").show(); // Show the View-Todo section
        $(".todo-input-area").hide(); // Hide the input area
        Todo.loadTodos();
    },

    loadTodos: function() {
        var params = {
            "recentServerCode": AllSession.serverGet()
        };

        AJAX.call("../Todo/ShowTodosController.jsp", params, function(data) {
            var todos = JSON.parse(data.trim());
            Todo.displayTodos(todos);
        }, function(xhr, status, error) {
            // Custom error handling for Todo.js
            if (xhr.status == 400) {
                alert("서버코드를 선택하세요");
            } else {
                alert("목록 조회 실패: " + error);
            }
        });
    },

    displayTodos: function(todos) {
        var todoListSection = document.getElementById("View-Todo"); // Target the View-Todo section
        todoListSection.innerHTML = ""; // Clear the To-Do list container

        if (todos.length === 0) {
            todoListSection.innerHTML = "<p>아직 아무 데이터도 존재하지 않습니다.</p>";
            return;
        }

        var calStr = "";
        for (var i = 0; i < todos.length; i++) {
            var todo = todos[i];
            var clickCode = todo.todo_code;
            var content = todo.title;
            var writer = todo.writer;
            var postDate = new Date(todo.post_date).toISOString().split('T')[0]; // Format date to YYYY-MM-DD
            var isDone = todo.check ? "done" : "";

            var str = `<div class="todo-item ${isDone}" id="todo-${clickCode}">`;
            str += `<span class="todo-writer" id="todo-writer-${clickCode}">[${writer}]</span> `;
            str += `<span class="todo-text" id="todo-text-${clickCode}">${content}</span>`;
            str += `<span class="todo-date" id="todo-date-${clickCode}">${postDate}</span>`;

            if (todo.tags && todo.tags.length > 0) {
                let tagsHTML = todo.tags
                    .map(tag => `<span class="tag">${tag}</span>`)
                    .join(" ");
                str += `<div class="todo-tags" id="todo-tags-${clickCode}">${tagsHTML}</div>`;
            }

            str += `</div>`;
            calStr += str;
        }
        todoListSection.innerHTML = calStr;
    }
};
