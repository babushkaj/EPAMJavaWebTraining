package by.training.module3.repository;

import java.util.List;
import java.util.Optional;

public interface GemsRepository<T> {
    void add(T gem);

    Optional<T> read(long id);

    List<T> getAll();
}
