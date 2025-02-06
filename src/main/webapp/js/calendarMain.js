//calendar 시작
        	var currentDate = new Date();
			function calendar(){
				minNo = recentNo;
				console.log("minNo는?" + minNo);
			    $("#list").empty();
			    $("#calendarList").empty();
			    // list 영역 비우기
			    console.log("캘린더 버튼 클릭됨, 리스트 영역 비워짐.");
			    alert("캘린더");
			    showCalendar();
			    let testtDate = new Date();
			    generateCalendar(new Date());
			    

			 	document.getElementById("prevMonth").addEventListener("click", function () {
			        changeMonth(-1);
			    });
			 	
			    document.getElementById("nextMonth").addEventListener("click", function () {
			        changeMonth(1);
			    });
			    
			    console.log(testtDate);
			}
		    
		    function changeMonth(step) {
		        currentDate.setMonth(currentDate.getMonth() + step);
		        generateCalendar(currentDate);
		    }
		    
		    function handleDateClick(date) {
		        alert(`선택한 날짜: ${date}`);
		        selectDate(date);
		    }
		    
		    function selectDate(date){
		    	var dateParams = {"POST_DATE" : date};
        	    AJAX.call("../JSP/calendarGet.jsp", dateParams, function(data) {
        	        var feeds = JSON.parse(data.trim());
        	        console.log("feed는??????" + feeds);
        	        if (feeds.length > 0) {
        	    	    minNo = feeds[feeds.length - 1].BOARD_CODE;
        	    	    recentNo = feeds[feeds.length - 1].BOARD_CODE;
        	        }
        	        console.log("minNo는?" + minNo);
        	        if(feeds.length != 0){
        	        	showCalendarTODO(feeds);
        	        }
        	        else showCalendarNothing();
        	    });
		    }
		    
        	function showCalendarTODO(feeds) {
        	    var calStr = "";
        	    for (var i=0; i<feeds.length; i++) {
        	        calStr += getFeedCode(feeds[i]);
        	    }
        	    $("#list").empty();
        	    $("#list").append(calStr);
        	}
        	
        	function showCalendarNothing(){
        		$("#list").empty();
        		var calStr = "아직 아무 데이터도 존재하지 않습니다.";
        		$("#list").append(calStr);
        	}
        	
        	function showCalendar() {
		    	var str = "<div id='calendar-page'>";
		    	str += "<div class='calendar-header'>";
		    	str += "<button id='prevMonth' class='calendar-nav'>&lt;</button>";
		    	str += "<span id='calendarMonthYear'></span>";
		    	str += "<button id='nextMonth' class='calendar-nav'>&gt;</button>"
		    	str += "</div>";
		    	str += "<table class='calendar-table'>";
		    	str += "<thead><tr><th>일</th><th>월</th><th>화</th><th>수</th><th>목</th><th>금</th><th>토</th></tr></thead>";
		    	str += "<tbody id='calendarBody'></tbody>";
		    	str += "</table></div>";
		    	$("#calendarList").append(str);
		    }
			var fullDate;
        	function generateCalendar(date) {
        	    const monthYear = document.getElementById("calendarMonthYear");
        	    const calendarBody = document.getElementById("calendarBody");

        	    monthYear.textContent = `${date.getFullYear()}년 ${date.getMonth() + 1}월`;
        	    calendarBody.innerHTML = "";

        	    const firstDay = new Date(date.getFullYear(), date.getMonth(), 1).getDay();
        	    const lastDate = new Date(date.getFullYear(), date.getMonth() + 1, 0).getDate();

        	    let row = "<tr>";

        	    for (let i = 0; i < firstDay; i++) {
        	        row += "<td></td>";
        	    }

        	    for (let day = 1; day <= lastDate; day++) {
        	        if ((firstDay + day - 1) % 7 === 0 && day !== 1) {
        	            row += "</tr><tr>";
        	        }

        	        fullDate = `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, "0")}-${day.toString().padStart(2, "0")}`;
        	        console.log("fullDate " + fullDate);
        	        row += `<td class="calendar-day" data-date="${fullDate}" onclick="handleDateClick('${fullDate}')">${day}</td>`;
        	    }

        	    row += "</tr>";
        	    console.log(row);
        	    calendarBody.innerHTML = row;
        	}
        	
        	
        	
        	//calendar 끝