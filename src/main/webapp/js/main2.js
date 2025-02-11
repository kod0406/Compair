var Board = {
	init: function() {
	    $(document).ready(function() {
		Board.boardShow();
        });
    },
	
	boardShow: function() {
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
	    $("#list").append(str);
	},

	getFeedCode: function(feed) {
	    var clickCode = feed.BOARD_CODE;
	    var str = "<tr class='board-body' onclick='Board.handleRowClick(\"" + clickCode + "\")'>";
	    
	    str += "<td class='board-num'>" + feed.BOARD_CODE + "</td>";
	    str += "<td class='board-title'>" + feed.TITLE + "</a></td>";
	    str += "<td>" + feed.AUTHOR + "</td>";
	    str += "<td>" + feed.POSTDATE + "</td>";
	    
	    str += "</tr>";

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

