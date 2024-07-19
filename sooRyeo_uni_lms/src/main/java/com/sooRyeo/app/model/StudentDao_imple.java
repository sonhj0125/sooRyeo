package com.sooRyeo.app.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.sooRyeo.app.domain.AssignmentSubmit;
import com.sooRyeo.app.domain.Lecture;
import com.sooRyeo.app.domain.Professor;
import com.sooRyeo.app.domain.Student;
import com.sooRyeo.app.dto.AssignmentSubmitDTO;
import com.sooRyeo.app.dto.LoginDTO;
import com.sooRyeo.app.dto.StudentDTO;

@Repository
public class StudentDao_imple implements StudentDao {
	
	@Autowired
	@Qualifier("sqlsession") // 이름이 같은것을 주입
	private SqlSessionTemplate sqlSession;
	
	// 학생 로그인
	@Override
	public Student selectStudent(LoginDTO loginDTO) {
		
		Student student = sqlSession.selectOne("student.selectStudent", loginDTO);
		
		return student;
		
	} // end of public Student selectStudent

	// 내정보 보기
	@Override
	public StudentDTO getViewInfo(String login_userid) {
		
		StudentDTO member_student = sqlSession.selectOne("student.getViewInfo", login_userid);
		
		return member_student;
		
	} // end of public StudentDTO getViewInfo

	// 학과명 가져오기
	@Override
	public String select_department(Integer student_id) {
		
		String d_name = sqlSession.selectOne("student.select_department", student_id);
		
		return d_name;
	} // end of public String select_department


	// 학생 비밀번호 중복확인
	@Override
	public int pwdDuplicateCheck(Map<String, String> paraMap) {
		
		int n = sqlSession.selectOne("student.pwdDuplicateCheck", paraMap);
		
		return n;
		
	} // end of public int pwdDuplicateCheck

	
	// 학생 전화번호 중복확인
	@Override
	public int telDuplicateCheck(Map<String, String> paraMap) {
		
		int n = sqlSession.selectOne("student.telDuplicateCheck", paraMap);
		
		return n;
		
	} // end of public int telDuplicateCheck

	
	// 학생 이메일 중복확인
	@Override
	public int emailDuplicateCheck(Map<String, String> paraMap) {
		
		int n = sqlSession.selectOne("student.emailDuplicateCheck", paraMap);
		
		return n;
		
	} // end of public int emailDuplicateCheck

	
	
	

	// 계정에 파일이 있는지 확인
	@Override
	public String select_file_name(Map<String, String> paraMap) {
		
		String fileName = sqlSession.selectOne("student.select_file_name", paraMap);
		
		return fileName;
		
	} // end of public StudentDTO select_file_name


	// 계정에 기존 파일 삭제
	@Override
	public int delFilename(String student_id) {
		
		int n = sqlSession.update("student.delFilename", student_id);
		
		return n;
		
	} // end of public int delFilename


	// 학생 정보 수정
	@Override
	public int student_info_edit(Map<String, String> paraMap) {
		
		int n = sqlSession.update("student.student_info_edit", paraMap);
		
		return n;
		
	} // end of public int student_info_edit


	// 수업리스트 보여주기
	@Override
	public List<Map<String, String>> classList(int userid) {
		List<Map<String, String>> classList = sqlSession.selectList("student.classList", userid);
		return classList;
	}
	


	// 수업  - 내 강의보기
	@Override
	public List<Lecture> getlectureList(String fk_course_seq) {
		
		List<Lecture> lectureList = sqlSession.selectList("student.getlectureList", fk_course_seq);
		
		return lectureList;
		
	} // end of public List<Lecture> getlectureList

	
	// 수업 - 이번주 강의보기
	@Override
	public List<Lecture> getlectureList_week(String fk_course_seq) {
		
		List<Lecture> lectureList_week = sqlSession.selectList("student.getlectureList_week", fk_course_seq);
		
		return lectureList_week;
		
	} // end of public List<Lecture> getlectureList_week

	
	// 수업 - 내 강의 - 과제
	@Override
	public List<Map<String, String>> getassignment_List(String fk_course_seq, int userid) {
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("fk_course_seq", fk_course_seq);
		paraMap.put("userid", String.valueOf(userid));
		
		List<Map<String, String>> assignment_List = sqlSession.selectList("student.getassignment_List", paraMap);
		
		return assignment_List;
		
	} // end of public List<Map<String, String>> getassignment_List

	
	
