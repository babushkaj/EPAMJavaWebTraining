package by.training.hospital.dao;

import java.sql.Connection;

public interface TransactionManager {

    void beginTransaction();

    void commitTransaction();

    void rollback();

    Connection getConnection();

    ConnectionPool getConnectionPool();
}
