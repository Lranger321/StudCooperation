package main.service.calculation;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public interface PageBuilder {

    Sheet createPage(Workbook workbook, int semestr);

    PageType type();
}
