<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

 
<!DOCTYPE html>
<% String ctxPath = request.getContextPath(); %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SooRyeo Univ.</title>
    <!-- Bootstrap CSS -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
<!--     <link href="https://cdnjs.cloudflare.com/ajax/libs/gridstack.js/4.3.1/gridstack.min.css" rel="stylesheet"/> -->
    <%-- <link href="<%=ctxPath %>/resources/node_modules/gridstack/dist/gridstack.min.css" rel="stylesheet"/> --%>
    
    <style>
		body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0; 
            display: flex;
            height: 100vh;
            background-color: white;
        }

        .sidebar {
            width: 250px;
            background-color: #d1e0e0;
            color: white;
            display: flex;
            flex-direction: column;
            align-items: center;
            padding-top: 20px;
            box-shadow: 2px 0 5px rgba(0,0,0,0.1);
            font-weight: bold;
        }

		.sidebar ul li:hover {
			background-color: white;
			margin-left: 10%;
			width:85%;
		
		}
        .sidebar .profile {
            text-align: center;
            margin-bottom: 20px;
        }

        .sidebar .profile img {
            width: 100px;
            height: 100px;
            border-radius: 50%;
        }

        .sidebar .profile h3 {
            margin: 10px 0 5px 0;
            font-size: 18px;
        }

        .sidebar .profile p {
            margin: 0;
            font-size: 14px;
            color: white;
        }

        .sidebar ul {
            list-style-type: none;
            padding: 0;
            width: 100%;
        }

        .sidebar ul li {
            width: 100%;
            margin: 5px;
        }

        .sidebar ul li a {
            width: 70%;
            margin: 0 auto;
            display: flex;
            align-items: center;
            padding: 15px 20px;
            text-decoration: none;
            color: #666;
            transition: background 0.3s ease, color 0.3s ease;
            font-size: 16px;
        }

        .sidebar ul li a .icon {
            margin-right: 10px;
            font-size: 18px;
        }

     
        .sidebar ul li a.active .icon {
            color: white;
        }

        .dropdown-menu {
            background-color: white;
        }

        .dropdown-item {
			padding-bottom: 2px;
			background-size: 0 2px;
			transition: background-size 0.5s;
        }

        .dropdown-item:hover {
        	background-size: 100% 2px;
			background-image: linear-gradient(#175F30, #175F30);
			background-repeat: no-repeat;
			background-position: top left;
        }
        .content {
            flex-grow: 1;
            overflow-y: auto;
        }

        .main-content {
            padding: 20px;
        }

        .header {
            background-color: white;
            color: #666;
            padding: 15px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            position: -webkit-sticky; /* Safari */
            position: sticky;
            top: 0;
            z-index: 1000;
            border-radius: 10px; /* Rounded edges */
            margin: 10px; /* Margin to prevent clipping */
        }

        .header .search-bar {
            display: flex;
            align-items: center;
            background-color: #f4f4f4;
            padding: 10px 20px;
            border-radius: 20px;
            width: 100%;
            max-width: 500px;
            position: relative;
        }

        .header .search-bar input {
            border: none;
            background: none;
            outline: none;
            margin-left: 10px;
            font-size: 16px;
            width: 100%;
        }

        .header .icons {
            display: flex;
            align-items: center;
        }
  /*      .header .icons span:first-child{
            margin-left: 20px;
        }*/
        .header .icons span {
            margin-left: 20px;
            font-size: 25px;
            cursor: pointer;
        }

/*         .grid-stack-item-content {
            background-color: white;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
            font-size: 18px;
            font-weight: bold;
            color: #333;
            transition: transform 0.2s, box-shadow 0.2s;
        }

        .grid-stack-item-content:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
        } */
        
        
		#displayList {
		    max-height: 250px; /* 최대 높이 설정 */
		    background-color: #f4f4f4; /* 배경색 변경 */
		    margin-left: 50px; /* 검색 바와의 간격 */
		    height: auto; /* 내용에 따라 자동 높이 조정 */
		    box-sizing: border-box; /* 패딩과 경계를 너비에 포함 */
		    position: absolute; /* 헤더와 겹치도록 절대 위치 설정 */
		    z-index: 10000; /* .header보다 높은 z-index */
		    overflow: auto; /* 내용이 넘칠 경우 스크롤 추가 */
		    border-radius: 0 0 20px 20px; /* 모서리 둥글게 */
		    padding: 10px; /* 내부 여백 추가 */
		    padding-left: 20px;
		    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 부드러운 그림자 추가 */
		    transition: box-shadow 0.3s; /* 마우스 오버 시 효과를 위한 전환 */
		    opacity: 0.9;
		    border: none;
		    margin-left:2.5%;
		    width: 100%;
		}
		
		span.result:hover {
			color: purple;
			font-weight: bold;
		
		}

        #mailDropdown {

            display: none;
            position: absolute;
            top: 40px;
            right: 0;
            background-color: white;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            width: 280px;
            max-height: 400px;
            overflow-y: auto;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            z-index: 1000;
        }

        .mail-dropdown-item {
            padding: 10px 14px;
            border-bottom: 1px solid #f0f0f0;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .mail-dropdown-item:last-child {
            border-bottom: none;
        }

        .mail-dropdown-item:hover {
            background-color: #f8f8f8;
        }

        .mail-item-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 4px;
        }

        .mail-item-title {
            font-weight: bold;
            color: #333;
        }

        .mail-item-room {
            color: #666;
        }

        .mail-item-body {
            font-size: 11px;
            color: #444;
        }

        .mail-item-unread {
            color: #1a73e8;
            font-weight: bold;
        }

        .no-messages {
            padding: 14px;
            text-align: center;
            color: #666;
            font-style: italic;
        }
    </style>
