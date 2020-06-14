package by.training.hospital.dao;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManagerImpl implements TransactionManager {

    private static final Logger LOGGER = Logger.getLogger(TransactionManagerImpl.class);

    private ThreadLocal<Connection> localConnection = new ThreadLocal<>();
    private ConnectionPool connectionPool;

    public TransactionManagerImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void beginTransaction() {
        try {
            if (localConnection.get() == null) {
                Connection connection = connectionPool.getConnection();
                connection.setAutoCommit(false);
                localConnection.set(connection);
            } else {
                throw new IllegalStateException("Method beginTransaction was used during transaction had already started.");
            }
        } catch (SQLException | ConnectionPoolException e) {
            LOGGER.error("Error in TransactionManager.", e);
        }
    }

    public void commitTransaction() {
        try {
            Connection connection = localConnection.get();
            if (connection != null) {
                connection.commit();
                connection.setAutoCommit(true);
                connection.close();
                localConnection.remove();
            } else {
                throw new NullPointerException("Method endTransaction was used before this transaction's beginning");
            }
        } catch (SQLException | NullPointerException e) {
            LOGGER.error("Error in TransactionManager.", e);
        }
    }

    public void rollback() {
        try {
            Connection connection = localConnection.get();
            if (connection != null) {
                connection.rollback();
                connection.setAutoCommit(true);
                connection.close();
                localConnection.remove();
            } else {
                throw new NullPointerException("Method rollback was used before this transaction's beginning");
            }
        } catch (SQLException | NullPointerException e) {
            LOGGER.error("Error in TransactionManager.", e);
        }
    }

    public Connection getConnection() {
        return localConnection.get();
    }

    @Override
    public ConnectionPool getConnectionPool() {
        return this.connectionPool;
    }

}
