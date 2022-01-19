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

    private final Importer importer;

    @Transactional
    public void importFile(File file) {
        String group = getGroupFromFile(file);
        try {
            importer.importFile(file, group);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getGroupFromFile(File file) {
        return FilenameUtils.removeExtension(file.getName()).replace("Все_семестровые_журналы_", "");
    }

}
