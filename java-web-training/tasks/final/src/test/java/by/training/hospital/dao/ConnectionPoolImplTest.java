package by.training.hospital.dao;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@RunWith(JUnit4.class)
public class ConnectionPoolImplTest {
    private static final String DB_PROPERTY = "database";

    private static ConnectionPool connectionPool;

    @Before
    public void initPool() throws ConnectionPoolException {
        connectionPool = ConnectionPoolImpl.getInstance();
        connectionPool.init(DB_PROPERTY, 5);
    }

    @Test
    public void shouldGetConnection() throws ConnectionPoolException {
        Connection connection = connectionPool.getConnection();
        Assert.assertNotNull(connection);
    }

    @Test
    public void sixthThreadShouldWait() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
           new ConnectionTaker().start();
        }
        Thread t6 = new ConnectionTaker();
        t6.start();
        Thread.sleep(200);
        Set<Thread.State> states = new HashSet<>();
        states.add(Thread.State.WAITING);
        states.add(Thread.State.TIMED_WAITING);
        Assert.assertTrue(states.contains(t6.getState()));
    }

    @After
    public void destroyPool() throws ConnectionPoolException {
        connectionPool.destroy();
    }

    private class ConnectionTaker extends Thread {
        @Override
        public void run() {
            try {
                Connection connection = connectionPool.getConnection();
                Thread.sleep(1000);
                connection.close();
            } catch (ConnectionPoolException | InterruptedException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
