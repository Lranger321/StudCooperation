package main.service.importer;

import lombok.RequiredArgsConstructor;
import main.dao.entity.Exam;
import main.dao.entity.Pass;
import main.dao.entity.Student;
import main.dao.repository.StudentRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BaseXslxImporter implements Importer {

    private final StudentRepository studentRepository;

    @Override
    public String importFile(File file, String group) throws IOException {
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        XSSFWorkbook excelBook = new XSSFWorkbook(new FileInputStream(file));
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < excelBook.getNumberOfSheets(); i++) {
            students.addAll(parseSheet(excelBook.getSheetAt(i), offsetDateTime, group));
        }
        try {
            saveStudents(distinctStudents(students));
        } catch (Exception e) {
            e.printStackTrace();
        }
        deleteOldStudents(group, offsetDateTime);
        return "SUCCESS";
    }

    private void deleteOldStudents(String group, OffsetDateTime offsetDateTime) {
        studentRepository.deleteAllByGroupAndCreatedAtBefore(group, offsetDateTime);
    }

    private void saveStudents(List<Student> students) {
        studentRepository.saveAll(students);
    }

    private List<Student> parseSheet(XSSFSheet sheet, OffsetDateTime time, String group) {
        List<Student> students = new ArrayList<>();
        List<Boolean> isExamList = new ArrayList<>();
        List<String> subjectNames = new ArrayList<>();
        XSSFRow rowWithNames = sheet.getRow(0);
        if (rowWithNames == null) {
            return students;
        }
        rowWithNames.forEach(cell -> {
            String value = cell.getStringCellValue();
            if (!value.equals("№") && !value.isEmpty() && !value.equals("ФИО")) {
                isExamList.add(value.contains("Экзамен"));
                subjectNames.add(value.split("\\(", 2)[0]);
            }
        });
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            XSSFRow row = sheet.getRow(i);
            List<Exam> exams = new ArrayList<>();
            List<Pass> passes = new ArrayList<>();
            Student student = new Student();
            student.setName(row.getCell(1).getStringCellValue());
            student.setGroup(group);
            student.setCreatedAt(time);
            for (int j = 0; j < subjectNames.size(); j++) {
                int cellNumber = j + 2;
                if (isExamList.get(j)) {
                    String mark = row.getCell(cellNumber).getRawValue();
                    if (mark != null) {
                        exams.add(Exam.builder()
                                .name(subjectNames.get(j))
                                .mark(mark)
                                .student(student)
                                .createdAt(time)
                                .build());
                    }
                } else {
                    try {
                        passes.add(Pass.builder()
                                .name(subjectNames.get(j))
                                .isPassed(isPassed(row.getCell(cellNumber).getRawValue()))
                                .createdAt(time)
                                .student(student)
                                .build());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            student.setPasses(passes);
            student.setExams(exams);
            students.add(student);
        }
        return students;
    }

    private List<Student> distinctStudents(List<Student> students) {
        List<Student> list = students.stream()
                .distinct()
                .filter(student -> !student.getName().equals(StringUtils.EMPTY))
                .collect(Collectors.toList());
        return list.stream()
                .peek(student -> addExamsAndPasses(student, students))
                .collect(Collectors.toList());
    }

    private void addExamsAndPasses(Student student, List<Student> students) {
        List<Student> list = students.stream()
                .filter(student::equals)
                .collect(Collectors.toList());
        students.removeAll(list);
        List<Pass> passes = new ArrayList<>();
        List<Exam> exams = new ArrayList<>();
        list.stream()
                .map(Student::getPasses)
                .forEach(passes::addAll);
        list.stream()
                .map(Student::getExams)
                .forEach(exams::addAll);
        passes = passes.stream()
                .peek(pass -> {
                    pass.setStudent(student);
                })
                .collect(Collectors.toList());
        exams = exams.stream()
                .peek(exam -> {
                    exam.setStudent(student);
                })
                .collect(Collectors.toList());
        student.setExams(exams);
        student.setPasses(passes);
    }

    private boolean isPassed(String value) {
        if (value == null) {
            return false;
        }
        return value.equals("Зач.");
    }
}
