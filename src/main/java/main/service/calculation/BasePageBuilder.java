package main.service.calculation;

import lombok.RequiredArgsConstructor;
import main.dao.entity.Exam;
import main.service.ExamService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class BasePageBuilder implements PageBuilder {

    private final ExamService examService;

    @Override
    public Sheet createPage(Workbook workbook, int semester) {
        Sheet sheet = workbook.createSheet("Семестр " + (semester + 1));
        List<String> examNames = examService.getExamsBySemester(semester);
        addHeader(sheet, semester);
        int lastRow = 1;
        for (String examName : examNames) {
            lastRow = addExamStat(sheet, examName, lastRow, semester);
        }
        return sheet;
    }

    private void addHeader(Sheet sheet, int semester) {
        Row header1 = sheet.createRow(0);
        if (semester % 2 == 0) {
            header1.createCell(4).setCellValue(String.format("Итог %d зимней сессии", (semester/2) +1));
        } else {
            header1.createCell(4).setCellValue(String.format("Итог %d летней сессии", (semester/2) +1));
        }
        Row header = sheet.createRow(1);
        header.createCell(0).setCellValue("Название экзамена");
        header.createCell(1).setCellValue("Группа");
        header.createCell(2).setCellValue("Кол-во студентов");
        header.createCell(3).setCellValue("Явилось");
        header.createCell(4).setCellValue("Сдали");
        header.createCell(5).setCellValue("% успеваемости");
        header.createCell(6).setCellValue("Отлично");
        header.createCell(7).setCellValue("Олично и хорошо");
        header.createCell(8).setCellValue("% отлично и хорошо");
        header.createCell(9).setCellValue("Удовлетворительно");
        header.createCell(10).setCellValue("Неуд. и неявка");
    }

    private int addExamStat(Sheet sheet, String examName, int lastRow, int semester) {
        Map<String, List<Exam>> examMap = examService.getExamsBySubjectAndSemester(examName, semester);
        int studentCountGlobal = 0;
        int mark5CountGlobal = 0;
        int mark4CountGlobal = 0;
        int mark3CountGlobal = 0;
        int notShowingCountGlobal = 0;
        for (String group : examMap.keySet()) {
            List<Exam> exams = examMap.get(group);
            int studentCount = exams.size();
            int mark5Count = (int) exams.stream().filter(examDTO -> examDTO.getMark().equals("5")).count();
            int mark4Count = (int) exams.stream().filter(examDTO -> examDTO.getMark().equals("4")).count();
            int mark3Count = (int) exams.stream().filter(examDTO -> examDTO.getMark().equals("3")).count();
            int notShowingCount = (int) exams.stream().filter(examDTO -> examDTO.getMark().equals("Н/я")).count();
            Row row = sheet.createRow(++lastRow);
            row.createCell(0).setCellValue(examName);
            row.createCell(1).setCellValue(group);
            row.createCell(2).setCellValue(studentCount);
            row.createCell(3).setCellValue(studentCount - notShowingCount);
            row.createCell(4).setCellValue(mark5Count + mark4Count + mark3Count);
            row.createCell(5).setCellValue(((mark5Count + mark4Count + mark3Count) * 100 / studentCount) + " %");
            row.createCell(6).setCellValue(mark5Count);
            row.createCell(7).setCellValue(mark5Count + mark4Count);
            row.createCell(8).setCellValue(((mark5Count + mark4Count) * 100 / studentCount) + " %");
            row.createCell(9).setCellValue(mark3Count);
            row.createCell(10).setCellValue(notShowingCount);
            studentCountGlobal += studentCount;
            mark5CountGlobal += mark5Count;
            mark4CountGlobal += mark4Count;
            mark3CountGlobal += mark3Count;
            notShowingCountGlobal += notShowingCount;
        }
        Row row = sheet.createRow(++lastRow);
        row.createCell(0).setCellValue(examName);
        row.createCell(2).setCellValue(studentCountGlobal);
        row.createCell(3).setCellValue(studentCountGlobal - notShowingCountGlobal);
        row.createCell(4).setCellValue(mark5CountGlobal + mark4CountGlobal + mark3CountGlobal);
        row.createCell(5).setCellValue(((mark5CountGlobal + mark4CountGlobal + mark3CountGlobal) * 100 / studentCountGlobal) + " %");
        row.createCell(6).setCellValue(mark5CountGlobal);
        row.createCell(7).setCellValue(mark5CountGlobal + mark4CountGlobal);
        row.createCell(8).setCellValue(((mark5CountGlobal + mark4CountGlobal) * 100 / studentCountGlobal) + " %");
        row.createCell(9).setCellValue(mark3CountGlobal);
        row.createCell(10).setCellValue(notShowingCountGlobal);
        return lastRow;
    }


    @Override
    public PageType type() {
        return PageType.BASE;
    }
}
