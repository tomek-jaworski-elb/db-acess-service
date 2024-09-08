package com.jaworski.dbaccessservice.repository;

import com.jaworski.dbaccessservice.dto.Student;
import com.jaworski.dbaccessservice.service.PersonelService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
class PersonelServiceTest {

    @Mock
    private AccessRepository accessRepository;

    @InjectMocks
    private PersonelService personelService;

    @BeforeEach
    void setUp() {
    }
    @Test
    void getAllStudents_givenNull_test() throws SQLException, ClassNotFoundException {
        when(accessRepository.getStudents()).thenReturn(null);
        var names = personelService.getNames();
        Assertions.assertNotNull(names);
        Assertions.assertTrue(names.isEmpty());
    }
    @Test
    void getAllStudents_test() throws SQLException, ClassNotFoundException {
        Student student = new Student(1, "name", "", "", null, null, "", "");
        Collection<Student> mockResult = List.of(student);
        when(accessRepository.getStudents()).thenReturn(mockResult);
        var names = personelService.getNames();
        Assertions.assertNotNull(names);
        Assertions.assertTrue(names.isPresent());
        org.assertj.core.api.Assertions.assertThat(names.get().stream().toList().get(0).getId()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(names.get().stream().toList().get(0).getName()).isEqualTo("name");
    }

    @Test
    void getStudents_test() throws SQLException, ClassNotFoundException {
        Student student = new Student(1, "name", "", "", null, null, "", "");
        Collection<Student> mockResult = List.of(student);
        when(accessRepository.getStudentsByWeek(anyInt())).thenReturn(mockResult);
        var names = personelService.getNamesByWeek(1);
        Assertions.assertNotNull(names);
        Assertions.assertTrue(names.isPresent());
        org.assertj.core.api.Assertions.assertThat(names.get().stream().toList().get(0).getId()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(names.get().stream().toList().get(0).getName()).isEqualTo("name");
    }
}