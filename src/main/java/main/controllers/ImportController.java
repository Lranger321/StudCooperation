package main.controllers;

import lombok.RequiredArgsConstructor;
import main.dto.ImportDTO;
import main.service.importer.ImportService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ImportController {

    private final ImportService importService;

    @PostMapping(value = "/api/import/")
    public ImportDTO importFile(@RequestParam MultipartFile file) throws IOException {
        return importService.importFile(convertMultiPartToFile(file));
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File toFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(toFile);
        fos.write(file.getBytes());
        fos.close();
        return toFile;
    }
}
