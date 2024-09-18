package com.jaworski.dbaccessservice.configuration;

import com.jaworski.dbaccessservice.resources.AppResources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class DataSourceConfigurationTest {

    @Mock
    private AppResources appResources;

    @InjectMocks
    private DataSourceConfiguration dataSourceConfiguration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSqlConnection_Success() throws SQLException, ClassNotFoundException {
        // Arrange
        String fileDbPath = "db/kurs2002.mdb";
        when(appResources.getFileDbPath()).thenReturn(Optional.of(fileDbPath));
        MockedStatic<DriverManager> mocked = mockStatic(DriverManager.class);
        Connection mockConnection = mock(Connection.class);
        mocked.when(() -> DriverManager.getConnection(anyString())).thenReturn(mockConnection);

        // Use a mock DriverManager to simulate database connection
        when(dataSourceConfiguration.getSqlConnection()).thenReturn(mockConnection);

        // Act
        Connection connection = dataSourceConfiguration.getSqlConnection();

        // Assert
        assertNotNull(connection);
        mocked.close();
    }

    @Test
    void testGetSqlConnection_FileNotFound() {
        // Arrange
        when(appResources.getFileDbPath()).thenReturn(Optional.of("wiredFile.mdb"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> dataSourceConfiguration.getSqlConnection());

        assertTrue(exception.getMessage().contains("File not found:"));
    }

}