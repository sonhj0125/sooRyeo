<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String ctxPath = request.getContextPath();
%>


<style type="text/css">
.subject-container {
	width: 100%;
	height: 300px; /* 원하는 높이로 설정 */
	overflow-x: auto;
	overflow-y: auto;
}

#top_container {
	border: solid 0px gray;
}

#bottom_container {
	border: solid 0px gray;
}

#total_container {
	border: solid 0px black;
}

#right_top_card {
	width: 90%;
}

#left_top_card {
	width: 90%;
}

th {
	text-align: center;
}

.timetable th, .timetable td {
	/*border: 1px solid black;*/
	border-collapse: collapse;
	text-align: center;
	width: 100px;
	padding: 5px;
	height: 20px;
	text-align: center;
}



.ml-n1 {
	margin-left: -2.5rem !important;
}

.pagination {
	justify-content: center;
}

.timetable {
	width: 100%;
	border-collapse: collapse;
}

.timetable th, .timetable td {
	border: 1px solid #ddd;
	padding: 8px;
	text-align: center;
}

.timetable th {
	background-color: #f2f2f2;
}

.timetable td {
	height: 50px;
}

.time-slot {
	background-color: #f9f9f9;
}

#course-list-container {
	margin-top: 20px;
	padding: 20px;
	background-color: #f8f9fa;
	border-radius: 8px;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.course-list-title {
	font-size: 1.5em;
	margin-bottom: 10px;
	text-align: center;
}

.course-list {
	list-style-type: none;
	padding: 0;
}

