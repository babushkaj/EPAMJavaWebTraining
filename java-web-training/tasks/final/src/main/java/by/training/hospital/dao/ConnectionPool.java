package by.training.hospital.dao;

import java.sql.Connection;

public interface ConnectionPool {
    void init(String dbProperty, int poolSize) throws ConnectionPoolException;

    Connection getConnection() throws ConnectionPoolException;

    void destroy() throws ConnectionPoolException;
}
