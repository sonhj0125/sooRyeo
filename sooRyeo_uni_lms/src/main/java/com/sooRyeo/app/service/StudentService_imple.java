package com.sooRyeo.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.sooRyeo.app.domain.*;
import com.sooRyeo.app.model.LectureDao;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.sooRyeo.app.model.DepartmentDao;
import com.sooRyeo.app.model.StudentDao;
import com.sooRyeo.app.mongo.entity.AlertLecture;
import com.sooRyeo.app.mongo.repository.AlertLectureRepository;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sooRyeo.app.common.AES256;
import com.sooRyeo.app.common.FileManager;
import com.sooRyeo.app.common.Sha256;
import com.sooRyeo.app.dto.AssignmentSubmitDTO;
import com.sooRyeo.app.dto.StudentDTO;


@Service
public class StudentService_imple implements StudentService {
	
	@Autowired
	private StudentDao studentDao;

	@Autowired
	private LectureDao lectureDao;
	
    @Autowired
    private AES256 aES256;
    
	@Autowired
	private FileManager fileManager;
	
	@Autowired
	private DepartmentDao departmentDao;
	
	
    @Autowired
    private AlertLectureRepository alertLectureRepository;

	
	// 내정보 보기
	@Override
	public StudentDTO getViewInfo(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		Student loginuser = (Student) session.getAttribute("loginuser");
		
		
		StudentDTO member_student = new StudentDTO();
		member_student.setName(loginuser.getName());					// 이름
		member_student.setPwd(loginuser.getPwd());						// 비번
		member_student.setGrade(loginuser.getGrade());					// 학년
		member_student.setStatus(loginuser.getStatus());				// 학적
		member_student.setBirthday(loginuser.getBirthday());  			// 생년월일
		member_student.setTel(loginuser.getTel()); 						// 연락처
		member_student.setEmail(loginuser.getEmail()); 					// 이메일
		member_student.setPostcode(loginuser.getPostcode());			// 우편번호
		member_student.setAddress(loginuser.getAddress()); 				// 주소
		member_student.setDetailAddress(loginuser.getDetailAddress());	// 상세주소
		member_student.setExtraAddress(loginuser.getExtraAddress());	// 추가주소
		
		// 학과명 가져오기
		String d_name = studentDao.select_department(loginuser.getStudent_id());
		member_student.setDepartment_name(d_name);
		
		
		return member_student;
		
	} // end of public void getViewInfo

	
	
	
	// 학생 비밀번호 중복확인
	@Override
	public JSONObject pwdDuplicateCheck(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		Student loginuser = (Student)session.getAttribute("loginuser");
		
		String student_id = Integer.toString(loginuser.getStudent_id());
		
		String pwd = request.getParameter("pwd");		
		pwd = Sha256.encrypt(pwd);
		
		Map<String, String> paraMap = new HashMap<>(); 
		
		paraMap.put("student_id", student_id);
		paraMap.put("pwd", pwd);
		
		int n = 0;
		try {
			n = studentDao.pwdDuplicateCheck(paraMap);
		} catch (Throwable e) {
			e.printStackTrace();
		}


		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n); // {"n":1}
		
