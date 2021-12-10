package main.service.importer;

import main.dto.ImportDTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface Importer {

    String importFile(File file, String group) throws IOException;


}
