package com.sooRyeo.app.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.sooRyeo.app.domain.Student;
import com.sooRyeo.app.dto.LoginDTO;
import com.sooRyeo.app.model.StudentDao;

@Service
public class LoginService_imple implements LoginService {
	
	@Autowired
	private StudentDao studentDao;
	

	@Override
	public JSONObject studentLogin(HttpServletRequest resquest, LoginDTO loginDTO) {
		
		Student loginStudent = studentDao.selectStudent(loginDTO);
		
		JSONObject jsonObject = new JSONObject();
		
		if(loginStudent == null) {
			jsonObject.put("isSuccess", false);
			
			return jsonObject;
		}
		HttpSession session = resquest.getSession();
		session.setAttribute("loginuser", loginStudent);

			
		
		jsonObject.put("isSuccess", true);
		jsonObject.put("redirectUrl", resquest.getContextPath() +  "/student/dashboard.lms");
		return jsonObject;
		
	}

	
	@Override
	public ModelAndView logout(ModelAndView mav, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		session.invalidate();

		String message = "로그아웃 되었습니다.";
		String loc = request.getContextPath()+"/"; 
		
		// String loc = request.getHeader("refer");
		// request.getHeader("refer"); 은 이전 페이지의 URL을 가져오는 것이다!!!!!!!!!!
		
		mav.addObject("message", message);
		mav.addObject("loc", loc);
		
		mav.setViewName("msg");
			
		return mav;
	}

}