		return jsonObj;
		
	} // end of public JSONObject pwdDuplicateCheck
	
	
	// 학생 전화번호 중복확인
	@Override
	public JSONObject telDuplicateCheck(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		Student loginuser = (Student)session.getAttribute("loginuser");
		
		String student_id = Integer.toString(loginuser.getStudent_id());
		
		String tel = request.getParameter("tel");
		try {
			tel = aES256.encrypt(tel);
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		}
		
		Map<String, String> paraMap = new HashMap<>(); 
		
		paraMap.put("student_id", student_id);
		paraMap.put("tel", tel);
		
		
		int n = 0;
		try {
			n = studentDao.telDuplicateCheck(paraMap);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n); // {"n":1}
		
		return jsonObj;
	}


	// 이메일 중복확인
	@Override
	public JSONObject emailDuplicateCheck(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		Student loginuser = (Student)session.getAttribute("loginuser");
		
		String student_id = Integer.toString(loginuser.getStudent_id());
		
		String email = request.getParameter("email");
		try {
			email = aES256.encrypt(email);
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		}
		
		Map<String, String> paraMap = new HashMap<>(); 
		
		paraMap.put("student_id", student_id);
		paraMap.put("email", email);
		
		
		int n = 0;
		try {
			n = studentDao.emailDuplicateCheck(paraMap);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n); // {"n":1}
		
		return jsonObj;
	}


	// 학생정보 수정
	@Override
	public int student_info_edit(StudentDTO student,  MultipartHttpServletRequest mrequest) {
		
		int n1 = 1;
		int n2 = 0;
			
		HttpSession session = mrequest.getSession();
		Student loginuser = (Student)session.getAttribute("loginuser");
		

		String student_id = Integer.toString(loginuser.getStudent_id());
		String pwd = mrequest.getParameter("pwd");
		pwd = Sha256.encrypt(pwd);
		
		// 주소
		String postcode = mrequest.getParameter("postcode");
		String address = mrequest.getParameter("address");
		String detailAddress = mrequest.getParameter("detailAddress"); 
		String extraAddress = mrequest.getParameter("extraAddress");	
		
		String email = mrequest.getParameter("email");
		
		System.out.println("~~~확인용 email : " + email);
		
		
		try {
			email = aES256.encrypt(email);
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		}
	
		
		
		String tel = mrequest.getParameter("tel");
		
		try {
			tel = aES256.encrypt(tel);
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		}
		
		System.out.println("~~~확인용 tel : " + tel);
		
		Map<String, String> paraMap = new HashMap<>();
		
		paraMap.put("student_id", student_id);
		paraMap.put("pwd", pwd);
		paraMap.put("postcode", postcode);
		paraMap.put("address", address);
		paraMap.put("detailAddress", detailAddress);
		paraMap.put("extraAddress", extraAddress);
		paraMap.put("email", email);
		paraMap.put("tel", tel);
		
		String fileName = studentDao.select_file_name(paraMap);
		
	
        System.out.println("확인용 fileName : " + fileName);
        
        if (fileName != null && !"".equals(fileName)) {
            
            String root = session.getServletContext().getRealPath("/");
            
            String path = root+"resources"+File.separator+"files";
   
            paraMap.put("path", path); // 삭제해야할 파일이 저장된 경로
            paraMap.put("fileName", fileName); // 삭제해야할 파일이 저장된 경로
            
            n1 = studentDao.delFilename(paraMap.get("student_id"));
            System.out.println("n1: " + n1);
            
            if (n1 == 1) {
                path = paraMap.get("path");
                fileName = paraMap.get("fileName");
                
                if (fileName != null && !"".equals(fileName)) {
                    try {
                        fileManager.doFileDelete(fileName, path);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } // end of if(n1 == 1)
        } // end of if(fileName != null && !"".equals(fileName))
	   
		
		// === 파일첨부가 된 글이라면 글 삭제시 먼저 첨부파일을 삭제해주어야 한다. 끝 === //
		/////////////////////////////////////////////////////////////////////////////////

		
		
		MultipartFile attach = student != null ? student.getAttach() : null;
		
		String root = session.getServletContext().getRealPath("/");
	     
	    // System.out.println("~~~ 확인용 webapp 의 절대경로 => " + root);
	    // ~~~ 확인용 webapp 의 절대경로 => C:\NCS\workspace_spring_framework\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\sooRyeo_uni_lms\
	     
	    String path = root+"resources"+File.separator+"files";
	    /*    File.separator 는 운영체제에서 사용하는 폴더와 파일의 구분자이다.
	          운영체제가 Windows 이라면 File.separator 는  "\" 이고,
	          운영체제가 UNIX, Linux, 매킨토시(맥) 이라면  File.separator 는 "/" 이다. 
	    */
	    // path 가 첨부파일이 저장될 WAS(톰캣)의 폴더가 된다.
	    // System.out.println("~~~ 확인용 path => " + path);
	    // ~~~ 확인용 path => C:\NCS\workspace_spring_framework\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\sooRyeo_uni_lms\resources\files
		/*
		  2. 파일첨부를 위한 변수의 설정 및 값을 초기화 한 후 파일 올리기
	    */
	    String newFileName = "";
	    // WAS(톰캣)의 디스크에 저장될 파일명
	     
	    byte[] bytes = null;
	    // 첨부 파일의 내용물을 담은 것
	    
	    String img_name = "";
	    
	    // System.out.println("~~~~~ 확인용 attach => " + attach);
	    // ~~~~~ 확인용 attach => MultipartFile[field="attach", filename=60.jpg, contentType=image/jpeg, size=237823]
	    
	    if(attach != null) {	    
		    try {
		    	bytes = attach.getBytes();
		    	// 첨부파일의 내용물을 읽어오는 것
				
				String originalFilename =  attach.getOriginalFilename();
				// attach.getOriginalFilename() 이 첨부파일명의 파일명(예: 강아지.png) 이다.
				
				// System.out.println("~~~ 확인용 originalFilename => " + originalFilename); 
		        // ~~~ 확인용 originalFilename => 60.jpg
				
				newFileName = fileManager.doFileUpload(bytes, originalFilename, path);
				// 첨부되어진 파일을 업로드 하는 것이다.
				
				// System.out.println("~~~ 확인용 newFileName => " + newFileName);
				// ~~~ 확인용 newFileName => 202407051559321925805007091400.jpg
				
				/*
		           3. BoardVO boardvo 에 fileName 값과 orgFilename 값과 fileSize 값을 넣어주기  
				*/
				img_name = newFileName;
				   // WAS(톰캣)에 저장된 파일명(2024062712074811660790417300.xlsx) 이다.	
				   
			} catch (Exception e) {
				e.printStackTrace();
			}			
	    };// end of if(attach != null) 	
		
	    // System.out.println("확인용 img_name : " + img_name);
	    // 확인용 img_name : 202407051559321925805007091400.jpg
		

		paraMap.put("img_name", img_name);


		
		try {
			n2 = studentDao.student_info_edit(paraMap);
			// System.out.println("n2: " + n2);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		if(n1*n2 == 1) {// 잘 수정되었다면 세션에 정보를 덧씌우기하기 위한 용도
			
			loginuser.updateinfo(paraMap, aES256); // student 도메인 데이터 수정
			
		}
		
		return n1*n2;
	}


	// 내수업리스트
	@Override
	public StudentTimeTable classList(int userid) {
		
		StudentTimeTable classList = studentDao.classList(userid);
		return classList;
	}
	
	
	//todo : getlectureList, getlectureList_week 모두 lectureDAO로 옮기기
	@Override
	public ModelAndView getCourseLecturePage(HttpServletRequest request, ModelAndView mav, String fkCourseSeq) {
		HttpSession session =  request.getSession();
		Student student =  (Student) session.getAttribute("loginuser");
		
		List<Lecture> lectureList = studentDao.getlectureList(fkCourseSeq);
		List<Attendance> addendenceList  = lectureDao.getAttendance(fkCourseSeq, student.getStudent_id());
		Map<Integer, Boolean> attendanceMap = new HashMap<>();
		for(Attendance a : addendenceList) {
			if(a.isAttended()) {
				attendanceMap.put(a.getLecture_seq(), true);
			}
		}

		List<Professor> professor_info = studentDao.select_prof_info(fkCourseSeq);

		// 수업 - 이번주 강의보기
		List<Lecture> lectureList_week = studentDao.getlectureList_week(fkCourseSeq);

		mav.addObject("professor_info", professor_info);
		mav.addObject("attendanceMap", attendanceMap);

		mav.addObject("lectureList", lectureList);
		mav.addObject("fk_course_seq", fkCourseSeq);
		mav.addObject("lectureList_week", lectureList_week);

		mav.setViewName("myLecture");

		return mav;
	}


	// 수업  - 내 강의보기
	@Override
	public List<Lecture> getlectureList(String fk_course_seq) {
		
		List<Lecture> lectureList = studentDao.getlectureList(fk_course_seq);

		return lectureList;
	} // end of public List<Lecture> getlectureList



	// 수업 - 이번주 강의보기
	@Override
	public List<Lecture> getlectureList_week(String fk_course_seq) {
		
		List<Lecture> lectureList_week = studentDao.getlectureList_week(fk_course_seq);
		
		return lectureList_week;
		
	} // end of public List<Lecture> getlectureList_week




	@Override
	public ModelAndView getCourseRegisterPage(HttpServletRequest request, ModelAndView mav) {
		mav.addObject("departments", departmentDao.departmentList_select());
		mav.setViewName("studentCourseRegister");
		return mav;
	}
	
	
	// 수업 - 내 강의 - 과제
	@Override
	public List<Map<String, String>> getassignment_List(String fk_course_seq, int userid) {
		
		List<Map<String, String>> assignment_List = studentDao.getassignment_List(fk_course_seq, userid);
		
		return assignment_List;
		
	} // end of public List<Map<String, String>> getassignment_List


	
	
	// 수업 - 내 강의 - 과제 - 상세내용1
	@Override
	public Map<String, Object> getassignment_detail_1(String schedule_seq_assignment) {
		
		Map<String, Object> assignment_detail_1 = studentDao.getassignment_detail_1(schedule_seq_assignment);
		
		return assignment_detail_1;
		
	} // end of public Map<String, Object> getassignment_detail_1

	
	

	// 수업 - 내 강의 - 과제 - 상세내용2
	@Override
	public Map<String, Object> getassignment_detail_2(String schedule_seq_assignment, int userid) {
		
		Map<String, Object> assignment_detail_2 = studentDao.getassignment_detail_2(schedule_seq_assignment, userid);
		
		return assignment_detail_2;
		
	} // end of public Map<String, Object> getassignment_detail_2


	
	
	// 과제제출
	@Override
	public int addComment(AssignmentSubmitDTO asdto) {
		
		int n = 0;
		
		n = studentDao.addComment(asdto);

		return n;
		
	} // end of public int addComment
	
	

	// 교수 이름, 교수 번호 select
	@Override
	public List<Professor> select_prof_info(String fk_course_seq) {
		List<Professor> prof_info = studentDao.select_prof_info(fk_course_seq);
		return prof_info;
	}



	// 스케줄, 상담 테이블에 insert
	@Override
	public int insert__schedule_consult(String prof_id, String title, String content, String start_date, String end_date, int userid) {
		
		int n = studentDao.insert__schedule_consult(prof_id, title, content, start_date, end_date, userid);
		return n;
	}

	
	// 수업 - 강의 한개 제목, 내용 select
	@Override
	public Map<String, String> classPlay_One(String lecture_seq) {
		Map<String, String> classOne = studentDao.classPlay_One(lecture_seq);
		return classOne;
	}

	
	// 출석 테이블에 내가 수강한 수업이 insert 되어진 값이 있는지 알아오기 위함
	@Override
	public String select_tbl_attendance(String lecture_seq, int userid) {
		String fk_lecture_seq = studentDao.select_tbl_attendance(lecture_seq, userid);
		return fk_lecture_seq;
	}


	// 처음 동영상을 재생한 경우 tbl_attendance 에 insert
	@Override
	public int insert_tbl_attendance(String play_time, String lecture_seq, int userid) {
		int n = studentDao.insert_tbl_attendance(play_time, lecture_seq, userid);
		return n;
	}

	
	@Override
	public int select_play_time_lecture_time(String play_time, String lecture_seq, int userid) {
		int i = studentDao.select_play_time_lecture_time(play_time, lecture_seq, userid);
		return i;
	}
	
	
	// 동영상 재생이력이 있는 경우 재생한 경우 tbl_attendance play_time 컬럼 update
	@Override
	public int update_tbl_attendance(String play_time, String lecture_seq, int userid) {

		int n1 = studentDao.update_tbl_attendance(play_time, lecture_seq, userid);
		System.out.println("service 에서 n1 체크 => " +  n1);
		return n1;
	}


	@Override
	public int update_tbl_attendance_isAttended(String lecture_seq, int userid) {
		
		int n2 = studentDao.update_tbl_attendance_isAttended(lecture_seq, userid);
		return n2;
	}









	// 과제 제출 내용보기
	@Override
	public Map<String, Object> getreadComment(String fk_schedule_seq_assignment, int userid) {
		
		Map<String, Object> asdto = studentDao.getreadComment(fk_schedule_seq_assignment, userid);
		
		return asdto;
		
	} // end of public List<AssignmentSubmit> getreadComment



	// 파일첨부가 되어진 과제에서 서버에 업로드되어진 파일명 조회
	@Override
	public AssignmentSubmitDTO getCommentOne(String assignment_submit_seq) {
		
		AssignmentSubmitDTO asdto = studentDao.getCommentOne(assignment_submit_seq);
		
		return asdto;
		
	} // end of public AssignmentSubmitDTO getCommentOne




	// 통계용 총 학점 가져오기
	@Override
	public String student_chart_credit(int student_id, int department_seq) {
		
		Map<String, String> myRequiredCredit = studentDao.student_myRequiredCredit(student_id, department_seq);
		Map<String, String> yourRequiredCredit = studentDao.student_yourRequiredCredit(student_id, department_seq);
		Map<String, String> UnrequiredCredit = studentDao.student_UnrequiredCredit(student_id);
		Map<String, String> LiberalCredit = studentDao.student_LiberalCredit(student_id);
		
		JsonArray jsonArr = new JsonArray();	
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty("total_myRequired_credit", myRequiredCredit.get("total_myRequired_credit"));
		jsonObj.addProperty("total_yourRequired_credit", yourRequiredCredit.get("total_yourRequired_credit"));
		jsonObj.addProperty("total_Unrequired_credit", UnrequiredCredit.get("total_Unrequired_credit"));
		jsonObj.addProperty("total_Liberal_credit", LiberalCredit.get("total_Liberal_credit"));
		
            
		jsonArr.add(jsonObj);
		      
		return new Gson().toJson(jsonArr);
	}


	// 출석 현황 조회
	@Override
	public List<Map<String, Object>> attendanceList(int userid, String name) {
		
		List<Map<String, Object>> attendanceList = studentDao.attendanceList(userid, name);
		
		return attendanceList;
		
	} // end of public List<Map<String, Object>> attendanceList



	
	// 수업명 가져오기
	@Override
	public List<Curriculum> lectureList() {
		
		List<Curriculum> lectureList = studentDao.lectureList();
		
		return lectureList;
		
	} // end of public List<String> lectureList




	// 이수한 학점이 몇점인지 알아오는 메소드
	@Override
	public int credit_point(int student_id) {
		int credit_point = studentDao.credit_point(student_id);
		return credit_point;
	}

	// 학적변경테이블(tbl_student_status_change)에 졸업신청을 insert 하는 메소드 
	@Override
	public int application_status_change(int student_id, int status_num) {
		int n = studentDao.application_status_change(student_id, status_num);
		return n;
	}


	// 현재 학적변경을 신청한 상태인지 알아오는 메소드
	@Override
	public String getApplication_status(int student_id) {
		String application_status = studentDao.getApplication_status(student_id);
		return application_status;
	}


	// 오늘의 수업만을 불러오는 메소드
	@Override
	public List<TodayLecture> getToday_lec(int student_id) {
		List<TodayLecture> today_lec = studentDao.getToday_lec(student_id);
		return today_lec;
	}

	@Override
	public Pager<Announcement> getAnnouncement(int currentPage) {
		Pager<Announcement> announcementList = studentDao.getAnnouncement(currentPage);
		return announcementList;
	}



	// 출석 현황 조회 - Excel
	@Override
	public void employeeList_to_Excel(String name, Model model, HttpServletRequest request) {
		
		// === 조회결과물인 empList 를 가지고 엑셀 시트 생성하기 ===
	    // 시트를 생성하고, 행을 생성하고, 셀을 생성하고, 셀안에 내용을 넣어주면 된다.
		SXSSFWorkbook workbook = new SXSSFWorkbook();	// workbook은 엑셀파일을 말함
		
		// 시트 생성
	    SXSSFSheet sheet = workbook.createSheet("출석현황");
		
	    // 학번, 수업명, 강의명, 출석일자
	    // 시트 열 너비 설정
	    sheet.setColumnWidth(0, 4000);
	    sheet.setColumnWidth(1, 6000);
	    sheet.setColumnWidth(2, 8000);
	    sheet.setColumnWidth(3, 6000);
	    
	    // 행의 위치를 나타내는 변수 
	    int rowLocation = 0;
	    
		////////////////////////////////////////////////////////////////////////////////////////
		// CellStyle 정렬하기(Alignment)
		// CellStyle 객체를 생성하여 Alignment 세팅하는 메소드를 호출해서 인자값을 넣어준다.
		// 아래는 HorizontalAlignment(가로)와 VerticalAlignment(세로)를 모두 가운데 정렬 시켰다.
	    
	    CellStyle mergeRowStyle =  workbook.createCellStyle();
	    mergeRowStyle.setAlignment(HorizontalAlignment.CENTER);
	    mergeRowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	    // import org.apache.poi.ss.usermodel.VerticalAlignment 으로 해야함.
	    
	    CellStyle headerStyle = workbook.createCellStyle();
	    headerStyle.setAlignment(HorizontalAlignment.CENTER);
	    headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	    
	    
	    // CellStyle 배경색(ForegroundColor)만들기
        // setFillForegroundColor 메소드에 IndexedColors Enum인자를 사용한다.
        // setFillPattern은 해당 색을 어떤 패턴으로 입힐지를 정한다.
	    mergeRowStyle.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex());	// IndexedColors.DARK_BLUE.getIndex() 는 색상(남색)의 인덱스값을 리턴시켜준다.
	    mergeRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);				// SOLID_FOREGROUND은 실선
	    
	    headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());	// IndexedColors.LIGHT_YELLOW.getIndex() 는 연한노랑의 인덱스값을 리턴시켜준다.
	    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    
	    
	    // CellStyle 천단위 쉼표, 금액
        CellStyle moneyStyle = workbook.createCellStyle();
        moneyStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
        
        
        // Cell 폰트(Font) 설정하기
        // 폰트 적용을 위해 POI 라이브러리의 Font 객체를 생성해준다.
        // 해당 객체의 세터를 사용해 폰트를 설정해준다. 대표적으로 글씨체, 크기, 색상, 굵기만 설정한다.
        // 이후 CellStyle의 setFont 메소드를 사용해 인자로 폰트를 넣어준다.
        Font mergeRowFont = workbook.createFont(); // import org.apache.poi.ss.usermodel.Font; 으로 한다.
        mergeRowFont.setFontName("나눔고딕");
        mergeRowFont.setFontHeight((short)500);
        mergeRowFont.setColor(IndexedColors.WHITE.getIndex());
        mergeRowFont.setBold(true);
        
        mergeRowStyle.setFont(mergeRowFont);	// row스타일에 폰트 스타일 넣음
        
        
        // CellStyle 테두리 Border
        // 테두리는 각 셀마다 상하좌우 모두 설정해준다.
        // setBorderTop, Bottom, Left, Right 메소드와 인자로 POI라이브러리의 BorderStyle 인자를 넣어서 적용한다.
        headerStyle.setBorderTop(BorderStyle.THICK);
	    headerStyle.setBorderBottom(BorderStyle.THICK);
	    headerStyle.setBorderLeft(BorderStyle.THIN);
	    headerStyle.setBorderRight(BorderStyle.THIN);
	    
	    
	    // Cell Merge 셀 병합시키기
        /* 
          	셀병합은 시트의 addMergeRegion 메소드에 CellRangeAddress 객체를 인자로 하여 병합시킨다.
           	CellRangeAddress 생성자의 인자로(시작 행, 끝 행, 시작 열, 끝 열) 순서대로 넣어서 병합시킬 범위를 정한다. 배열처럼 시작은 0부터이다.  
        */
        // 병합할 행 만들기
	    Row mergeRow = sheet.createRow(rowLocation);  // 엑셀에서 행의 시작은 0 부터 시작한다. 
        
	    
	    // 병합할 행에 "우리회사 사원정보" 로 셀을 만들어 셀에 스타일을 주기
    	for(int i=0; i<3; i++) {
	    		
			Cell cell = mergeRow.createCell(i);
			
			cell.setCellStyle(mergeRowStyle);
			cell.setCellValue("출석현황");
			
    	}// end of for-------------------------
    
    
	    // 셀 병합하기
	    sheet.addMergedRegion(new CellRangeAddress(rowLocation, rowLocation, 0, 3)); // 시작 행, 끝 행, 시작 열, 끝 열 

	    ///////////////////////////////////////////////////////////////////////////////////////////////
	    
	    
	    // 헤더 행 생성
        Row headerRow = sheet.createRow(++rowLocation); // 엑셀에서 행의 시작은 0 부터 시작한다.
                                                        // ++rowLocation는 전위연산자임. 
        
        // 해당 행의 첫번째 열 셀 생성
        Cell headerCell = headerRow.createCell(0); // 엑셀에서 열의 시작은 0 부터 시작한다.
        headerCell.setCellValue("학번");
        headerCell.setCellStyle(headerStyle);
        
        // 해당 행의 두번째 열 셀 생성
        headerCell = headerRow.createCell(1);
        headerCell.setCellValue("수업명");
        headerCell.setCellStyle(headerStyle);
        
        // 해당 행의 세번째 열 셀 생성
        headerCell = headerRow.createCell(2);
        headerCell.setCellValue("강의명");
        headerCell.setCellStyle(headerStyle);
        
        // 해당 행의 네번째 열 셀 생성
        headerCell = headerRow.createCell(3);
        headerCell.setCellValue("출석일자");
        headerCell.setCellStyle(headerStyle);
        
        
        // ==== 출석현황 내용에 해당하는 행 및 셀 생성하기 ==== //
        Row bodyRow = null;
        Cell bodyCell = null;
        
        HttpSession session = request.getSession();
		Student loginuser = (Student)session.getAttribute("loginuser");
		int userid = loginuser.getStudent_id();
		
        List<Map<String, Object>> attendanceList = attendanceList(userid, name);
      
	    for(int i=0; i<attendanceList.size(); i++) {
	    	
	    	Map<String, Object> attendanceListMap = attendanceList.get(i);
	    	
	    	// 행 생성
	        bodyRow = sheet.createRow(i + (rowLocation+1));
	    	
	        // 데이터 학번 표시
            bodyCell = bodyRow.createCell(0);
            bodyCell.setCellValue((String) attendanceListMap.get("fk_student_id")); 
           
            // 데이터 수업명 표시
            bodyCell = bodyRow.createCell(1);
            bodyCell.setCellValue((String) attendanceListMap.get("name")); 
                      
            // 데이터 강의명 표시
            bodyCell = bodyRow.createCell(2);
            bodyCell.setCellValue((String) attendanceListMap.get("lecture_title")); 
           
            // 데이터 출석일자 표시
            bodyCell = bodyRow.createCell(3);
            if(attendanceListMap.get("attended_date") == " ") {
            	bodyCell.setCellValue("출석 진행중");
            }
            else {
            	bodyCell.setCellValue((String) attendanceListMap.get("attended_date"));
            }
             
           
	    } // end of for

        model.addAttribute("locale", Locale.KOREA);
        model.addAttribute("workbook", workbook);
        model.addAttribute("workbookName", "출석현황");
		
	} // end of public void employeeList_to_Excel




	// 하이차트 - 학생이 듣고있는 수업명 가져오는 메소드
	@Override
	public List<Curriculum> Curriculum_nameList(int student_id) {
		
		List<Curriculum> Curriculum_nameList = studentDao.Curriculum_nameList(student_id);
		
		return Curriculum_nameList;
		
	} // end of public List<Curriculum> Curriculum_nameList



	// 학생 대쉬보드 - 하이차트 - 수강중인 과목 출석률 
	@Override
	public Map<String, Object> myAttendance_byCategoryJSON(int student_id, String name) {
		
		Map<String, Object> myAttendance_byCategoryJSON = studentDao.myAttendance_byCategoryJSON(student_id, name);
		
		return myAttendance_byCategoryJSON;
		
	} // end of public List<Map<String, Object>> myAttendance_byCategoryJSON



	// 학생 - 성적 취득현황
	@Override
	public List<Map<String, Object>> Acquisition_status(int student_id) {
		
		List<Map<String, Object>> Acquisition_status = studentDao.Acquisition_status(student_id);
		
		return Acquisition_status;
		
	} // end of public List<Map<String, Object>> Acquisition_status



	// 학생 - 성적 취득현황JSON
	@Override
	public List<Map<String, Object>> Acquisition_status_JSON(String semester, int student_id) {
		
		List<Map<String, Object>> Acquisition_status_JSON = studentDao.Acquisition_status_JSON(semester, student_id);
		
		return Acquisition_status_JSON;
		
	} // end of public List<Map<String, Object>> Acquisition_status_JSON




	// 학기 별 개강과목JSON
	@Override
	public StudentTimeTable classListJSON(String semester, int student_id) {
		
		StudentTimeTable classListJSON = studentDao.classListJSON(semester, student_id);
		
		return classListJSON;
		
	} // end of public StudentTimeTable classListJSON

	@Override
	public List<AlertLecture> getAlertLecture(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		Student loginuser = (Student) session.getAttribute("loginuser");
		
		List<AlertLecture> getAlert = alertLectureRepository.findAllByStudentId(loginuser.getStudent_id());
		
		if(getAlert.size() != 0) {
			return getAlert;
		}
		else {
			return null;
		}
		
	}



	@Override
	public AlertLecture deleteAlertLecture(String id) {
		
		alertLectureRepository.deleteById(id);
		
		AlertLecture checkId = alertLectureRepository.findById(id).orElse(null);
		
		return checkId;
	}


	

	
	
	
	
	
}
