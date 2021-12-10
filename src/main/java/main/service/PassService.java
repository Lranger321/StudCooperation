package main.service;

import main.dto.PassDTO;

import java.util.List;

public interface PassService {

    List<PassDTO> getPassesBySubject(String subject);
}
