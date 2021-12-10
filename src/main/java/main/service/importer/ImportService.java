package main.service.importer;

import liquibase.util.file.FilenameUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import main.dto.ImportDTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImportService {

    private ExecutorService executor = Executors.newCachedThreadPool();
    private Map<String, ImportProcessing> futureMap = new ConcurrentHashMap<>();
    private final Importer importer;

    @Transactional
    public ImportDTO importFile(File file) {
        String group = getGroupFromFile(file);
        ImportProcessing importProcessing = futureMap.get(group);
        if (importProcessing == null || importProcessing.getFuture().isDone()) {
            ImportDTO dto = new ImportDTO();
            dto.setGroup(group);
            importProcessing = importNewFile(dto, file);
        }
        return importProcessing.getImportDTO();
    }

    private ImportProcessing importNewFile(ImportDTO dto, File file) {
        dto.setResult("EXECUTING");
        Future future = executor.submit(() -> {
                    try {
                        log.info("Import start. File name: " + file.getName());
                        String result = importer.importFile(file, dto.getGroup());
                        dto.setResult(result);
                        log.info("Import finish. File name: " + file.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                        dto.setResult("FAIL");
                        log.error("Import failed. File name: {}. Error: {}", file.getName(), e.getMessage());
                    }
                }
        );
        ImportProcessing importProcessing = new ImportProcessing(future, dto);
        futureMap.put(dto.getGroup(), importProcessing);
        return importProcessing;
    }

    private String getGroupFromFile(File file) {
        return FilenameUtils.removeExtension(file.getName()).replace("Все_семестровые_журналы_", "");
    }


    @AllArgsConstructor
    @Getter
    @Setter
    private static class ImportProcessing {
        private Future future;
        private ImportDTO importDTO;
    }
}
