package com.jaworski.dbaccessservice.rest;

import com.jaworski.dbaccessservice.dto.Student;
import com.jaworski.dbaccessservice.service.PersonelService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Collection;

@RestController()
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccessRestController {

    private static final Logger LOG = LogManager.getLogger(AccessRestController.class);

    private final PersonelService personelService;

    public AccessRestController(PersonelService personelService) {
        this.personelService = personelService;
    }

    @GetMapping(path = "/hello")
    public ResponseEntity<String> getAllData() {
        return ResponseEntity.ok("Hello World");
    }

    @GetMapping(path = "/names")
    public ResponseEntity<Collection<Student>> getNames() {
        try {
            return ResponseEntity.of(personelService.getNames());
        } catch (SQLException | ClassNotFoundException e) {
            LOG.error("Error: {}", e, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(path = "/names/{weekNo}")
    public ResponseEntity<Collection<Student>> getNamesWeek(@PathVariable(required = true) int weekNo) {
        try {
            return ResponseEntity.of(personelService.getNamesByWeek(weekNo));
        } catch (SQLException | ClassNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
