package by.trainig.module2.repository;

import by.trainig.module2.model.WordLeaf;

public class WordLeafRepository extends TextRepositoryImpl<WordLeaf> {

    private static long id = 1;

    @Override
    public long create(WordLeaf textLeaf) {
        textLeaf.setId(id);
        this.leaves.put(id, textLeaf);
        return id++;
    }

}
