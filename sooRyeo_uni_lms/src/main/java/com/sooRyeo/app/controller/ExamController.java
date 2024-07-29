package com.sooRyeo.app.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sooRyeo.app.aop.RequireLogin;
import com.sooRyeo.app.domain.Admin;
import com.sooRyeo.app.domain.Exam;
import com.sooRyeo.app.domain.Professor;
import com.sooRyeo.app.domain.Student;
import com.sooRyeo.app.service.ExamService;

@Controller
public class ExamController {

    @Autowired
    private ExamService examService;


	@RequireLogin(type = {Student.class})
	@PostMapping("/student/exam/test.lms")
	public ModelAndView test(ModelAndView mav, HttpServletRequest request) throws NumberFormatException , NullPointerException {
		int schedule_seq = Integer.parseInt(request.getParameter("schedule_seq"));

		return examService.takeExam(mav, request, schedule_seq);
	}
	
	@RequireLogin(type = {Student.class})
	@PostMapping("/exam/SelectAnswer.lms")
	public ModelAndView selectAnswer(ModelAndView mav, HttpServletRequest request) {
		
		String schedule_seq = request.getParameter("schedule_seq");
		
		int selCount = Integer.parseInt(request.getParameter("selCount"));
		
		Exam getCourse_seq = examService.getCourse_seq(schedule_seq); // 강의 시퀀스를 불러오는 메소드
		
		int course_seq = getCourse_seq.getFk_course_seq();
		
		List<Integer> inputAnswers = new ArrayList<>();
		
		for(int i=1; i<selCount+1; i++) {
			Integer selectAnswer = Integer.parseInt(request.getParameter(String.valueOf(i)));
			
			if (selectAnswer != null) {
				inputAnswers.add(selectAnswer);
	        }
		}
		
		examService.insertMongoStudentExamAnswer(inputAnswers, schedule_seq , request, course_seq);
		
		mav.addObject("message", "답안지 제출이 완료되었습니다.");
		mav.addObject("loc", request.getContextPath()+"/student/exam.lms?course_seq="+course_seq); // 여기서 course_seq를 어떻게 보내야할지 고민중. post 방시인디
		mav.setViewName("msg");

		return mav;
	}


	@RequireLogin(type = {Professor.class})
	@GetMapping(value = "/professor/exam.lms")
	public ModelAndView professorExam(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) {
		return examService.getExamPage(mav, request, response);
	}

	@RequireLogin(type = {Professor.class})
	@GetMapping("/professor/exam/result.lms")
	public ModelAndView getExamResultPage(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) throws NumberFormatException {
		//todo : error handler 추가하기
		int schedule_seq = Integer.parseInt(request.getParameter("schedule_seq") == null? "-1" : request.getParameter("schedule_seq"));
		mav.addObject("schedule_seq", schedule_seq);
		mav.setViewName("exam/examResult");
		return mav;
	}

	@RequireLogin(type = {Professor.class, Student.class})
	@GetMapping("/professor/exam/resultREST.lms")
	public ResponseEntity<String> getExamResultData(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) {
		return examService.getExamResultData(mav, request, response);
	}

	@RequireLogin(type = {Student.class})
	@GetMapping(value = "/student/exam.lms")
	public ModelAndView studentExam(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) throws NumberFormatException {
		return examService.getExamPage(mav, request, response);
	}


	@RequireLogin(type = {Student.class})
	@GetMapping("/student/exam/result.lms")
	public ModelAndView getStudentExamResult(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) throws NumberFormatException {
		//todo : error handler 추가하기
		int schedule_seq = Integer.parseInt(request.getParameter("schedule_seq") == null? "-1" : request.getParameter("schedule_seq"));
		mav.addObject("schedule_seq", schedule_seq);
		mav.setViewName("exam/examResult");
		return mav;
	}

	@RequireLogin(type = {Student.class})
	@GetMapping("/student/exam/resultREST.lms")
	public ResponseEntity<String> getStudentExamResultData(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) {
		return examService.getStudentExamResultData(mav, request, response);
	}

	@RequireLogin(type = {Student.class})
	@GetMapping("/student/exam/wait.lms")
	public ModelAndView waitExamPage(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) throws NumberFormatException {
		int schedule_seq = Integer.parseInt(request.getParameter("schedule_seq"));

		return examService.getWaitExamPage(mav, request, response, schedule_seq);
	}
	
	
}
