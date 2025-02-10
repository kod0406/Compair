//작동 여부 확인
var AllSession ={
	init: function() {
		Page.init(AllSession.uidSession);
		AllSession.serverSession(null);
	},
	//사용할 모든 세션 스토리지 여기서 저장 후 사용
	
	//유저 세션
	uidSession: function(uid){
		sessionStorage.setItem("uid", uid);	
	},
	uidGet:function(){
		return sessionStorage.getItem("uid");
	},
	
	//서버코드 세션
	serverSession:function(serverSession){
		sessionStorage.setItem("serverSession", serverSession);
	},
	serverGet:function(){
		return sessionStorage.getItem("serverSession");
	},
	
	//최근 선택 날짜 세션
	dateSession:function(dateSession){
		sessionStorage.setItem("dateSession", dateSession);
	},
	dateGet:function(){
		return sessionStorage.getItem("dateSession");
	},
	
	//현재 페이지 세션
	pageSession:function(screen){
		sessionStorage.setItem("pageSession", screen);
	},

	pageGet:function(){
		return sessionStorage.getItem("pageSession");
	},
	
	//글 불러올때 번호 세션
	minSession:function(minSession){
		sessionStorage.setItem("minSession", minSession);
	},
	minGet:function(){
		return sessionStorage.getItem("minSession");
	},
	
	//글 불러올때 번호 세션
	maxSession:function(maxSession){
			sessionStorage.setItem("maxSession", maxSession);
	},
	maxGet:function(){
		return sessionStorage.setItem("maxSession");
	}
};