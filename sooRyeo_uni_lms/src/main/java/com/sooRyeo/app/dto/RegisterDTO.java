package com.sooRyeo.app.dto;

import org.springframework.web.multipart.MultipartFile;

import com.sooRyeo.app.common.Sha256;

public class RegisterDTO {
	
	private String name;
	private String pwd;
	private String email;
	private String jubun;
	private String tel;
	private String fk_department_seq;

	private String office_address;
	private String department_seq;
	
	private String address;
	private String postcode;
	private String detailaddress;
	private String extraAddress;
	private String register_year;
	private String grade;
	private String img_name;
	
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getDetailaddress() {
		return detailaddress;
	}
	public void setDetailaddress(String detailaddress) {
		this.detailaddress = detailaddress;
	}
	public String getExtraAddress() {
		return extraAddress;
	}
	public void setExtraAddress(String extraAddress) {
		this.extraAddress = extraAddress;
	}
	private MultipartFile attach;

	public String getOffice_address() {
		return office_address;
	}
	public void setOffice_address(String office_address) {
		this.office_address = office_address;
	}
	public String getDepartment_seq() {
		return department_seq;
	}
	public void setDepartment_seq(String department_seq) {
		this.department_seq = department_seq;
	}
	public MultipartFile getAttach() {
		return attach;
	}
	public void setAttach(MultipartFile attach) {
		this.attach = attach;
	}
	public String getImg_name() {
		return img_name;
	}
	public void setImg_name(String img_name) {
		this.img_name = img_name;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = Sha256.encrypt(pwd);
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getJubun() {
		return jubun;
	}
	public void setJubun(String jubun) {
		this.jubun = jubun;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getFk_department_seq() {
		return fk_department_seq;
	}
	public void setFk_department_seq(String fk_department_seq) {
		this.fk_department_seq = fk_department_seq;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRegister_year() {
		return register_year;
	}
	public void setRegister_year(String register_year) {
		this.register_year = register_year;
	}
	
}
