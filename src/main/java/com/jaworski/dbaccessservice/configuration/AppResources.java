package com.jaworski.dbaccessservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AppResources {

    @Value("${file.db}")
    private String fileDbPath;

    public Optional<String> getFileDbPath() {
        return Optional.ofNullable(fileDbPath);
    }
}
