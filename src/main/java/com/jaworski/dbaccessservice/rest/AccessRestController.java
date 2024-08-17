package com.jaworski.dbaccessservice.rest;

import com.jaworski.dbaccessservice.dto.Personel;
import com.jaworski.dbaccessservice.service.PersonelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;


@RestController()
@RequestMapping(path = "/api")
public class AccessRestController {


    private final PersonelService personelService;

    public AccessRestController(PersonelService personelService) {
        this.personelService = personelService;
    }

    @GetMapping(path = "/hello")
    public ResponseEntity<String> getAllData() {
        return ResponseEntity.ok("Hello World");
    }

    @GetMapping(path = "/names")
    public ResponseEntity<Collection<Personel>> getNames() {
        return ResponseEntity.of(personelService.getNames());
    }

}
