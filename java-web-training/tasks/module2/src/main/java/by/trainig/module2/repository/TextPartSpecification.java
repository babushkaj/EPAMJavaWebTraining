package by.trainig.module2.repository;

import by.trainig.module2.model.TextLeaf;

public interface TextPartSpecification<T extends TextLeaf> {
    boolean match(T entity);
}
