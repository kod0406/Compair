var Calendar = {
    init: function() {
		$("#todo-title").show();
		$("#calendarList").show();
		$("#todo-list-section").show();
    },

	// 데이터가 없는 경우 메시지 출력
	//calendarShow.js에서 호출 됨
	todoShow : function(){
		$("#todo-list-section").show();
		var dateParams = {"POST_DATE": AllSession.dateGet(), "ServerCode": AllSession.serverGet()};
		AJAX.call("../JSP/todoGet.jsp", dateParams, function(data) {
		    var feeds = JSON.parse(data.trim());
			
			if (feeds.length != 0) {
		        Calendar.showCalendarTODO(feeds);
		    } else {
		        Calendar.showCalendarNothing();
		    }
		});
	},
	
	showCalendarTODO: function(feeds) {
	    var calStr = "";
	    for (var i = 0; i < feeds.length; i++) {
	        // 예시: Board 객체의 getFeedCode 함수로 항목 생성 (필요에 따라 수정)
	        calStr += Calendar.getFeedCode(feeds[i]);
	    }
	    $("#todo-list-section").append(calStr);
	},
	
	showCalendarNothing: function() {
	    var calStr = "아직 아무 데이터도 존재하지 않습니다.";
	    $("#todo-list-section").append(calStr);
	},
	
	// todoPlus: To-Do 입력 필드의 값을 읽어와 후속 작업 수행
	todoPlus: function(){
	    var todoInput = document.getElementById("todo-input");
	    if (todoInput) {
	        var todoValue = todoInput.value.trim();

	        if (todoValue === "") {
	            alert("할 일을 입력해주세요.");
	            return;
	        }
			else if(AllSession.serverGet() == 'null'){
				alert("서버를 선택해주세요");
				return;
			}
	        var todoParams = {"ServerCode":AllSession.serverGet(), "todoInput": todoValue, "thisPageDate": AllSession.dateGet()};
	        
			AJAX.call("../JSP/todoInsert.jsp", todoParams, function(data) {
	            if (data.trim() === "OK") {
	                alert(AllSession.dateGet() + "에 할 일이 추가되었습니다.");
	                todoInput.value = "";
	            } else {
	                alert("네트워크 에러가 발생되었습니다.");
	            }
	        });
	    } else {
	        alert("todo-input 요소를 찾을 수 없습니다.");
	    }
	},
	
	getFeedCode: function(feed) {
		//수정 부분
	    var clickCode = feed.TODO_CODE;
	    var str = "<div style='display: flex; align-items: center; padding: 10px; border-bottom: 1px solid #ddd;' onclick='Board.handleRowClick(\"" + clickCode + "\")'>";
	    str += "<div style='width: 100px; text-align: center;'>" + feed.TODO_CODE + "</div>";
		str += "<div style='width: 150px; text-align: center;'>" + feed.TODO_CONTENT + "</div>";
	    str += "<div style='width: 150px; text-align: center;'>" + feed.TODO_WRITER + "</div>";
	    
	    str += "</div>";

	    return str;
	},
	

};
