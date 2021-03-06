package main.controllers;

import lombok.RequiredArgsConstructor;
import main.service.ConverterService;
import org.apache.poi.util.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImportController {

    private final ConverterService service;

    @PostMapping(value = "/api/import/", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> importFile(@RequestBody MultipartFile[] files) throws IOException {
        File file = service.convertFiles(convertMultiPartToFile(files));
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=result.xlsx")
                .body(IOUtils.toByteArray(new FileInputStream(file)));
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
