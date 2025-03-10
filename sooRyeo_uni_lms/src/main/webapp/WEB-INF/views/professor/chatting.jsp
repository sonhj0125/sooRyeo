<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<%-- Font Awesome 6 Icons --%>
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
</head>
<script type="text/javascript">
	$(document).ready(function(){
		const senderId = "${requestScope.senderId}"
		const senderType = "${requestScope.senderType}"

		$("div#mycontent").css({"background-color":"#d1e0e0"});
		// div#mycontent 는  /board/src/main/webapp/WEB-INF/tiles/layout/layout-tiles1.jsp 파일의 내용에 들어 있는 <div id="mycontent"> 이다.

		const url = window.location.host; // 웹브라우저의 주소창의 포트까지 가져옴
		// alert("url : " + url);
		// url : 192.168.0.189:9099

		const pathname = window.location.pathname; // 최초 '/' 부터 오른쪽에 있는 모든 경로
		// alert("pathname : " + pathname);
		// pathname : /board/chatting/multichat.action

		const appCtx = pathname.substring(0, pathname.lastIndexOf("/")); // "전체 문자열".lastIndexOf("검사할 문자");
		// alert("appCtx : " + appCtx);
		// appCtx : /board/chatting

		const root = url + appCtx;


		const wsUrl = "ws://"+root+"/chat/socket.lms?roomId=" + "${requestScope.roomId}";

		const websocket = new WebSocket(wsUrl);

		let messageObj = {}; // 자바스크립트 객체 생성함
		// === 웹소켓에 최초로 연결이 되었을 경우에 실행되어지는 콜백함수 정의하기 === //
		websocket.onopen = function(){
			// alert("웹소켓연결됨");
			$("div#chatStatus").text("정보: 웹소켓에 연결이 성공됨!!");
			getUnreadNotification();
		};

		// === 메시지 수신시 콜백함수 정의하기 === //
		websocket.onmessage = function(event){
			console.log(event.data);

			const message = JSON.parse(event.data);

			const now = new Date();
			let ampm = "오전 ";
			let hours = now.getHours();

			if(hours > 12) {
				hours = hours - 12;
				ampm = "오후 ";
			}
			if(hours == 0) {
				hours = 12;
			}
			if(hours == 12) {
				ampm = "오후 ";
			}

			let minutes = now.getMinutes();
			if(minutes < 10) {
				minutes = "0"+minutes;
			}

			const currentTime = ampm + hours + ":" + minutes;

			if (message.msgType === "CHAT") {
				if (message.senderId == senderId && message.senderType == senderType) {

					let html = `
						<div style='display: flex; justify-content: flex-end; margin-bottom: 10px;' class="myMsg">
					`
					if(2- message.readStatus.length > 0) {
						html += `<p>   \${2- message.readStatus.length}</p>`
					}

					html += `
						<div style='display: inline-block; padding: 20px 5px 0 0; font-size: 7pt; float: left'>\${currentTime}</div>
                        <div style='background-color: #ffff80; display: inline-block; max-width: 80%; padding: 7px; border-radius: 10px; word-break: break-all;'>
                            \${message.content}
                        </div>
                    </div>
					`

					$("#chatMessage").append(html);
				} else {

					let html = `
					<div class ="opponentMsg"> <strong>\${message.name}:</strong></div>
                    <div style='display: flex; justify-content: flex-start; margin-bottom: 10px;'>

                        <div style='background-color: #e0e0e0; display: inline-block; max-width: 80%; padding: 7px; border-radius: 10px; word-break: break-all;'>
                             \${message.content}
                        </div>
                        <div style='display: inline-block; padding: 20px 5px 0 0; font-size: 7pt;  float: right;'>\${currentTime}</div>
					`

					if(2- message.readStatus.length > 0) {
						html += `<p>   \${2- message.readStatus.length}</p>`
					}

					html += `</div>`;



					$("#chatMessage").append(html);
				}
			} else if (message.msgType === "EXIT") {
				$("#chatMessage").append(`<div style='text-align: center; background-color: rgba(245, 245, 220, 0.8); border-radius: 10px; margin: 10px 0;'>\${message.content}</div>`);


			}
			else if(message.msgType === "ENTER") {
				$("#chatMessage").append(`<div style='text-align: center; background-color: rgba(245, 245, 220, 0.8); border-radius: 10px; margin: 10px 0;'>\${message.content}</div>`);

				if(message.senderId != senderId || message.senderType != senderType) {
					$(".myMsg p").each(function() {
						let currentCount = parseInt($(this).text().trim());
						if(currentCount - 1 <=  0) {
							$(this).remove();
						}
						else {
							$(this).text(currentCount - 1);
						}

					});
				}

			}

			$("div#chatMessage").scrollTop(99999999);

/*			getUnreadNotification();*/
		};

		// === 웹소켓 연결 해제시 콜백함수 정의하기 === //
		websocket.onclose = function(){

		}

		///////////////////////////////////////////////////////////////////////////////////////////////

		// === 메시지 입력후 엔터하기 === //
		$("input#message").keyup(function(key){
			if(key.keyCode == 13) {
				$("input#btnSendMessage").click();
			}
		});

		// === 메시지 보내기 === //
		$("input#btnSendMessage").click(function(){

			if( $("input#message").val().trim() != "" ) {


				let messageVal = $("input#message").val();
				messageVal = messageVal.replace(/<script/gi, "&lt;script");
				// 스크립트 공격을 막으려고 한 것임.

				<%--
                 messageObj = {message : messageVal
                            ,type : "all"
                            ,to : "all"};
                --%>
				// 또는
				messageObj = {}; // 자바스크립트 객체 생성함.
				messageObj.content = messageVal;
				messageObj.msgType = "CHAT";
				messageObj.senderType = senderType;
				messageObj.senderId = senderId;

				console.log(messageObj)
				websocket.send(JSON.stringify(messageObj));

				$("div#chatMessage").scrollTop(99999999);

				$("input#message").val("");
				$("input#message").focus();
			}

		});



	}) // end of $(document).ready(function(){})------------------------------------
</script>

<body>
<div class="container-fluid" id="mycontent" style="width:85%; border-radius: 20px;">
	<div class="row">
		<div class="col-md-10 offset-md-1">
			<div id="chatStatus"></div>
			<div id="connectingUserList" style=" max-height: 100px; overFlow: auto;"></div>

			<div id="chatMessage" style=" height:500px; max-height: 500px; overFlow: auto; margin: 20px 0;"></div>

			<input type="text"   id="message" class="form-control" placeholder="메시지 내용"/>
			<input type="button" id="btnSendMessage" class="btn btn-success btn-sm my-3" value="메시지보내기" />
			<input type="button" class="btn btn-danger btn-sm my-3 mx-3" onclick="javascript:location.href='<%=request.getContextPath() %>/professor/dashboard.lms'" value="채팅방나가기" />
		</div>
	</div>
</div>
</body>
</html>