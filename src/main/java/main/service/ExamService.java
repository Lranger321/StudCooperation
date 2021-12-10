package main.service;

import main.dto.ExamDTO;

import java.util.*;

public interface ExamService {

        List<ExamDTO> getExamsBySubject(String subject);
}
