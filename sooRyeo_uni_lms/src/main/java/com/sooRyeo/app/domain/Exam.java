package com.sooRyeo.app.domain;

import org.springframework.web.multipart.MultipartFile;

public class Exam {
	
    private Integer fk_schedule_seq;
    private Integer fk_course_seq;
    private String file_name;
    private String original_file_name;
    private String answer_mongo_id;
    private Integer question_count;

    private Schedule schedule;
    
    private MultipartFile attach;


    public Integer getFk_schedule_seq() {
        return fk_schedule_seq;
    }

    public Integer getFk_course_seq() {
        return fk_course_seq;
    }

    public String getFile_name() {
        return file_name;
    }

    public String getOriginal_file_name() {
        return original_file_name;
    }

    public String getAnswer_mongo_id() {
        return answer_mongo_id;
    }

    public Integer getQuestion_count() {
        return question_count;
    }

    public Schedule getSchedule() {
        return schedule;
    }
}