.course-list li {
	display: flex;
	align-items: center;
	padding: 10px;
	margin-bottom: 10px;
	background-color: #ffffff;
	border: 1px solid #e0e0e0;
	border-radius: 4px;
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.course-list li span {
	flex: 1;
	font-size: 1em;
	color: #333;
}
</style>


<script>

/**
 * 
 */
 
let departmentSelect;
let gradeSelect;
let selectedDepartment = "";
let selectedGrade = "";


const dayMap = {
        1: 'monday',
        2: 'tuesday',
        3: 'wednesday',
        4: 'thursday',
        5: 'friday'
    };

const colors = [
    "#d1e7dd", // light green
    "#f8d7da", // light red
    "#fff3cd", // light yellow
    "#d1ecf1", // light cyan
    "#f5c6cb", // light pink
    "#d6d8d9", // light gray
    "#c3e6cb", // light green
    "#bee5eb"  // light blue
];



let colorIndex = 0;

function getDayOfWeek(day) {
	const days = ["월", "화", "수", "목", "금", "토", "일"];
	return days[day - 1];
}

function fetchData(pageNum) {
    console.log(pageNum);
    const url = '<%=ctxPath%>' + '/student/courseJSON.lms?fk_department_seq=' + selectedDepartment + '&grade=' + selectedGrade + '&currentPage=' + pageNum;
    console.log(url);
    fetch(url)
        .then(response => response.json())
        .then(data => {
            const tableBody = document.querySelector("table tbody");
            tableBody.innerHTML = "";
            console.log(data);
			if (data.length === 0) {
				// Display message when no courses are available
				const messageRow = document.createElement("tr");
				messageRow.innerHTML = "<td colspan='8' class='text-center'>개설된 강의 없음</td>";
				tableBody.appendChild(messageRow);
			} else {
				data.forEach(function(data) {
					var row = document.createElement("tr");
					console.log(data.register_count)
					console.log(data.capacity)

					const timeString = data.timeList.map(item => {
						const day = getDayOfWeek(item.day_of_week);
						return `\${day} \${item.start_period}-\${item.end_period}`;
					}).join(", ");

					row.innerHTML = "<td><button type='button' class='btn sign-up-btn btn-primary' data-course-seq='" + data.course_seq + "'>신청</button></td>" +
							"<td>" + data.curriculum.name + "</td>" +
							"<td>" + timeString + "</td>" +
							"<td>" + data.professor.name + "</td>" +
							"<td>" + (data.curriculum.grade == null ? '' : data.curriculum.grade) + "</td>" +
							"<td>" + data.curriculum.credit + "</td>" +
							"<td>" + (data.curriculum.required === 1 ? 'Yes' : 'No') + "</td>" +
							"<td>" + data.register_count  + "/" +  data.capacity + "</td>";
					tableBody.appendChild(row);
				});
			}

            // Add event listeners to the sign-up buttons
            document.querySelectorAll('.sign-up-btn').forEach(button => {
                button.addEventListener('click', handleSignUp);
            });
        })
        .catch(error => console.error("Error fetching data:", error));
}


function handleSignUp(event) {
    const courseSeq = event.target.getAttribute('data-course-seq');
    console.log("Signing up for course:", courseSeq);
	
    
    const url = '<%=ctxPath%>' + '/student/registerCourseREST.lms'
    console.log(url);
    
    
    // Create the data to be sent
    const data = new URLSearchParams();
    data.append('course_seq', courseSeq);

    // Send POST request
    fetch(url, {
        method: 'POST',
        body: data,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
    })
    .then(response => {
        return response.text().then(text => {
            return { status: response.status, body: text };
        });
    })
    .then(result => {
        console.log('Success:', result);
        // Handle the response here (e.g., show a success message)
        
    	if(result.status == 200) {
    		alert(result.body)
    		fetchTimeTable();
    		fetchData(1);
    	}
    	else {
    		alert(result.body)
    		fetchData(1);
    	}
    })
    .catch(error => {
    	console.error("error: ");
        console.error(error);
        fetchData(1);
        // Handle any errors here (e.g., show an error message)
        alert(error)
    });
    
}



function fetchTimeTable() {

    const url = '<%=ctxPath%>' + '/student/timetableJSON.lms'

    console.log(url);

    fetch(url)
        .then(response => response.json())
        .then(data => {

			console.log(data);
			clearTimetable();
			recreateTimetableStructure();
			
			fillTimetable(data);

        })
        .catch(error => console.error("Error fetching data:", error));
}

function handleDepartmentChange() {
    const departmentValue = departmentSelect.value;
    if (departmentValue === "") {
        gradeSelect.disabled = true;
    } else {
        gradeSelect.disabled = false;
    }
}

document.addEventListener("DOMContentLoaded", function() {
    departmentSelect = document.getElementById("department");
    gradeSelect = document.getElementById("grade");

    departmentSelect.addEventListener("change", handleDepartmentChange);
    
    console.log(departmentSelect.value);
    console.log(gradeSelect.value);
    
    
    

    const searchButton = document.getElementById("searchButton");
    searchButton.addEventListener('click', function() {
        selectedDepartment = departmentSelect.value;
        selectedGrade = gradeSelect.value;
        fetchData(1);
    });

    // Fetch initial data
    fetchData(1);
    handleDepartmentChange();
    fetchTimeTable();
    
    
    


    
});



function isTimeslotAvailable(day, start_period, end_period) {
    for (let period = start_period; period <= end_period; period++) {
        const slotId = day + "-" + period;
        const slot = document.getElementById(slotId);
        if (slot && slot.textContent.trim() !== "") {
            return false;
        }
    }
    return true;
}

function fillTimetable(data) {
    const courseListContainer = document.getElementById('course-list-container');
    courseListContainer.innerHTML = ''; // Clear previous course list
    
    console.log(data);

    // Create table
    const table = document.createElement('table');
    table.className = 'table table-bordered table-hover';
    
    // Create table header
    const thead = document.createElement('thead');
    thead.innerHTML = `
        <tr>
            <th>강의명</th>
            <th>교수</th>
            <th>삭제</th>
        </tr>
    `;
    
    table.appendChild(thead);

    // Create table body
    const tbody = document.createElement('tbody');

    data.courseList.forEach((course, index) => {
        const color = colors[index % colors.length];
        const row = document.createElement('tr');
        row.style.backgroundColor = color;
        
        row.innerHTML = `
            <td>\${course.curriculum.name}</td>
            <td>\${course.professor.name}</td>
            <td><button class="btn btn-danger btn-sm delete-course" data-course-id="\${course.course_seq}">삭제</button></td>
        `;

        tbody.appendChild(row);
        

        course.timeList.forEach(time => {

            // Fill the timetable
            const day = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday'][time.day_of_week - 1];
            if (isTimeslotAvailable(day, time.start_period, time.end_period)) {
                const slotId = day + "-" + time.start_period;
                const slot = document.getElementById(slotId);
                if (slot) {
                    slot.textContent = course.curriculum.name;
                    slot.style.backgroundColor = color;
                    slot.style.verticalAlign = 'middle';
                    slot.rowSpan = time.end_period - time.start_period + 1;
                    for (let period = time.start_period + 1; period <= time.end_period; period++) {
                        const nextSlotId = day + "-" + period;
                        const nextSlot = document.getElementById(nextSlotId);
                        if (nextSlot) {
                            nextSlot.remove();
                        }
                    }
                }
            } else {
                console.warn("이미 선택된 시간입니다");
            }
        });
    });

    table.appendChild(tbody);
    courseListContainer.appendChild(table);

    // Add event listeners for delete buttons
    document.querySelectorAll('.delete-course').forEach(button => {
        button.addEventListener('click', function() {
            const courseId = this.getAttribute('data-course-id');
            deleteCourse(courseId);
        });
    });
    
   
}


function openEditModal(courseId) {
    const course = findCourseById(courseId);
    if (!course) return;

    document.getElementById('editCourseId').value = courseId;
    document.getElementById('editCapacity').value = course.capacity;
    
    const timeSlotsContainer = document.getElementById('editTimeSlots');
    timeSlotsContainer.innerHTML = '';
    
    course.timeList.forEach((time, index) => {
        addEditTimeSlot(time, index);
    });
    
    $('#editCourseModal').modal('show');
}

function addEditTimeSlot(time = null, index = null) {
    const timeSlotsContainer = document.getElementById('editTimeSlots');
    const slotId = index !== null ? index : timeSlotsContainer.children.length;
    
    const timeSlotHtml = 
        '<div class="form-row mt-3" id="editTimeSlot-' + slotId + '">' +
            '<div class="form-group col-md-3">' +
                '<label for="editDayOfWeek-' + slotId + '">요일</label>' +
                '<select class="form-control" id="editDayOfWeek-' + slotId + '" required>' +
                    '<option value="1"' + (time && time.day_of_week === 1 ? ' selected' : '') + '>월</option>' +
                    '<option value="2"' + (time && time.day_of_week === 2 ? ' selected' : '') + '>화</option>' +
                    '<option value="3"' + (time && time.day_of_week === 3 ? ' selected' : '') + '>수</option>' +
                    '<option value="4"' + (time && time.day_of_week === 4 ? ' selected' : '') + '>목</option>' +
                    '<option value="5"' + (time && time.day_of_week === 5 ? ' selected' : '') + '>금</option>' +
                '</select>' +
            '</div>' +
            '<div class="form-group col-md-3">' +
                '<label for="editStartPeriod-' + slotId + '">시작 교시</label>' +
                '<select class="form-control" id="editStartPeriod-' + slotId + '" required>' +
                    generatePeriodOptions(time ? time.start_period : 1) +
                '</select>' +
            '</div>' +
            '<div class="form-group col-md-3">' +
                '<label for="editEndPeriod-' + slotId + '">끝 교시</label>' +
                '<select class="form-control" id="editEndPeriod-' + slotId + '" required>' +
                    generatePeriodOptions(time ? time.end_period : 1) +
                '</select>' +
            '</div>' +
            '<div class="form-group col-md-3">' +
                '<label>&nbsp;</label>' +
                '<button type="button" class="btn btn-danger form-control" onclick="removeEditTimeSlot(' + slotId + ')">삭제</button>' +
            '</div>' +
        '</div>';
    
    timeSlotsContainer.insertAdjacentHTML('beforeend', timeSlotHtml);
}

function removeEditTimeSlot(slotId) {
    const slotElement = document.getElementById('editTimeSlot-' + slotId);
    if (slotElement) {
        slotElement.remove();
    }
}

function generatePeriodOptions(selectedPeriod) {
    let options = '';
    for (let i = 1; i <= 8; i++) {
        options += '<option value="' + i + '"' + (i === selectedPeriod ? ' selected' : '') + '>' + i + '교시</option>';
    }
    return options;
}

function saveEditCourse() {
    const courseId = parseInt( document.getElementById('editCourseId').value);
    const capacity = parseInt(document.getElementById('editCapacity').value);
    const timeSlots = document.getElementById('editTimeSlots').children;
    const timeSlotsArray = Array.from(timeSlots);
    
    
    let timeData = [];
    
    timeSlotsArray.forEach(slot => {
    	const slotId = slot.id.split('-')[1];
    	
    	let day_of_week = parseInt(document.getElementById('editDayOfWeek-' + slotId).value);
    	let start_period = parseInt(document.getElementById('editStartPeriod-' + slotId).value);
    	let end_period = parseInt(document.getElementById('editEndPeriod-' + slotId).value);
    	
    	console.log(day_of_week)
    	console.log(start_period)
    	console.log(end_period)
    	
        if (start_period > end_period) {
            alert("끝나는 교시는 시작교시와 같거나 커야합니다");
            return;
        }
        
        const data = {
                "day_of_week": day_of_week,
                "start_period": start_period,
                "end_period": end_period
            };
        
        timeData.push(data);
    	
    })
    

    
    const jsonData = {
    		"course_seq" : courseId,
    		"capacity" : capacity,
    		"timeList" : timeData

    }
    console.log(jsonData);
    
    fetch('<%=ctxPath%>/admin/courseUpdateREST.lms', {
        method: 'POST',
        headers: {
        	'Content-Type' : 'application/json'
        },
        body: JSON.stringify(jsonData),
    })
    .then(data => {
        if (data.ok) {
            $('#editCourseModal').modal('hide');
            let profId = document.getElementById("professor-search").value;
            fetchProfTimeTable(profId);
        } else {
            console.error('Failed to update course:', data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function findCourseById(course_seq) {
    let result;
    
    $.ajax({
        url: '<%=ctxPath%>/admin/getCourseREST.lms',
        method: 'POST',
        data: { "course_seq": course_seq },
        async: false,
        dataType: 'json',
        success: function(data) {
            if (data) {
                console.log(data);
                result = data;
            } else {
                console.error('강의 정보 가져오기 실패:', data.message);
            }
        },
        error: function(xhr, status, error) {
            console.error('Error:', error);
        }
    });
    
    return result;
}



function deleteCourse(courseId) {
    // Implement the logic to delete the course
    console.log(`Deleting course with ID: \${courseId}`);
    
    
    if(!confirm("수강 취소 하시겠습니까?")) {
    	return false;
    }
   
	
    
    fetch('<%=ctxPath%>/student/dropCourseREST.lms', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({"course_seq" : courseId})
    })
    .then(response => {
        return response.text().then(text => {
            return { status: response.status, body: text };
        });
    })
    .then(result => {
        if (result.status == 200) {
    		fetchTimeTable();
    		fetchData(1);
            alert(result.body);
        } else {
            alert("잠시후 다시 시도해 주세요: " + result.body);
        }
    })
    .catch((error) => {
        console.error('Error:', error);
        alert("수강취소 중 오류가 발생했습니다.");
    });


}

/*function resetTimetable() {
    const tableBody = document.getElementById('timetable-body');
    for (let day of ['monday', 'tuesday', 'wednesday', 'thursday', 'friday']) {
        for (let period = 1; period <= 8; period++) {
            let cell = document.createElement('td');
            cell.id = day + "-" + period;
            cell.className = 'time-slot';
            let existingCell = document.getElementById(day + "-" + period);
            if (existingCell) {
                existingCell.replaceWith(cell);
            }
        }
    }
}*/

function clearTimetable() {
	const timetable = document.querySelector('.timetable tbody');
	timetable.innerHTML = '';
}

function recreateTimetableStructure() {
	const timetable = document.querySelector('.timetable tbody');
	const days = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday'];

	for (let i = 1; i <= 8; i++) {
		const row = document.createElement('tr');
		row.innerHTML = `<td>\${i}</td>`;

		days.forEach(day => {
			row.innerHTML += `<td id="\${day}-\${i}" class="time-slot"></td>`;
		});

		timetable.appendChild(row);
	}
}


/* let formCounter = 0;

function addForm() {
    formCounter++;
    const formHtml = `
        <form class="row mt-3 schedule-form" id="schedule-form-\${formCounter}">
            <div class="form-group col-md-2">
                <label for="day-of-week-\${formCounter}">요일</label> 
                <select class="form-control" id="day-of-week-\${formCounter}" required>
                    <option value="1">월</option>
                    <option value="2">화</option>
                    <option value="3">수</option>
                    <option value="4">목</option>
                    <option value="5">금</option>
                </select>
            </div>
            <div class="form-group col-md-2">
                <label for="start-period-\${formCounter}">시작 교시</label> 
                <select class="form-control" id="start-period-\${formCounter}" required>
                    <option value="1">1교시</option>
                    <option value="2">2교시</option>
                    <option value="3">3교시</option>
                    <option value="4">4교시</option>
                    <option value="5">5교시</option>
                    <option value="6">6교시</option>
                    <option value="7">7교시</option>
                    <option value="8">8교시</option>
                </select>
            </div>
            <div class="form-group col-md-2">
                <label for="end-period-\${formCounter}">끝 교시</label> 
                <select class="form-control" id="end-period-\${formCounter}" required>
                    <option value="1">1교시</option>
                    <option value="2">2교시</option>
                    <option value="3">3교시</option>
                    <option value="4">4교시</option>
                    <option value="5">5교시</option>
                    <option value="6">6교시</option>
                    <option value="7">7교시</option>
                    <option value="8">8교시</option>
                </select>
            </div>
            <div class="col-md-2" style="margin-top:5%">
                <button type="button" class="btn btn-danger" onclick="removeForm(this)">삭제</button>
            </div>
        </form>
    `;
    $('#form-container').append(formHtml);
}

function removeForm(button) {
    $(button).closest('.schedule-form').remove();
}

function clearScheduleForms() {
	document.getElementById("capacity").value='';
	
    const formContainer = document.getElementById('form-container');
    formContainer.innerHTML = '';
    
    // Add one empty form
    addForm();
}
 */

</script>

<div class="justify-content-center" id="total_container">
	<div class="row justify-content-center" id="top_container">
		<div class="card col-md-6 pl-0 pr-0" id="left_top_card">
			<div class="card-header">
				<h3>강의 검색</h3>
			</div>
			<div class="card-body">
				<form class="mb-4">
					<div class="form-row">
						<div class="form-group col-md-5">
							<label for="department">학과</label> <select id="department" name="department" class="form-control">
								<option value="">교양</option>
								<c:forEach items="${departments}" var="department">
									<option value="${department.department_seq}" ${department.department_seq == selectedDepartment ? 'selected' : ''}>${department.department_name}</option>
								</c:forEach>
							</select>
						</div>
						<div class="form-group col-md-5">
							<label for="grade">학년</label> <select id="grade" name="grade" class="form-control">
								<option value="">--학년 선택--</option>
								<option value="1" ${"1".equals(selectedGrade) ? "selected" : ""}>1</option>
								<option value="2" ${"2".equals(selectedGrade) ? "selected" : ""}>2</option>
								<option value="3" ${"3".equals(selectedGrade) ? "selected" : ""}>3</option>
								<option value="4" ${"4".equals(selectedGrade) ? "selected" : ""}>4</option>
							</select>
						</div>
						<div class="form-group col-md-2 d-flex align-items-end">
							<button type="button" class="btn btn-primary" id="searchButton">검색</button>
						</div>
					</div>
				</form>

				<table class="table table-bordered table-hover" id="curri_table">
					<thead class="thead-light">
						<tr>
							<th>선택</th>
							<th>강의 이름</th>
							<th>시간</th>
							<th>교수</th>
							<th>학년</th>
							<th>이수 학점</th>
							<th>필수 여부</th>
							<th>정원</th>
						</tr>
					</thead>
					<tbody>
						<!-- Rows will be populated dynamically -->
					</tbody>
				</table>

				<!-- Pagination -->
				<nav>
					<ul class="pagination">

					</ul>
				</nav>
			</div>



			<!-- <div class="card-header d-flex justify-content-between align-items-center">
				<button type="button" class="btn btn-success" onclick="addSchedule()">시간표에 추가</button>
			</div>

			<div class="card-body">
				<div class="form-group col-md-5 pl-0">
					<label for="capacity-0">수강 정원</label> <input type="number" class="form-control" id="capacity" required min="1">
				</div>
				<div id="form-container">

					<form class="row mt-3 schedule-form" id="schedule-form-0">
						<div class="form-group col-md-2">
							<label for="day-of-week-0">요일</label> <select class="form-control" id="day-of-week-0" required>
								<option value="1">월</option>
								<option value="2">화</option>
								<option value="3">수</option>
								<option value="4">목</option>
								<option value="5">금</option>
							</select>
						</div>
						<div class="form-group col-md-2">
							<label for="start-period-0">시작 교시</label> <select class="form-control" id="start-period-0" required>
								<option value="1">1교시</option>
								<option value="2">2교시</option>
								<option value="3">3교시</option>
								<option value="4">4교시</option>
								<option value="5">5교시</option>
								<option value="6">6교시</option>
								<option value="7">7교시</option>
								<option value="8">8교시</option>
							</select>
						</div>
						<div class="form-group col-md-2">
							<label for="end-period-0">끝 교시</label> <select class="form-control" id="end-period-0" required>
								<option value="1">1교시</option>
								<option value="2">2교시</option>
								<option value="3">3교시</option>
								<option value="4">4교시</option>
								<option value="5">5교시</option>
								<option value="6">6교시</option>
								<option value="7">7교시</option>
								<option value="8">8교시</option>
							</select>
						</div>
						<div class="col-md-2" style="margin-top:5%">
							<button type="button" class="btn btn-danger" onclick="removeForm(this)">삭제</button>
						</div>
					</form>
				</div>

				<button type="button" class="btn btn-primary mt-3" onclick="addForm()">시간 추가</button>
			</div>-->
		</div> 



		<div class="card col-md-5 ml-2" id="right_top_card">
			<div class="card-header row">
				<div class="col-md-5 h3 pl-0 pr-0">시간표</div>

			</div>
			<div class="card-body">
				<!-- time_table start -->
				<table class="timetable table table-bordered">
					<thead>
						<tr>
							<th>교시</th>
							<th>월</th>
							<th>화</th>
							<th>수</th>
							<th>목</th>
							<th>금</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>1</td>
							<td id="monday-1" class="time-slot"></td>
							<td id="tuesday-1" class="time-slot"></td>
							<td id="wednesday-1" class="time-slot"></td>
							<td id="thursday-1" class="time-slot"></td>
							<td id="friday-1" class="time-slot"></td>
						</tr>
						<tr>
							<td>2</td>
							<td id="monday-2" class="time-slot"></td>
							<td id="tuesday-2" class="time-slot"></td>
							<td id="wednesday-2" class="time-slot"></td>
							<td id="thursday-2" class="time-slot"></td>
							<td id="friday-2" class="time-slot"></td>
						</tr>
						<tr>
							<td>3</td>
							<td id="monday-3" class="time-slot"></td>
							<td id="tuesday-3" class="time-slot"></td>
							<td id="wednesday-3" class="time-slot"></td>
							<td id="thursday-3" class="time-slot"></td>
							<td id="friday-3" class="time-slot"></td>
						</tr>
						<tr>
							<td>4</td>
							<td id="monday-4" class="time-slot"></td>
							<td id="tuesday-4" class="time-slot"></td>
							<td id="wednesday-4" class="time-slot"></td>
							<td id="thursday-4" class="time-slot"></td>
							<td id="friday-4" class="time-slot"></td>
						</tr>
						<tr>
							<td>5</td>
							<td id="monday-5" class="time-slot"></td>
							<td id="tuesday-5" class="time-slot"></td>
							<td id="wednesday-5" class="time-slot"></td>
							<td id="thursday-5" class="time-slot"></td>
							<td id="friday-5" class="time-slot"></td>
						</tr>
						<tr>
							<td>6</td>
							<td id="monday-6" class="time-slot"></td>
							<td id="tuesday-6" class="time-slot"></td>
							<td id="wednesday-6" class="time-slot"></td>
							<td id="thursday-6" class="time-slot"></td>
							<td id="friday-6" class="time-slot"></td>
						</tr>
						<tr>
							<td>7</td>
							<td id="monday-7" class="time-slot"></td>
							<td id="tuesday-7" class="time-slot"></td>
							<td id="wednesday-7" class="time-slot"></td>
							<td id="thursday-7" class="time-slot"></td>
							<td id="friday-7" class="time-slot"></td>
						</tr>
						<tr>
							<td>8</td>
							<td id="monday-8" class="time-slot"></td>
							<td id="tuesday-8" class="time-slot"></td>
							<td id="wednesday-8" class="time-slot"></td>
							<td id="thursday-8" class="time-slot"></td>
							<td id="friday-8" class="time-slot"></td>
						</tr>
					</tbody>
				</table>
				<!-- time_table end -->

			</div>


			<div id="course-list-container"></div>

		</div>

	</div>

	<br>


</div>