package by.trainig.module2.repository;

import by.trainig.module2.model.TextLeaf;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class TextRepositoryImpl<T extends TextLeaf> implements TextRepository<T> {

    protected Map<Long, T> leaves;

    public TextRepositoryImpl() {
        leaves = new HashMap<>();
    }

    @Override
    public T read(long id) {
        return leaves.get(id);
    }

    @Override
    public boolean delete(long id) {
        if (id == 0) {
            return false;
        }
        if (!this.leaves.containsKey(id)) {
            return false;
        }
        this.leaves.remove(id);
        return true;
    }

    @Override
    public boolean update(T textLeaf) {
        if (textLeaf.getId() == 0) {
            return false;
        }
        if (!this.leaves.containsKey(textLeaf.getId())) {
            return false;
        }
        leaves.put(textLeaf.getId(), textLeaf);
        return true;
    }

    @Override
    public List<T> getAll() {
        return new LinkedList<>(this.leaves.values());
    }

    @Override
    public List<T> find(TextPartSpecification<T> spec) {
        List<T> wordLeaves = new LinkedList<>();
        for (T w : getAll()) {
            if (spec.match(w)) {
                wordLeaves.add(w);
            }
        }
        return wordLeaves;
    }
}
