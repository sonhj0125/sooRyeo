package com.sooRyeo.app.domain;


public class Assignment {
	
    private Integer schedule_seq_assignment;
    private Integer fk_course_seq;
    private String content;
    private String attatched_file;
    private String orgfilename;
       
   public Integer getSchedule_seq_assignment() {
      return schedule_seq_assignment;
   }
   public Integer getFk_course_seq() {
      return fk_course_seq;
   }
   public String getContent() {
      return content;
   }
   public String getAttatched_file() {
      return attatched_file;
   }
   public String getOrgfilename() {
	  return orgfilename;
   }

    
    
    
}
