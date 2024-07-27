package com.sooRyeo.app.mongo.repository;

import com.sooRyeo.app.mongo.entity.StudentAnswer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentExamAnswerRepository extends MongoRepository<StudentAnswer, String> {
    List<StudentAnswer> findAllByExamAnswersId(String answerMongoId);

    Optional<StudentAnswer> findByExamAnswersIdAndStudentId(String answerMongoId, Integer studentId);
}
