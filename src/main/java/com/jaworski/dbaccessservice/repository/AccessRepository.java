package com.jaworski.dbaccessservice.repository;

import com.jaworski.dbaccessservice.configuration.DataSourceConfiguration;
import com.jaworski.dbaccessservice.dto.Student;
import com.jaworski.dbaccessservice.dto.TableKursmain;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Repository
public class AccessRepository {

    private final DataSourceConfiguration dataSourceConfiguration;

    public AccessRepository(DataSourceConfiguration dataSourceConfiguration) {
        this.dataSourceConfiguration = dataSourceConfiguration;
    }

    public Collection<Student> getStudents() throws SQLException, ClassNotFoundException {
        Connection sqlConnection = dataSourceConfiguration.getSqlConnection();
        Statement statement = sqlConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM [" + TableKursmain.TABLE_NAME + "]" +
                " ORDER BY [" + TableKursmain.DATE_END + "]" + " DESC, [" + TableKursmain.CERT_NO + "]" + " ASC");
        statement.close();
        return getResultCollection(resultSet);
    }

    public Collection<Student> getStudentsByWeek(int week) throws SQLException, ClassNotFoundException {
        Connection sqlConnection = dataSourceConfiguration.getSqlConnection();
        Statement statement = sqlConnection.createStatement();
        ResultSet set = statement.executeQuery("SELECT MAX(" + TableKursmain.DATE_END + ") AS LatestDate FROM [" +
                TableKursmain.TABLE_NAME + "]");
        String latestDate = null;
        while (set.next()) {
            latestDate = set.getString("LatestDate");
        }
        if (latestDate == null) {
            return Collections.emptyList();
        }
        ResultSet resultSet = statement.executeQuery("SELECT * FROM [" + TableKursmain.TABLE_NAME + "] WHERE [" +
                TableKursmain.DATE_END + "] = '" + latestDate + "'" + "ORDER BY [" + TableKursmain.CERT_NO + "]" + " DESC");
        statement.close();
        return getResultCollection(resultSet);
    }

    private static Collection<Student> getResultCollection(ResultSet resultSet) throws SQLException {
        Collection<Student> result = new ArrayList<>();
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
    }
}
