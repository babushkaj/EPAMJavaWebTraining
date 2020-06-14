package by.training.hospital.dao;

import java.sql.Connection;

public interface ConnectionManager {
    Connection getConnection();

    void releaseConnection(Connection connection);

}
