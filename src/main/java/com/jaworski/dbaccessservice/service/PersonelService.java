package com.jaworski.dbaccessservice.service;

import com.jaworski.dbaccessservice.dto.Personel;
import com.jaworski.dbaccessservice.repository.AccessRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class PersonelService {

    private final AccessRepository accessRepository;

    public PersonelService(AccessRepository accessRepository) {
        this.accessRepository = accessRepository;
    }

    public Optional<Collection<Personel>> getNames() {
        Collection<Personel> personel;
        personel = accessRepository.getPersonel();
        return Optional.ofNullable(personel);
    }
}
