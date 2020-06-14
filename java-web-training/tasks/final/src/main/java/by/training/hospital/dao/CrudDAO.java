package by.training.hospital.dao;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface CrudDAO<K, T> {

    long create(T entity) throws DAOException;

    T getById(K id) throws DAOException, NoConcreteEntityInDatabaseException;

    boolean update(T entity) throws DAOException;

    boolean delete(K id) throws DAOException;

    List<T> getAll() throws DAOException;

    default void closeStatement(Statement s) throws DAOException {
        try {
            s.close();
        } catch (SQLException e) {
            throw new DAOException("Error during closing Statement.");
        }
    }

}
