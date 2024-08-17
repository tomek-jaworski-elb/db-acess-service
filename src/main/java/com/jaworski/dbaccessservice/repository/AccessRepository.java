package com.jaworski.dbaccessservice.repository;

import com.jaworski.dbaccessservice.configuration.DataSourceConfiguration;
import com.jaworski.dbaccessservice.dto.Personel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class AccessRepository {

    private static final Logger LOG = LogManager.getLogger(AccessRepository.class);

    private final DataSourceConfiguration dataSourceConfiguration;

    public AccessRepository(DataSourceConfiguration dataSourceConfiguration) {
        this.dataSourceConfiguration = dataSourceConfiguration;
    }


    public List<Map<String, Object>> getSomeData() {
        try {
            dataSourceConfiguration.dataSourceUCanAccess();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
      return Collections.emptyList();
    }

    public Collection<Personel> getPersonel() {
        List<Personel> result = new ArrayList<>();
        try {
            Connection sqlConnection = dataSourceConfiguration.getSqlConnection();
            Statement statement = sqlConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM [Personel]");
            Personel personel;
            int id = 1;
            while (resultSet.next()) {
                String name = resultSet.getString(2);
                String lastName = resultSet.getString(3);
                personel = new Personel(id++, name, lastName);
                result.add(personel);
            }
            return result;

        } catch (ClassNotFoundException | SQLException e) {
            LOG.error("Sql error", e);
            return Collections.emptyList();
        }
    }
}
