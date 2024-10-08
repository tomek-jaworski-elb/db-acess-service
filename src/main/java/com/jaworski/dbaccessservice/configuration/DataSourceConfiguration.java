package com.jaworski.dbaccessservice.configuration;

import com.jaworski.dbaccessservice.resources.AppResources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DataSourceConfiguration {

    private static final Logger LOG = LogManager.getLogger(DataSourceConfiguration.class);
    private static final String NET_UCANACCESS_JDBC_UCANACCESS_DRIVER = "net.ucanaccess.jdbc.UcanaccessDriver";
    private static final String JDBC_UCANACCESS = "jdbc:ucanaccess://";
    private final AppResources appResources;

    public DataSourceConfiguration(AppResources appResources) {
        this.appResources = appResources;
    }

    public Connection getSqlConnection() throws SQLException, ClassNotFoundException {
        System.setProperty("hsqldb.method_class_names", "net.ucanaccess.converters.*"); // see http://hsqldb.org/doc/2.0/guide/sqlroutines-chapt.html#src_jrt_access_control
        Class.forName(NET_UCANACCESS_JDBC_UCANACCESS_DRIVER);
        Path path = getPathToFileDB();
        return DriverManager.getConnection(JDBC_UCANACCESS + path + ";memory=false");
    }

    private Path getPathToFileDB() {
        Path path = Path.of(".");
        String fileDbPath = appResources.getFileDbPath()
                .orElseThrow(() -> new RuntimeException("Resource not found: " + appResources.getFileDbPath()));
        path = path.resolve(fileDbPath);
        if (!path.toFile().isFile()) {
            LOG.error("File not found: {}", path);
            throw new RuntimeException("File not found: " + path);
        }
        return path;
    }
}
