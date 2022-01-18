package main.controllers;

import lombok.RequiredArgsConstructor;
import main.service.ConverterService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImportController {

    private final ConverterService service;

    @PostMapping(value = "/api/import/")
    public ResponseEntity<Resource> importFile(@RequestParam MultipartFile[] files) throws IOException {
        File file = service.convertFiles(convertMultiPartToFile(files));
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
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
