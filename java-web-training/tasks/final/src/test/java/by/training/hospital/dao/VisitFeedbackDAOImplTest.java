package by.training.hospital.dao;

import by.training.hospital.dto.VisitFeedbackDTO;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

//@Ignore
@RunWith(JUnit4.class)
public class VisitFeedbackDAOImplTest {


    private static final String DB_PROPERTY = "database";
    private static ConnectionManager connectionManager;
    private static ConnectionPool connectionPool;

    @BeforeClass
    public static void initPool() throws ConnectionPoolException {
        connectionPool = ConnectionPoolImpl.getInstance();
        connectionPool.init(DB_PROPERTY, 5);
        TransactionManager transactionManager = new TransactionManagerImpl(connectionPool);
        connectionManager = new ConnectionManagerImpl(transactionManager);
    }

    @AfterClass
    public static void destroyPool() throws ConnectionPoolException {
        connectionPool.destroy();
    }

    @Before
    public void createTable() throws SQLException {
        String createRoleTable = "CREATE TABLE user_role" +
                " (id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1)," +
                "  role_name VARCHAR(10) NOT NULL," +
                " PRIMARY KEY (id))";

        executeSql(createRoleTable);

        String createVisitStatusTable = "CREATE TABLE visit_status (" +
                "  id          BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1)," +
                "  status_name VARCHAR(10) NOT NULL," +
                "  PRIMARY KEY (id))";

        executeSql(createVisitStatusTable);

        String createUserAccountTable = "CREATE TABLE user_account (" +
                "  id         BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1)," +
                "  login      VARCHAR(25) UNIQUE  NOT NULL," +
                "  password   VARCHAR(35)         NOT NULL," +
                "  is_blocked BOOLEAN DEFAULT false NOT NULL," +
                "  role_id    BIGINT DEFAULT 2 NOT NULL," +
                "  PRIMARY KEY (id)," +
                "  FOREIGN KEY (role_id) REFERENCES user_role (id))";

        executeSql(createUserAccountTable);

        String createVisitTable = "CREATE TABLE visit (" +
                "  id           BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1)," +
                "  cause        VARCHAR(300) NOT NULL," +
                "  result       VARCHAR(600)," +
                "  visit_date   BIGINT    NOT NULL," +
                "  visitor_id   BIGINT       NOT NULL," +
                "  doctor_id    BIGINT       NOT NULL," +
                "  status_id    BIGINT       NOT NULL," +
                "  PRIMARY KEY (id)," +
                "  FOREIGN KEY (visitor_id) REFERENCES user_account (id)," +
                "  FOREIGN KEY (doctor_id) REFERENCES user_account (id)," +
                "  FOREIGN KEY (status_id) REFERENCES visit_status (id))";

        executeSql(createVisitTable);

        String createVisitFeedbackTable = "CREATE TABLE visit_feedback (" +
                "  id           BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1)," +
                "  visitor_mess VARCHAR(300)," +
                "  doctor_mess  VARCHAR(300)," +
                "  visit_id     BIGINT  NOT NULL," +
                "  PRIMARY KEY (id)," +
                "  FOREIGN KEY (visit_id) REFERENCES visit (id))";

        executeSql(createVisitFeedbackTable);

        String insertRoles = "INSERT INTO user_role (role_name)" +
                " VALUES ('ADMIN')," +
                "       ('VISITOR')," +
                "       ('DOCTOR')";

        executeSql(insertRoles);

        String insertVisitStatuses = "INSERT INTO visit_status (status_name) " +
                " VALUES ('PLANNED')," +
                "       ('CANCELED')," +
                "       ('COMPLETED')";

        executeSql(insertVisitStatuses);

        String insertUserAccounts = "INSERT INTO user_account (login, password, role_id)" +
                " VALUES ('Doctor1', 'd8578edf8458ce06fbc5bb76a58c5ca4', 3)," +
                "       ('Doctor2', '4e5fd8ab9c0e3e84ab141a65e36ef094', 3)," +
                "       ('Doctor3', 'bec92fe29583ad9a32a1f1b4d1df5b01', 3)";

        executeSql(insertUserAccounts);

        String insertVisits = "INSERT INTO visit (cause, result, visit_date, visitor_id, doctor_id, status_id) " +
                " VALUES ('Головная боль.', null, 1573812000000, 1, 3, 1)," +
                "       ('Плановая проверка зрения.', null, 1575270000000, 2, 3, 1)," +
                "       ('Болит правое ухо.', null, 1575268200000, 1, 3, 1)";

        executeSql(insertVisits);

        String insertVisitFeedbacks = "INSERT INTO visit_feedback (visitor_mess, doctor_mess, visit_id) " +
                " VALUES ('Большое спасибо врачу1.', 'Благодарю за отзыв1.', 1)," +
                "       ('Большое спасибо врачу2.', 'Благодарю за отзыв2.', 2)," +
                "       ('Большое спасибо врачу3.', 'Благодарю за отзыв3.', 3)";

        executeSql(insertVisitFeedbacks);
    }

