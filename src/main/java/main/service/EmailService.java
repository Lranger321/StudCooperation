package main.service;

import java.io.File;

public interface EmailService {

    void sendEmail(String email, File file);
}
