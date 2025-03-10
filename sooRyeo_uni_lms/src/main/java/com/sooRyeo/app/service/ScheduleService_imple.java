package com.sooRyeo.app.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sooRyeo.app.common.AES256;
import com.sooRyeo.app.domain.*;
import com.sooRyeo.app.dto.ConsultApprovalDto;
import com.sooRyeo.app.dto.ConsultDetailDTO;
import com.sooRyeo.app.jsonBuilder.JsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.sooRyeo.app.model.ScheduleDao;


@Service
public class ScheduleService_imple implements ScheduleService {

	
	@Autowired
	private ScheduleDao dao;

	@Autowired
	private AES256 aES256;

	@Autowired
	private JsonBuilder jsonBuilder;
	
	// 스케줄테이블 select
	@Override
	public List<Map<String, String>> showAssignment(int userid) {
		List<Map<String, String>> schedule = dao.showAssignment(userid);
		return schedule;
	}

	
	// 내 일정 테이블 select
	@Override
	public List<Map<String, String>> showTodo(int userid) {
		List<Map<String, String>> schedule = dao.showTodo(userid);
		return schedule;
	}
	
	// 상담 테이블 select
	@Override
	public List<Map<String, String>> showConsult(int userid) {
		List<Map<String, String>> schedule = dao.showConsult(userid);
		return schedule;
	}


	// 내 개인일정 update - 스케줄테이블 update
	@Override
	public int update_tbl_schedule(String schedule_seq, String title, String start_date, String end_date) {
		
		int n1 = dao.update_tbl_schedule(schedule_seq, title, start_date, end_date);
		return n1;
	}


	// 내 개인일정 수정 - todo테이블 update
	@Override
	public int update_tbl_todo(String schedule_seq, String content) {
		int n2 = dao.update_tbl_todo(schedule_seq, content);
		return n2;
	}


	// 내 개인일정 insert - 스케줄테이블 insert
	@Override
	public int insert_tbl_schedule(String title, String start_date, String end_date, String content, int userid) {
		
		int n = dao.insert_tbl_schedule(title, start_date, end_date, content, userid);
		return n;
	}

	
	// 내 개인일정 삭제 - todo 테이블 delete
	@Override
	public int delete_tbl_todo(String schedule_seq) {
		int n1 = dao.delete_tbl_todo(schedule_seq);
		return n1;
	}


	// 내 개인일정 삭제 - 스케줄 테이블 delete
	@Override
	public int delete_tbl_schedule(String schedule_seq) {
		int n2 = dao.delete_tbl_schedule(schedule_seq);
		return n2;
	}

	private void parseEmail(List<Consult> consultList) {
		for(Consult consult : consultList) {
			if(consult.getStudent() != null) {
				consult.getStudent().setDecodedEmail(aES256);
			}

			if(consult.getProfessor() != null) {
				consult.getProfessor().setDecodedEmail(aES256);
			}
        }
	}


	@Override
	public ModelAndView makeApproveConsultPage(HttpServletRequest request, ModelAndView mav) {
		
		int currentPage = Integer.parseInt(request.getParameter("page")==null? "1" : request.getParameter("page"));
		int sizePerPage = 10;
		HttpSession session = request.getSession();
		int professor_id = ((Professor) session.getAttribute("loginuser")).getProf_id();
		
		List<Consult> unconfirmedConsultList = dao.getUnconfirmedConsultList(currentPage, sizePerPage, professor_id);

		parseEmail(unconfirmedConsultList);
		
		
		int totalElementCount = dao.getUnconfirmedConsultCount(professor_id);
		
		Pager<Consult> schedulePager = new Pager<Consult>(unconfirmedConsultList, currentPage, sizePerPage, totalElementCount);
		
		mav.addObject("consultList", schedulePager.getObjectList());
		mav.addObject("pageBar", schedulePager.makePageBar(request.getContextPath() + "/professor/approveConsult.lms"));
		mav.addObject("currentPage", schedulePager.getPageNumber());
		mav.addObject("perPageSize", schedulePager.getPerPageSize());
		mav.setViewName("consultApprove");
		
		
		return mav;
	}

	@Override
	public ResponseEntity<String> getConsultDetail(HttpServletRequest request) {
		int schedule_seq = Integer.parseInt(request.getParameter("schedule_seq"));
		Consult consult = dao.getConsult(schedule_seq);
		consult.getStudent().setDecodedEmail(aES256);

		ConsultDetailDTO dto = ConsultDetailDTO.toDTO(consult);

		return ResponseEntity.ok(jsonBuilder.toJson(dto));

	}

	@Override
	public ResponseEntity<String> updateConsultApproveStatus(HttpServletRequest request, ConsultApprovalDto consultApprovalDto) {
		int result = -1;
		if(consultApprovalDto.getIsApproved()) {
			result = dao.updateConsultApproveStatus(consultApprovalDto);
		}
		else {
			result = dao.deleteUnapprovedConsult(consultApprovalDto);
		}


		if(result != 1) {
			return ResponseEntity.badRequest().body("요청한 상담이 존재하지 않습니다");
		}

		return ResponseEntity.ok("수정 성공");
	}

	@Override
	public ModelAndView getProfessorConsultPage(HttpServletRequest request,  ModelAndView mav) {

		int currentPage = Integer.parseInt(request.getParameter("page")==null? "1" : request.getParameter("page"));
		int sizePerPage = 10;

		HttpSession session = request.getSession();
		int professor_id = ((Professor) session.getAttribute("loginuser")).getProf_id();


		List<Consult> approvedConsult = dao.getConfirmedConsultList(professor_id, currentPage, sizePerPage);
		int totalElementCount = dao.getConfirmedConsultCount(professor_id);

		parseEmail(approvedConsult);

		Pager<Consult> consultPager = new Pager<>(approvedConsult, currentPage, sizePerPage, totalElementCount);
 		mav.addObject("consultList", consultPager.getObjectList());
		mav.addObject("pageBar", consultPager.makePageBar(request.getContextPath() + "/professor/consult.lms"));
		mav.addObject("currentPage", consultPager.getPageNumber());
		mav.addObject("perPageSize", consultPager.getPerPageSize());
		mav.setViewName("approvedConsult");

		return mav;
	}

	@Override
	public ModelAndView getStudentConsultPage(HttpServletRequest request, ModelAndView mav) {

		int currentPage = Integer.parseInt(request.getParameter("page")==null? "1" : request.getParameter("page"));
		int sizePerPage = 10;

		HttpSession session = request.getSession();
		int student_id = ((Student) session.getAttribute("loginuser")).getStudent_id();


		List<Consult> approvedConsult = dao.getStudentConfirmedConsultList(student_id, currentPage, sizePerPage);
		int totalElementCount = dao.getStudentConfirmedConsultCount(student_id);

		parseEmail(approvedConsult);

		Pager<Consult> consultPager = new Pager<>(approvedConsult, currentPage, sizePerPage, totalElementCount);

		mav.addObject("consultList", consultPager.getObjectList());
		mav.addObject("pageBar", consultPager.makePageBar(request.getContextPath() + "/student/consult.lms"));
		mav.addObject("currentPage", consultPager.getPageNumber());
		mav.addObject("perPageSize", consultPager.getPerPageSize());
		mav.setViewName("approvedConsult");
		return mav;
	}


	// 시험테이블 select
	@Override
	public List<Map<String, String>> showExam(int userid) {
		List<Map<String, String>> schedule = dao.showExam(userid);
		return schedule;
	}


}
