package main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.service.calculation.FileBuilder;
import main.service.importer.ImportService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.io.File;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConverterService {

    private final ImportService importService;
    private final FileBuilder fileBuilder;
    private final ExamService examService;

    public File convertFiles(List<File> files){
        log.info("Import start");
        for(File file:files){
            importService.importFile(file);
        }
        log.info("Creating file");
        File file = fileBuilder.createFile();
        log.info("File created");
        examService.deleteAll();
        return file;
    }
}
