package com.sooRyeo.app.service;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.sooRyeo.app.domain.Lecture;
import com.sooRyeo.app.domain.Professor;
import com.sooRyeo.app.dto.StudentDTO;

public interface StudentService {
	
	// 수업리스트 보여주기 
	List<Map<String, String>> classList(int userid);
	
	// 내정보 보기
	StudentDTO getViewInfo(HttpServletRequest request);

	// 학생 비밀번호 중복확인
	JSONObject pwdDuplicateCheck(HttpServletRequest request);

	// 학생 전화번호 중복확인
	JSONObject telDuplicateCheck(HttpServletRequest request);

	// 학생 이메일 중복확인
	JSONObject emailDuplicateCheck(HttpServletRequest request);

	// 학생 정보 수정
	int student_info_edit(StudentDTO student, MultipartHttpServletRequest mrequest);

	// 수업  - 내 강의보기
	List<Lecture> getlectureList(String fk_course_seq);

	// 수업 - 이번주 강의보기
	List<Lecture> getlectureList_week(String fk_course_seq);

	ModelAndView getCourseRegisterPage(HttpServletRequest request, ModelAndView mav);
	// 수업 - 내 강의 - 과제
	List<Map<String, String>> getassignment_List(String fk_course_seq);

	// 수업 - 내 강의 - 과제 - 상세내용
	List<Map<String, String>> getassignment_detail_List(String schedule_seq_assignment);

	// 교수 이름, 교수 번호 select
	List<Professor> select_prof_info(String fk_course_seq);

	// 스케줄, 상담 테이블에 insert
	int insert__schedule_consult(String prof_id, String title, String content, String start_date, String end_date, int userid);
	
	// ajax학생 수강과목 가져와서 학점 계산하기(chart)
	String student_chart_credit(int student_id);

	// 이수한 학점이 몇점인지 알아오는 메소드
	int credit_point(int student_id);

	// 학적변경테이블(tbl_student_status_change)에 졸업신청을 insert 하는 메소드 
	int application_status_change(int student_id, int status_num);

	// 현재 학적변경을 신청한 상태인지 알아오는 메소드
	String getApplication_status(int student_id);




	

	
	
	
}
