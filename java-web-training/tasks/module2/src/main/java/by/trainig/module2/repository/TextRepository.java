package by.trainig.module2.repository;

import by.trainig.module2.model.TextLeaf;

import java.util.List;

public interface TextRepository<T extends TextLeaf> {
    long create(T textLeaf);

    T read(long id);

    boolean delete(long id);

    boolean update(T textLeaf);

    List<T> getAll();

    List<T> find(TextPartSpecification<T> spec);
}
