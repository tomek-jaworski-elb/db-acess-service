package com.jaworski.dbaccessservice.service;

import com.jaworski.dbaccessservice.dto.Student;
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

    public Optional<Collection<Student>> getNames() {
        Collection<Student> student;
        student = accessRepository.getStudents();
        return Optional.ofNullable(student);
    }
}
