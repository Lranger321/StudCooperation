package main.service;

import java.util.*;

public interface StudentService {

    List<String> getGroups();

    int getStudentInGroup(String group);

    void deleteAll();
}
