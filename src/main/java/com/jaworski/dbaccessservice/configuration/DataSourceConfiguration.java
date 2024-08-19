package com.jaworski.dbaccessservice.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DataSourceConfiguration {

    private static final Logger LOG = LogManager.getLogger(DataSourceConfiguration.class);
    private final AppResources appResources;

    public DataSourceConfiguration(AppResources appResources) {
        this.appResources = appResources;
    }

    public void dataSourceUCanAccess() throws SQLException, ClassNotFoundException {
        Connection connection = getSqlConnection();
        Statement s = connection.createStatement();
        ResultSet resultSet = s.executeQuery("SELECT * FROM [Personel]");
        while (resultSet.next()) {
            LOG.info("{} {} {} {} {}", resultSet.getRow(), resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4));
        }
        s.closeOnCompletion();
        connection.close();
    }

    public Connection getSqlConnection() throws SQLException, ClassNotFoundException {
        System.setProperty("hsqldb.method_class_names", "net.ucanaccess.converters.*"); // see http://hsqldb.org/doc/2.0/guide/sqlroutines-chapt.html#src_jrt_access_control
        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        Path path = getPathToFileDB();
        return DriverManager.getConnection("jdbc:ucanaccess://" + path);
    }

    private Path getPathToFileDB() {
        Path path = Path.of(".");
        String fileDbPath = appResources.getFileDbPath()
                .orElseThrow(() -> new RuntimeException("Resource not found: file.db"));
        path = path.resolve(fileDbPath);
        if (!path.toFile().isFile()) {
            LOG.error("File not found: {}", path);
            throw new RuntimeException("File not found: " + path);
        }
        return path;
    }
}
