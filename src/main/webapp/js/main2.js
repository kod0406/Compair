var Board = {
	init: function() {
	    $(document).ready(function() {
		boardShow();
        });
    },
	
	boardShow: function() {
		$("#list").show();
	    var params = { "recentServerCode": AllSession.serverGet(), good:AllSession.maxGet()};
	    AJAX.call("../JSP/feedGetGroup.jsp", params, function(data) {
	        var feeds = JSON.parse(data.trim());
	        if (feeds.length > 0) {
	            AllSession.minSession(feeds[feeds.length - 1].BOARD_CODE);
	        }
	        Board.show(feeds);
	    });
	},
	
	show: function(feeds) {
	    var str = "";
	    
	    // 테이블 전체 구조 포함
	    str += "<div id='board-page'>";
	    str += "<table class='board-table'>";
	    str += "<thead>";
	    str += "  <tr><th colspan='4'><h2>게시판</h2></th></tr>";
	    str += "  <tr class='board-header'>";
	    str += "    <th class='board-num'>번호</th>";
	    str += "    <th class='board-title'>제목</th>";
	    str += "    <th>작성자</th>";
	    str += "    <th>작성날짜</th>";
	    str += "  </tr>";
	    str += "</thead>";
	    str += "<tbody>";

	    // 게시글 데이터 추가
	    for (var i = 0; i < feeds.length; i++) {
	        str += Board.getFeedCode(feeds[i]);
	    }

	    str += "</tbody>";
	    str += "</table>";
	    str += "</div>";

	    // 결과를 원하는 위치에 삽입
	    $("#list").html(str);
	},

    getFeedCode: function(feed) {
        var clickCode = feed.BOARD_CODE;
        var str = "<div style='display: flex; align-items: center; padding: 10px; border-bottom: 1px solid #ddd;' onclick='Board.handleRowClick(\"" + clickCode + "\")'>";
        
        str += "<div style='width: 100px; text-align: center;'>" + feed.BOARD_CODE + "</div>";
        str += "<div style='flex: 2; text-align: center;'>" + feed.TITLE + "</div>";
        str += "<div style='width: 150px; text-align: center;'>" + feed.AUTHOR + "</div>";
        str += "<div style='width: 150px; text-align: center;'>" + feed.POSTDATE + "</div>";
        str += "</div>";
		
		

        return str;
    },

    handleRowClick: function(clickCode) {
        window.location.href = "b_view.html?boardCode=" + clickCode;
    },
	
    getNext: function() {
        var params = { maxNo: AllSession.minGet(), recentServerCode: AllSession.serverGet()};
		console.log(AllSession.minGet());
        AJAX.call("../JSP/feedGetGroup.jsp", params, function(data) {
            var feeds = JSON.parse(data.trim());
            if (feeds.length > 0) {
				AllSession.minSession(feeds[feeds.length - 1].BOARD_CODE);
            }
			console.log(AllSession.minGet());
			AllSession.maxSession(AllSession.maxGet() + 3);
			console.log("허허허" + AllSession.maxGet());
            Board.show(feeds);
        });
    }
};

