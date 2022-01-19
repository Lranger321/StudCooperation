package main.service;

import lombok.RequiredArgsConstructor;
import main.dao.entity.Exam;
import main.dao.repository.ExamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ExamServiceImpl implements ExamService {

    private final ExamRepository repository;

    @Override
    public Map<String, List<Exam>> getExamsBySubjectAndSemester(String subject, int semester) {
        return repository.findAllByNameAndSemester(subject, semester).stream()
                .collect(Collectors.groupingBy(Exam::getGroup));
    }

    @Override
    public Map<String, Exam> getExamsByGroup(String group) {
        return null;
    }

    @Override
    public List<String> getExamsBySemester(int semester) {
        return repository.findBySemester(semester).stream()
                .map(Exam::getName)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

}