</head>
<body>
	<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
	
<script type="text/javascript">
    function getUnreadNotification() {
        fetch('<%=ctxPath%>/professor/chatAlertREST.lms', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                console.log(response);
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                console.log('Data received:', data);

                let totalUnreadCount = Object.values(data).reduce((sum, value) => sum + value.unreadCount, 0);

                console.log(totalUnreadCount);

                if(totalUnreadCount > 0) {
                    document.getElementById('message').innerHTML += `

            <div class="badge" id="unreadCountBadge" style="position: absolute; right: -10px; top: -10px; background-color: red; color:white; align-content: center; font-size: 12px; border-radius: 50%; width: 23px; height: 23px;">
                \${totalUnreadCount}
            </div>

            `;
                }

                const mailDropdown = document.getElementById('mailDropdown');
                mailDropdown.innerHTML = '';

                if(totalUnreadCount > 0) {
                    Object.entries(data).forEach(([key, value]) => {
                        const item = document.createElement('div');
                        item.className = 'mail-dropdown-item';
                        item.innerHTML = `
                    <div class="mail-item-header">
                        <span class="mail-item-title ml-0" style="font-size: 13px">상담명: </span>
                        <span class="mail-item-room ml-0" style="font-size: 13px">\${value.roomName}</span>
                    </div>
                    <div class="mail-item-body">
                        <span class="mail-item-unread ml-0" style="font-size: 13px">\${value.unreadCount}개 안읽었습니다</span>
                    </div>
                `;
                        /*                item.style.padding = '10px';
                                        item.style.borderBottom = '1px solid #ccc';*/
                        item.onclick =function() {
                            location.href = `<%=ctxPath%>/chat.lms?roomId=\${key}`;
                        };
                        mailDropdown.appendChild(item);
                    });
                }
                else {
                    const item = document.createElement('div');
                    item.className = 'mail-dropdown-item';
                    item.textContent = `메시지가 없습니다`;
                    item.style.fontSize = '13px'
                    /*                item.style.padding = '10px';
                                    item.style.borderBottom = '1px solid #ccc';*/
                    mailDropdown.appendChild(item);
                }


                document.getElementById('message').addEventListener('click', function(event) {
                    const dropdown = document.getElementById('mailDropdown');
                    if (dropdown.style.display === 'none' || dropdown.style.display === '') {
                        dropdown.style.display = 'block';
                    } else {
                        dropdown.style.display = 'none';
                    }
                    event.stopPropagation(); // Stop the click event from propagating to the document
                });

                // Add an event listener to the document to hide the dropdown when clicking outside
                document.addEventListener('click', function(event) {
                    const dropdown = document.getElementById('mailDropdown');
                    const messageDiv = document.getElementById('message');
                    if (!dropdown.contains(event.target) && !messageDiv.contains(event.target)) {
                        dropdown.style.display = 'none';
                    }
                });


            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }



    let isChatPage = false;


$(document).ready(function(){
    isChatPage = window.location.pathname.includes('/chat.lms');


	$("div#displayList").hide()



    if(!isChatPage) {
        getUnreadNotification();
    }


	
	$("input[name='search']").keyup(function(){
		
		const wordLength = $(this).val().trim().length;
		// 검색어에서 공백을 제외한 길이를 알아온다.
		
		if(wordLength == 0){
			$("div#displayList").hide();
			// 검색어가 공백이거나 검색어 입력후 백스페이스키를 눌러서 검색어를 모두 지우면 검색된 내용이 안 나오도록 해야 한다.
		}
		else{
			$.ajax({
				url:"<%= ctxPath%>/student/wordSearchShow.lms",
				type:"get",
				data:{"searchWord":$("input[name='search']").val()},
				dataType:"json",
				success:function(json){
					<%-- #120. 검색어 입력시 자동글 완성하기 7 --%>
					if(json.length > 0){
						// 검색된 데이터가 있는 경우임.
						
						let v_html = ``;
						
						$.each(json, function(index, item){
							
							const urlIdx = item.name.indexOf(",");
							
							const name = item.name.substring(0,urlIdx);
							const url = item.name.substring(urlIdx+1);
							
							// word ==> javascript 는 재미가 있어요
							// word ==> 그러면 javaScript  는 뭔가요? ==> 대문자 포함됨
							
							// word.toLowerCase() 은 word 를 모두 소문자로 변경하는 것이다.
							// word ==> javascript 는 재미가 있어요
							// word ==> 그러면 javascript  는 뭔가요? ==> 대문자 사라짐
							
							const idx = name.toLowerCase().indexOf($("input[name='search']").val().toLowerCase());
							// 만약에 검색어가 JavA 같이 적었다면
							/*
								그러면 javascript  는 뭔가요?   는 idx 가 4 이다.
								javascript 는 재미가 있어요             는 idx 가 0 이다.
							*/
							
							const len = $("input[name='search']").val().length;
							// 검색어(JavA)의 길이 len은 4가 된다.
							/*
								console.log("~~~~~ 시작 ~~~~~");
								console.log(word.substring(0,idx));         // 검색어 전까지         ==> 저는
								console.log(word.substring(idx,idx + len)); // 검색어                   ==> java
								console.log(word.substring(idx + len));     // 검색어 이후 나머지  ==> 에 대해서 궁금해요~~
								console.log("~~~~~ 끝 ~~~~~");
							*/
							
							const result = `<img src='<%=ctxPath%>/resources/images/glass.png' style='width:15px; height:15px; margin-right:4%; vertical-align: middle;'>` 
									 	 + "<span style='vertical-align: middle;'>" + name.substring(0, idx) + "</span>" 
										 + "<span style='color:purple; font-weight:bold; vertical-align: middle;'>" + name.substring(idx, idx + len) + "</span>" 
										 + "<span style='vertical-align: middle;'>" + name.substring(idx + len) + "</span>";
							
							v_html += `<span style='cursor:pointer;' data-custom="\${url}" class='result'>\${result}<br></span>`;
						}); // end of $.each(json, function(index, item){})------------------------------------
						
						const input_width = $("input[name='search']").css("width"); // 검색어 input 태그 width 값 알아오기
						
						$("div#displayList").css({"width":input_width}); // 검색결과 div 의 width 크기를 검색어 입력 input 태그의 width 와 일치시키기 
						
						$("div#displayList").html(v_html);
						
						$("div#displayList").show();
					}
				},
				error: function(request, status, error){
		          alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			    }
			});// ajax------------------------------
		}
	
	}); // $("input[name='search']").keyup(function(){})-------------------------------
		
	<%-- #121. 검색어 입력시 자동글 완성하기 8 --%>
	$(document).on("click", "span.result", function(e){
		
		const url = $(this).data('custom');
		const name = $(this).text();
		
		$("input[name='search']").val(name); // 텍스트 박스에 검색된 결과의 문자열을 입력해준다. 클릭하면 그 클릭한 문장을 검색 텍스트에 적어주는 것.
		$("div#displayList").hide(); // 검색할 문장을 선택했으면 리스트를 숨겨주는 것
		
		location.href = `<%=ctxPath%>\${url}`;
		
	});
	
	
	// 마우스로 다른 곳을 클릭 시 검색 결과 리스트 숨기기
	$(document).click(function(e) {
		if (!$(e.target).closest("div#displayList").length && !$(e.target).is("input[name='search']")) {
			$("div#displayList").hide();
		}
	});
	
});
</script>	
	
	
    <div class="sidebar">
        <div class="profile">
        	<c:if test="${empty sessionScope.loginuser.img_name}"> <%-- 이미지가 없을 경우 --%>
            	<img src="<%=ctxPath%>/resources/images/teacher.png" alt="Profile Picture">
            </c:if>
            <c:if test="${not empty sessionScope.loginuser.img_name}"> <%-- 이미지가 있을 경우 --%>
            	<img src="<%=ctxPath%>/resources/files/${sessionScope.loginuser.img_name}" alt="Profile Picture">
            </c:if>
            <h3>${sessionScope.loginuser.name}</h3>
            <p>${sessionScope.loginuser.department.department_name}</p>
        </div>
        <ul class="nav flex-column">
            <li class="nav-item"><a href="<%=ctxPath%>/professor/dashboard.lms" class="nav-link active"><span class="icon">🏠</span>대쉬보드</a></li>

            <li class="nav-item dropdown">
                <a href="#classes" class="nav-link dropdown-toggle" id="classesMenu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="icon">📚</span>수업</a>
                <div class="dropdown-menu" aria-labelledby="classesMenu">
                    <a class="dropdown-item" href="<%=ctxPath%>/professor/courseList.lms">내 수업</a>
                    <!-- <a class="dropdown-item" href="#">출석현황</a> -->
                </div>
            </li>
            <li class="nav-item">
                <a href="#schedule" class="nav-link dropdown-toggle" id="scheduleMenu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="icon">📅</span>스케줄</a>
                <div class="dropdown-menu" aria-labelledby="scheduleMenu" >
                    <a class="dropdown-item" href="<%=ctxPath %>/professor/approveConsult.lms">상담 승인</a>
                    <a class="dropdown-item" href="<%=ctxPath %>/professor/consult.lms">상담 일정</a>
                </div>
            </li>
            <li class="nav-item">
                <a href="#grades" class="nav-link dropdown-toggle" id="gradesMenu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="icon">📈</span>성적</a>
                <div class="dropdown-menu" aria-labelledby="gradesMenu" >
                    <a class="dropdown-item" href="<%=ctxPath %>/professor/insertGradeform.lms">성적 기입</a>
                    <!-- <a class="dropdown-item" href="#">취득 현황</a> -->
                </div>
            </li>
            <li class="nav-item">
                <a href="#groups" class="nav-link dropdown-toggle" id="groupsMenu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="icon">👥</span>커뮤니티</a>
                <div class="dropdown-menu" aria-labelledby="groupsMenu" >
                    <!-- <a class="dropdown-item" href="#">내 친구</a> -->
                    <a class="dropdown-item" href="<%=ctxPath %>/board/announcement.lms">학사공지사항</a>
                </div>
            </li>
            <li class="nav-item"><a href="<%=ctxPath%>/professor/info.lms" class="nav-link"><span class="icon">⚙️</span>내정보</a></li>
            <li class="nav-item"><a href="<%=ctxPath%>/logout.lms" class="nav-link"><span class="icon">➡️</span>로그아웃</a></li>
        </ul>
    </div>
    <div class="content">
        <div class="header sticky-top">
            <div style="width:100%;">
	            <div class="search-bar">
	                <span class="icon">🔎</span>
	                <input type="text" name="search" placeholder="메뉴검색" autocomplete='off'>
	            </div>
	            <div id="displayList"></div>
            </div>
            <div class="icons">
                <span id="message" class="icon" style="position: relative">
                    📫
                    <span class="mail-dropdown" id="mailDropdown" style="display: none; position: absolute; top: 30px; right: 0; background-color: white; border: 1px solid #ccc; border-radius: 5px; width: 200px; max-height: 300px; overflow-y: auto; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); z-index: 1;"></span>
                </span>
                <span class="icon">🔔</span>
                <span class="icon">❔</span>
            </div>
        </div>
        
        
        <div class="main-content">
        
           <tiles:insertAttribute name="content" />
        
        
        </div>

<!--         <div class="main-content grid-stack">
            <div class="grid-stack-item" data-gs-width="4" data-gs-height="2">
                <div class="grid-stack-item-content">Item 1</div>
            </div>
            <div class="grid-stack-item" data-gs-width="4" data-gs-height="2">
                <div class="grid-stack-item-content">Item 2</div>
            </div>
            <div class="grid-stack-item" data-gs-width="4" data-gs-height="2">
                <div class="grid-stack-item-content">Item 3</div>
            </div>
        </div>
 -->
    </div>

    <!-- Bootstrap JS and dependencies -->

<%--     <script src="<%=ctxPath %>/resources/node_modules/gridstack/dist/gridstack-all.js"></script>
    <script type="text/javascript">
        var grid = GridStack.init();
    </script> --%>
</body>
</html>
