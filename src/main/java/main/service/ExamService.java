package main.service;

import main.dao.entity.Exam;
import main.dto.ExamDTO;

import java.util.*;

public interface ExamService {

        Map<String, List<Exam>> getExamsBySubject(String subject);

        Map<String, Exam> getExamsByGroup(String group);

        List<String> getExamsBySemester(int semester);

        void deleteAll();
}
