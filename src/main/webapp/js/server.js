var Server = {
	start: function() {
		Server.checkServer();
		 	
	},
	checkServer: function() {
	    var serverParams = {"serverList": AllSession.uidGet()};
	    AJAX.call("../JSP/checkServer.jsp", serverParams, function(data) {
	        var rvalue = JSON.parse(data.trim());
	        var keys = Object.keys(rvalue);
	        
	        var values = keys.map(function(key) {
	            return rvalue[key];
	        });
	        
	        Server.serverListShow(values);
	        console.log("data는 " + data);
	    });
	},
	//서버 작성 코드
	serverListShow: function(values) {
	    var serverListShowStr = "<div class = 'server-list'>";
	    console.log(values[0]);
	    if (values[0] == "0") {
	        serverListShowStr += "<div class = 'server-item' onclick='Server.serverAdd()'>";
	        serverListShowStr += 0;
	        serverListShowStr += "</div>";
	    }

	    if (values.length > 0) {
	        for (let i = 1; i < values.length; i++) {
	            var sc = values[i];
	            serverListShowStr += "<div class = 'server-item' onclick='Server.serverClick(\"" + sc + "\")'>";
	            serverListShowStr += values[i];
	            serverListShowStr += "</div>";
	            
	            console.log("rvalue 는???? " + values[i]);
	        }
	        serverListShowStr += "</div>";
	    }

	    $("#serverList").append(serverListShowStr);
	},
	//0번 서버 선택시 이동
	serverAdd: function() {
	    window.location.href = "ServerAdd.html";
	},

	//서버가 바뀔때만 그 전에 화면 비움
	serverClick: function(sc) {
		AllSession.minSession(-1);
		AllSession.serverSession(sc);
		$("#list").empty();
		$("#todo-list-section").empty();
		$("#todo-show-section").empty();
		var nowScreen = AllSession.pageGet();
		alert(nowScreen);	
			if(nowScreen == 'board'){
				Board.boardShow();
			}
			else if(nowScreen == 'calendar'){
				Calendar.init();
			}
			else if(nowScreen == 'todo'){
				Todo.init();
			}
			else if(nowScreen == 'email'){
				loadMailList();
			}
	 }
}