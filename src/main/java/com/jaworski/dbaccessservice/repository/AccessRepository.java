package com.jaworski.dbaccessservice.repository;

import com.jaworski.dbaccessservice.configuration.DataSourceConfiguration;
import com.jaworski.dbaccessservice.dto.Student;
import com.jaworski.dbaccessservice.dto.TableKursmain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Repository
public class AccessRepository {

    private static final Logger LOG = LogManager.getLogger(AccessRepository.class);

    private final DataSourceConfiguration dataSourceConfiguration;

    public AccessRepository(DataSourceConfiguration dataSourceConfiguration) {
        this.dataSourceConfiguration = dataSourceConfiguration;
    }

    public Collection<Student> getStudents() {
        List<Student> result = new ArrayList<>();
        try {
            Connection sqlConnection = dataSourceConfiguration.getSqlConnection();
            ResultSet resultSet;
            try (Statement statement = sqlConnection.createStatement()) {
                resultSet = statement.executeQuery("SELECT * FROM [" + TableKursmain.TABLE_NAME + "]");
            }
            while (resultSet.next()) {
                int certNo = resultSet.getInt(TableKursmain.CERT_NO);
                String name = resultSet.getString(TableKursmain.FIRST_NAME);
                String surname = resultSet.getString(TableKursmain.SURNAME);
                Date dateBegine = resultSet.getDate(TableKursmain.DATE_BEGINE);
                Date dateEnd = resultSet.getDate(TableKursmain.DATE_END);
                String mrMs = resultSet.getString(TableKursmain.MR_MS);
                String certType = resultSet.getString(TableKursmain.CERT_TYPE);
                String courseNo = resultSet.getString(TableKursmain.COURSE_NO);

                Student student = new Student(certNo, name, surname, courseNo, dateBegine, dateEnd, mrMs, certType);
                result.add(student);
            }
            return result;

        } catch (ClassNotFoundException | SQLException e) {
            LOG.error("Sql error", e);
            return Collections.emptyList();
        }
    }
}