	// 수업 - 내 강의 - 과제 - 상세내용1
	@Override
	public Map<String, Object> getassignment_detail_1(String schedule_seq_assignment) {
		
		Map<String, Object> assignment_detail_1 = sqlSession.selectOne("student.getassignment_detail_1", schedule_seq_assignment);
		
		return assignment_detail_1;
		
	} // end of public Map<String, Object> getassignment_detail_1
	
	
	
	
	// 수업 - 내 강의 - 과제 - 상세내용2
	@Override
	public Map<String, Object> getassignment_detail_2(String schedule_seq_assignment, int userid) {
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("schedule_seq_assignment", schedule_seq_assignment);
		paraMap.put("userid", String.valueOf(userid));
		
		Map<String, Object> assignment_detail_2 = sqlSession.selectOne("student.getassignment_detail_2", paraMap);
		
		return assignment_detail_2;
		
	} // end of public Map<String, Object> getassignment_detail_2

	
	// 과제제출
	@Override
	public int addComment(AssignmentSubmitDTO asdto) {
		
		int n = sqlSession.insert("student.addComment", asdto);
		
		return n;
	} // end of public int addComment
	
	
	// 교수번호, 교수 이름  가져오기
	@Override
	public List<Professor> select_prof_info(String fk_course_seq) {
		List<Professor> prof_info = sqlSession.selectList("student.select_prof_info", fk_course_seq);
		return prof_info;
	}
	

	// 스케줄, 상담 테이블에 insert
	@Override
	public int insert__schedule_consult(String prof_id, String title, String content, String start_date, String end_date, int userid) {
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("title", title);
		paraMap.put("start_date", start_date);
		paraMap.put("end_date", end_date);
		
		sqlSession.insert("student.insert_tbl_schedule", paraMap);
		
		System.out.println(paraMap.get("schedule_seq"));
		String schedule_seq = (String) paraMap.get("schedule_seq");
		
		paraMap.put("content", content);
		paraMap.put("prof_id", prof_id);
		paraMap.put("userid",  String.valueOf(userid));
		
		int n = sqlSession.insert("student.insert_tbl_consult", paraMap);
		
		return n;
	}

	/////////////////////////////////////////////////////////////////////////////
	// 통계용 총 학점 가져오기
	// 전공필수
	@Override
	public Map<String, String> student_RequiredCredit(int student_id) {
		
		Map<String, String> student_RequiredCredit = sqlSession.selectOne("student.student_RequiredCredit", student_id);
		
		return student_RequiredCredit;
	}
	// 전공선택
	@Override
	public Map<String, String> student_UnrequiredCredit(int student_id) {
		
		Map<String, String> student_UnrequiredCredit = sqlSession.selectOne("student.student_UnrequiredCredit", student_id);
		
		return student_UnrequiredCredit;
	}
	// 교양
	@Override
	public Map<String, String> student_LiberalCredit(int student_id) {
		
		Map<String, String> student_LiberalCredit = sqlSession.selectOne("student.student_LiberalCredit", student_id);
		
		return student_LiberalCredit;
	}
	
	
	/////////////////////////////////////////////////////////////////////////////

	// 이수한 학점이 몇점인지 알아오는 메소드
	@Override
	public int credit_point(int student_id) {
		int credit_point = sqlSession.selectOne("student.credit_point", student_id);
		return credit_point;
	}

	// 학적변경테이블(tbl_student_status_change)에 졸업신청을 insert 하는 메소드 
	@Override
	public int application_status_change(int student_id, int status_num) {
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("student_id",student_id);
		paraMap.put("status_num",status_num);
		
		int n = sqlSession.insert("student.application_status_change", paraMap);
		return n;
	}

	@Override
	public String getApplication_status(int student_id) {
		String Application_status = sqlSession.selectOne("student.getApplication_status", student_id);
		return Application_status;
	}

	
	// 과제 제출 내용보기
	@Override
	public Map<String, Object> getreadComment(String fk_schedule_seq_assignment, int userid) {
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("fk_schedule_seq_assignment", fk_schedule_seq_assignment);
		paraMap.put("userid", String.valueOf(userid));
		
		Map<String, Object> asdto = sqlSession.selectOne("student.getreadComment", paraMap);
		
		return asdto;
		
	} // end of public List<AssignmentSubmit> getreadComment

	
	// 파일첨부가 되어진 과제에서 서버에 업로드되어진 파일명 조회
	@Override
	public AssignmentSubmitDTO getCommentOne(String assignment_submit_seq) {
		
		AssignmentSubmitDTO asdto = sqlSession.selectOne("student.getCommentOne", assignment_submit_seq);
		
		return asdto;
		
	} // end of public AssignmentSubmitDTO getCommentOne









	
	
}
