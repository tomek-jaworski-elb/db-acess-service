package com.jaworski.dbaccessservice.resources;

import com.jaworski.dbaccessservice.dto.UserRestService;
import org.apache.commons.lang3.StringUtils;
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

    @Value("${rest.service.credentials}")
    private String restServiceCredentials;

    public UserRestService getRestServiceCredentials() {
        StringUtils.split(restServiceCredentials, ":");
        String[] credentials = StringUtils.split(restServiceCredentials, ":");
        if (credentials.length != 2) {
            throw new IllegalArgumentException("Wrong credentials: " + restServiceCredentials);
        }
        UserRestService user = new UserRestService();
        user.setName(credentials[0]);
        user.setPassword(credentials[1]);
        return user;
    }
}
