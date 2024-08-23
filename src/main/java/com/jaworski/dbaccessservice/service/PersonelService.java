package com.jaworski.dbaccessservice.service;

import com.jaworski.dbaccessservice.dto.Student;
import com.jaworski.dbaccessservice.repository.AccessRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Service
public class PersonelService {

    private final AccessRepository accessRepository;

    public PersonelService(AccessRepository accessRepository) {
        this.accessRepository = accessRepository;
    }

    public Optional<Collection<Student>> getNames() throws SQLException, ClassNotFoundException {
        Collection<Student> students;
        students = accessRepository.getStudents();
        return Optional.ofNullable(students);
    }

    public Optional<Collection<Student>> getNamesByWeek(int week) throws SQLException, ClassNotFoundException {
        Collection<Student> students;
        students = accessRepository.getStudentsByWeek(week);
        return Optional.ofNullable(students);
    }
}
