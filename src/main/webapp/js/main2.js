$(document).ready(function() {
       	Page.init(start);
       });
       var suid;
       var minNo = -1;
       var recentNo = -1;
       console.log(minNo);
       //board
       	function start(uid) {
       		suid = uid;
       	    AJAX.call("../JSP/feedGetGroup.jsp", null, function(data) {
       	        var feeds = JSON.parse(data.trim());
       	        console.log(feeds);
       	        if (feeds.length > 0) {
       	    	    minNo = feeds[feeds.length - 1].BOARD_CODE;
       	    	    recentNo = feeds[feeds.length - 1].BOARD_CODE;
       	        }
       	        console.log("minNo는?" + minNo);
       	        show(feeds);
       	    });
       	}
       	function show(feeds) {
       	    var str = "";
       	    for (var i=0; i<feeds.length; i++) {
       	        str += getFeedCode(feeds[i]);
       	    }
			str += "<button onclick=\"window.location.href='../html/b_write.html'\">글 작성</button>";

       	    $("#list").append(str);
       	}
       	
       	function getFeedCode(feed) {
       		var clickCode = feed.BOARD_CODE;
       		var str = "<div style='display: flex; align-items: center; padding: 10px; border-bottom: 1px solid #ddd;' onclick='handleRowClick(\"" + clickCode + "\")'>";
       	    
       		str += "<div style='width: 100px; text-align: center;'>" + feed.BOARD_CODE + "</div>";
       	    str += "<div style='flex: 2; text-align: center;'>" + feed.TITLE + "</div>";
       	    str += "<div style='width: 150px; text-align: center;'>" + feed.AUTHOR + "</div>";
       	    str += "<div style='width: 150px; text-align: center;'>" + feed.POSTDATE + "</div>";
       	    str += "</div>";
			

       	    return str;
       		
       		//var name = (feed.user == null) ? feed.id : feed.user.name;
			/*
       	    var str = "<div class='feed'>";
       	    str += "<div class='author'>";
       	    str += "<div class='photo'></div>";
       	    str += "<div class='name'>" + feed.AUTHOR + "</div>";
       	    str += "</div>";

       	    if (feed.ATTACHMENT) {
       	        var imgurl = "../images/"+ feed.ATTACHMENT;
       	        console.log(imgurl);
       	        console.log(feed.ATTACHMENT);
       	        str += "<div><img src=\"" + imgurl + "\" alt=\"이미지\" width=\"280px\" /></div>";

       	    }

       	    // 게시물의 제목과 내용
       	    str += "<div class='title'>" + feed.TITLE + "</div>";
       	    str += "<div class='text'>" + feed.CONTENT + "</div>";
       	    str += "<div class='postdate'>" + feed.POSTDATE + "</div>";  // 게시물 작성일 출력
       	    str += "</div>";

       	    return str;
       		*/
       	}
       	
       	function handleRowClick(clickCode) {
       	    alert("게시글 코드: " + clickCode + " 클릭됨!");
       	    window.location.href = "b_view.html?boardCode=" + clickCode;
       	}
       	
       	function getNext() {
       	    var params = {maxNo: minNo};
       	    console.log(minNo);
       	    AJAX.call("../JSP/feedGetGroup.jsp", params, function(data) {
       	        var feeds = JSON.parse(data.trim());
       	        if (feeds.length > 0) {
       	    	    minNo = feeds[feeds.length - 1].BOARD_CODE;
       	        }
       	        show(feeds);
       	    });
       	}
       	//board 끝 