package main.service.importer;

import lombok.RequiredArgsConstructor;
import main.dao.entity.Exam;
import main.dao.repository.ExamRepository;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseXslxImporter implements Importer {

    private final ExamRepository repository;

    @Transactional
    @Override
    public String importFile(File file, String group) throws IOException {
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        XSSFWorkbook excelBook = new XSSFWorkbook(new FileInputStream(file));
        List<Exam> exams = new ArrayList<>();
        for (int i = 0; i < excelBook.getNumberOfSheets(); i++) {
            exams.addAll(parseSheet(excelBook.getSheetAt(i), offsetDateTime, group, i));
        }
        repository.saveAll(exams);
        //deleteOldStudents(group, offsetDateTime);
        return "SUCCESS";
    }

    private List<Exam> parseSheet(XSSFSheet sheet, OffsetDateTime time, String group, int semester) {
        List<Exam> exams = new ArrayList<>();
        List<Boolean> isExamList = new ArrayList<>();
        List<String> subjectNames = new ArrayList<>();
        XSSFRow rowWithNames = sheet.getRow(0);
        if (rowWithNames == null) {
            return exams;
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
            for (int j = 0; j < subjectNames.size(); j++) {
                try {
                    int cellNumber = j + 2;
                    if (isExamList.get(j)) {
                        String mark = row.getCell(cellNumber).getRawValue();
                        if (checkMark(mark)) {
                            if (mark.equals("32") || mark.equals("33")) {
                                mark = "Н/я";
                            }
                            exams.add(Exam.builder()
                                    .name(subjectNames.get(j))
                                    .mark(mark)
                                    .group(group)
                                    .createdAt(time)
                                    .semester(semester)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return exams;
    }

    private boolean checkMark(String mark) {
        if (mark == null) {
            return false;
        }
        return mark.matches("[2345]{1,2}");
    }
}
