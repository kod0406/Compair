$(document).ready(function() {
	       	Page.init(start);
	       });
		   
		   var suid;
	       var minNo = -1;
	       var recentNo = -1;
	       console.log(minNo);
	       let tokens;
		   
		   
	       function checkServer(uid){
			serverParams = {"serverList" : uid};
			AJAX.call("../JSP/checkServer.jsp", serverParams, function(data) {
				var rvalue = JSON.parse(data.trim());
				console.log(rvalue);
				var keys = Object.keys(rvalue);
				
				var values = keys.map(function(key) {
				    return rvalue[key];
				});
				console.log(values);
				
				serverListShow(values);
				console.log("data는" + data);
			});
		   }
		   
		   
		   function serverListShow(values){
			var serverListShowStr = "<div>";
			for(let i=0; i<values.length; i++){
				serverListShowStr += values[i];
				serverListShowStr += " ";
				console.log("rvalue 는????" + values[i]);
			}
				serverListShowStr += "</div>";
				$("#serverList").append(serverListShowStr);
		   }
		   
		   
		   //board
	       	function start(uid) {
				checkServer(uid);
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
	       console.log(suid);
       	function show(feeds) {
       	    var str = "";
       	    for (var i=0; i<feeds.length; i++) {
       	        str += getFeedCode(feeds[i]);
       	    }
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