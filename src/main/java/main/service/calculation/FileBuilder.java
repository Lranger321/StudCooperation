package main.service.calculation;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class FileBuilder {

    private final PageBuilder builder;

    public File createFile(){
        Workbook workbook = new XSSFWorkbook();
        for(int i=0;i< 8;i++){
            builder.createPage(workbook,i);
        }
        File file = new File("result.xlsx");

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
