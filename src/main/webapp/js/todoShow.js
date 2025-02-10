var Todo = {
	init: function() {
		Page.init(Todo.show);
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
	    $("#list").append(calStr);
	},
	
	showTodoNothing: function() {
	    $("#list").append(calStr);
	},
	
	getFeedCode: function(feed) {
	    var clickCode = feed.TODO_CODE;
	    var str = "<div style='display: flex; align-items: center; padding: 10px; border-bottom: 1px solid #ddd;' onclick='Board.handleRowClick(\"" + clickCode + "\")'>";
	    
	    str += "<div style='width: 100px; text-align: center;'>" + feed.TODO_CODE + "</div>";
		str += "<div style='width: 150px; text-align: center;'>" + feed.TODO_CONTENT + "</div>";
		str += "<div style='width: 150px; text-align: center;'>" + feed.TODO_WRITER + "</div>";
		str += "<div style='flex: 2; text-align: center;'>" + feed.POST_DATE + "</div>";
			    
	    str += "</div>";

	    return str;
	},
	
};
