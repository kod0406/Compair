var CalendarShow = {
	currentDate: new Date(),
	
	calendar: function() {
	    CalendarShow.showCalendar();
	    CalendarShow.generateCalendar(CalendarShow.currentDate);
		CalendarShow.showTodo();
		
		
	    document.getElementById("prevMonth").addEventListener("click", function () {
	        Calendar.changeMonth(-1);
	    });
	    document.getElementById("nextMonth").addEventListener("click", function () {
	        Calendar.changeMonth(1);
	    });
		$("#todo-title").hide();
		$("#calendarList").hide();
		$("#todo-list-section").hide();
	},
	
	showCalendar: function() {
	    var str = "<div id='calendar-page'>";
	    str += "<div class='calendar-header'>";
	    str += "<button id='prevMonth' class='calendar-nav'>&lt;</button>";
	    str += "<span id='calendarMonthYear'></span>";
	    str += "<button id='nextMonth' class='calendar-nav'>&gt;</button>";
	    str += "</div>";
	    str += "<table class='calendar-table'>";
	    str += "<thead><tr><th>일</th><th>월</th><th>화</th><th>수</th><th>목</th><th>금</th><th>토</th></tr></thead>";
	    str += "<tbody id='calendarBody'></tbody>";
	    str += "</table>";
	    $("#calendarList").html(str);
	},
	
	generateCalendar: function(date) {
	    var monthYear = document.getElementById("calendarMonthYear");
	    var calendarBody = document.getElementById("calendarBody");

	    monthYear.textContent = date.getFullYear() + "년 " + (date.getMonth() + 1) + "월";
	    calendarBody.innerHTML = "";

	    var firstDay = new Date(date.getFullYear(), date.getMonth(), 1).getDay();
	    var lastDate = new Date(date.getFullYear(), date.getMonth() + 1, 0).getDate();

	    var row = "<tr>";

	    // 첫 주의 빈 칸 채우기
	    for (var i = 0; i < firstDay; i++) {
	        row += "<td></td>";
	    }

	    // 날짜 채우기
	    for (var day = 1; day <= lastDate; day++) {
	        if ((firstDay + day - 1) % 7 === 0 && day !== 1) {
	            row += "</tr><tr>";
	        }

	        var fullDate = date.getFullYear() + "-" + 
	                       String(date.getMonth() + 1).padStart(2, "0") + "-" + 
	                       String(day).padStart(2, "0");
	        console.log("fullDate " + fullDate);

	        // 날짜 클릭 시 Calendar.handleDateClick 호출
	        row += "<td class='calendar-day' data-date='" + fullDate + 
	               "' onclick=\"CalendarShow.handleDateClick('" + fullDate + "')\">" + day + "</td>";
	    }

	    row += "</tr>";
	    calendarBody.innerHTML = row;
	},

	// 날짜 클릭 시 호출되는 함수
	handleDateClick: function(date) {
		$("#todo-list-section").empty();
		var date = AllSession.dateSession(date);
	    Calendar.todoShow();
	},
		
	// 월 변경 후 캘린더 재생성
	changeMonth: function(step) {
	    Calendar.currentDate.setMonth(Calendar.currentDate.getMonth() + step);
	    Calendar.generateCalendar(Calendar.currentDate);
	},
	
	showTodo:function(){
		var str ="";
		str += "<h2>To-Do 리스트</h2>";
		str += "<div class='todo-input-area'>";
		str += "<input type='text' id='todo-input' placeholder='할 일을 입력하세요'/>";
		str += "<button id='add-todo-button' onclick='Calendar.todoPlus()'>+</button>";
		str += "</div>";
		str += "<div id='todo-list'></div>";
		str += "</div>";
		$("#todo-title").html(str);
	},
}