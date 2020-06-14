package by.training.hospital.dao;

import org.apache.log4j.Logger;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPoolImpl implements ConnectionPool {
    private static final Logger LOGGER = Logger.getLogger(ConnectionPoolImpl.class);

    private static final String POOL_PROPERTIES_ERROR = "Error during initialization ConnectionPool." +
            " Parameters 'driver', 'db_url', 'db_user', 'password' are necessary.";
    private static final String DRIVER_LOADING_ERROR = "JDBC driver couldn't be loaded.";
    private static final String GET_CONNECTION_FROM_MANAGER_ERROR = "Exception during getting Connection from DriverManager.";
    private static final String CONNECTION_FROM_POOL_ERROR = "Exception during getting connection from the pool.";
    private static final String CONNECTION_IN_POOL_ERROR = "Exception during returning connection in the pool.";
    private static final String CONNECTION_CLOSING_ERROR = "Exception during closing connection.";

    private static ReentrantLock lock = new ReentrantLock();

    private static ConnectionPool instance;
    private String dbURL;
    private String user;
    private String password;
    private String dbDriver;
    private BlockingQueue<Connection> availableConnections;
    private BlockingQueue<Connection> takenConnections;

    private ConnectionPoolImpl() {

    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            try {
                lock.lock();
                if (instance == null) {
                    instance = new ConnectionPoolImpl();
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    @Override
    public final void init(String dbProperty, int poolSize) throws ConnectionPoolException {
        try {

            ResourceBundle resource = ResourceBundle.getBundle(dbProperty);

            if (resource != null) {
                dbDriver = resource.getString("driver");
                dbURL = resource.getString("db_url");
                user = resource.getString("user");
                password = resource.getString("password");
            }

            Class.forName(dbDriver);

            availableConnections = new ArrayBlockingQueue<>(poolSize);
            takenConnections = new ArrayBlockingQueue<>(poolSize);

            for (int i = 0; i < poolSize; i++) {
                Connection connection = DriverManager.getConnection(dbURL, user, password);
                availableConnections.add(connection);
            }

        } catch (MissingResourceException e) {
            LOGGER.error(POOL_PROPERTIES_ERROR, e);
            throw new ConnectionPoolException(POOL_PROPERTIES_ERROR, e);
        } catch (ClassNotFoundException e) {
            LOGGER.error(DRIVER_LOADING_ERROR, e);
            throw new ConnectionPoolException(DRIVER_LOADING_ERROR, e);
        } catch (SQLException e) {
            LOGGER.error(GET_CONNECTION_FROM_MANAGER_ERROR, e);
            throw new ConnectionPoolException(GET_CONNECTION_FROM_MANAGER_ERROR, e);
        }
    }

    @Override
    public Connection getConnection() throws ConnectionPoolException {
        try {
            Connection connection = availableConnections.take();
            takenConnections.put(connection);
            return createProxyConnection(connection);
        } catch (InterruptedException e) {
            LOGGER.error(CONNECTION_FROM_POOL_ERROR, e);
            throw new ConnectionPoolException(CONNECTION_FROM_POOL_ERROR, e);
        }
    }

    private void releaseConnection(Connection connection) throws SQLException {
        try {
            takenConnections.remove(connection);
            availableConnections.put(connection);
        } catch (InterruptedException e) {
            LOGGER.error(CONNECTION_IN_POOL_ERROR, e);
            throw new SQLException(CONNECTION_IN_POOL_ERROR, e);
        }
    }

    public void destroy() throws ConnectionPoolException {
        try {
            Connection connection;
            while (availableConnections.size() > 0) {
                connection = availableConnections.poll();
                connection.close();
            }
            while (takenConnections.size() > 0) {
                connection = takenConnections.poll();
                connection.close();
            }
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                if (driver.getClass() == Class.forName(dbDriver)) {
                    DriverManager.deregisterDriver(driver);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.error(CONNECTION_CLOSING_ERROR, e);
            throw new ConnectionPoolException(CONNECTION_CLOSING_ERROR, e);
        }
    }

    private Connection createProxyConnection(Connection connection) {

        return (Connection) Proxy.newProxyInstance(connection.getClass().getClassLoader(),
                new Class[]{Connection.class},
                (proxy, method, args) -> {
                    if ("close".equals(method.getName())) {
                        releaseConnection(connection);
                        return null;
                    } else if ("hashCode".equals(method.getName())) {
                        return connection.hashCode();
                    } else {
                        return method.invoke(connection, args);
                    }
                });
    }
}
