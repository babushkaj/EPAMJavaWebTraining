package by.training.hospital.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManagerImpl implements ConnectionManager {

    private ConnectionPool connectionPool;
    private TransactionManager transactionManager;

    public ConnectionManagerImpl(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        this.connectionPool = transactionManager.getConnectionPool();
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            if (transactionManager != null) {
                connection = transactionManager.getConnection() != null ? transactionManager.getConnection() : connectionPool.getConnection();
            } else {
                throw new NullPointerException("TransactionManager in ConnectionManager is nat initialized.");
            }

        } catch (ConnectionPoolException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void releaseConnection(Connection connection) {
        if (transactionManager == null || transactionManager.getConnection() == null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