    @After
    public void deleteTables() throws SQLException {
        String dropVisitFeedbacks = "DROP TABLE visit_feedback";
        String dropVisits = "DROP TABLE visit";
        String dropUsers = "DROP TABLE user_account";
        String dropVisitStatuses = "DROP TABLE visit_status";
        String dropRoles = "DROP TABLE user_role";
        executeSql(dropVisitFeedbacks);
        executeSql(dropVisits);
        executeSql(dropUsers);
        executeSql(dropVisitStatuses);
        executeSql(dropRoles);
    }

    private void executeSql(String sql) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:hospital", "sa", "");
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.executeUpdate();
        statement.close();
        connection.close();
    }

    @Test
    public void shouldReturnVisitFeedback() throws NoConcreteEntityInDatabaseException, DAOException {
        VisitFeedbackDAO visitFeedbackDAO = new VisitFeedbackDAOImpl(connectionManager);
        VisitFeedbackDTO visitFeedbackDTO = visitFeedbackDAO.getById(1L);

        Assert.assertEquals(visitFeedbackDTO.getDoctorMess(), "Благодарю за отзыв1.");
    }

    @Test
    public void shouldAddVisitFeedback() throws DAOException {
        VisitFeedbackDAO visitFeedbackDAO = new VisitFeedbackDAOImpl(connectionManager);
        VisitFeedbackDTO visitFeedbackDTO = new VisitFeedbackDTO();
        visitFeedbackDTO.setVisitorMess("Тестовое спасибо доктору.");
        visitFeedbackDTO.setDoctorMess("Тестовое благодарю за отзыв.");
        visitFeedbackDTO.setVisitId(3L);

        long visitFeedbackId = visitFeedbackDAO.create(visitFeedbackDTO);

        Assert.assertEquals(visitFeedbackId, 4);
    }

    @Test
    public void shouldUpdateVisitFeedback() throws NoConcreteEntityInDatabaseException, DAOException {
        VisitFeedbackDAO visitFeedbackDAO = new VisitFeedbackDAOImpl(connectionManager);
        VisitFeedbackDTO visitFeedbackDTO = visitFeedbackDAO.getById(1L);
        visitFeedbackDTO.setDoctorMess("Новое сообщение от врача.");
        boolean result = visitFeedbackDAO.update(visitFeedbackDTO);
        VisitFeedbackDTO visitFeedbackUpdatedDTO = visitFeedbackDAO.getById(1L);

        Assert.assertTrue(result);
        Assert.assertEquals(visitFeedbackUpdatedDTO.getDoctorMess(), "Новое сообщение от врача.");
    }

    @Test
    public void shouldDeleteVisitFeedback() throws DAOException {
        VisitFeedbackDAO visitFeedbackDAO = new VisitFeedbackDAOImpl(connectionManager);
        boolean result = visitFeedbackDAO.delete(1L);
        List<VisitFeedbackDTO> visitFeedbacks = visitFeedbackDAO.getAll();

        Assert.assertTrue(result);
        Assert.assertEquals(visitFeedbacks.size(), 2);
    }

    @Test
    public void shouldReturnAllVisitFeedbacks() throws DAOException {
        VisitFeedbackDAO visitFeedbackDAO = new VisitFeedbackDAOImpl(connectionManager);
        List<VisitFeedbackDTO> visitFeedbacks = visitFeedbackDAO.getAll();

        Assert.assertEquals(visitFeedbacks.size(), 3);
    }

    @Test
    public void shouldReturnVisitFeedbackByVisitId() throws DAOException {
        VisitFeedbackDAO visitFeedbackDAO = new VisitFeedbackDAOImpl(connectionManager);
        VisitFeedbackDTO visitFeedbackDTO = visitFeedbackDAO.getByVisitId(1L);

        Assert.assertEquals(visitFeedbackDTO.getDoctorMess(), "Благодарю за отзыв1.");
    }

}
