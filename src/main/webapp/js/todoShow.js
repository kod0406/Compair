var Todo = {
	init: function() {
		Todo.show();
	},
	
	show: function(){
		var params = { "recentServerCode": AllSession.serverGet()};
		AJAX.call("../JSP/todoShow.jsp", params, function(data) {
		    var feeds = JSON.parse(data.trim());

		    if (feeds.length != 0) {
		        Todo.showTodo(feeds);
		    } else {
		        Todo.showTodoNothing();
		    }
		});
	},
	
	showTodo: function(feeds) {
	    var calStr = "";
	    for (var i = 0; i < feeds.length; i++) {
	        // 예시: Board 객체의 getFeedCode 함수로 항목 생성 (필요에 따라 수정)
	        calStr += Todo.getFeedCode(feeds[i]);
	    }
	    $("#todo-show-section").append(calStr);
	},
	
	showTodoNothing: function() {
		var calStr = "할 일이 없습니다.";
	    $("#list").append(calStr);
	},
	
	getFeedCode: function(feeds) {
	    var clickCode = feeds.TODO_CODE;
	    var str = "<div style='display: flex; align-items: center; padding: 10px; border-bottom: 1px solid #ddd;' onclick='Board.handleRowClick(\"" + clickCode + "\")'>";
	    
	    str += "<div style='width: 100px; text-align: center;'>" + feeds.TODO_CODE + "</div>";
		str += "<div style='width: 150px; text-align: center;'>" + feeds.TODO_CONTENT + "</div>";
		str += "<div style='width: 150px; text-align: center;'>" + feeds.TODO_WRITER + "</div>";
		str += "<div style='flex: 2; text-align: center;'>" + feeds.POST_DATE + "</div>";
			    
	    str += "</div>";

	    return str;
	},
	
};
