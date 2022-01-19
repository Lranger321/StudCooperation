package main.controllers;

import lombok.RequiredArgsConstructor;
import main.service.ConverterService;
import org.apache.poi.util.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImportController {

    private final ConverterService service;

    @PostMapping(value = "/api/import/",   produces = "application/xlsx")
    public RedirectView importFile(@RequestParam MultipartFile[] files) throws IOException {
        File file = service.convertFiles(convertMultiPartToFile(files));
        System.out.println("file://"+file.getAbsolutePath());
        return new RedirectView("file://"+file.getAbsolutePath());
    }

    private List<File> convertMultiPartToFile(MultipartFile[] files) {
        List<File> fileList = new ArrayList<>();
        for (MultipartFile file : files) {
            File toFile = new File(file.getOriginalFilename());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(toFile);
                fos.write(file.getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileList.add(toFile);
        }
        return fileList;
    }
}
