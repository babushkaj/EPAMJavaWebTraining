package by.trainig.module2.repository;

import by.trainig.module2.model.SentenceComposite;

public class SentenceCompositeRepository extends TextRepositoryImpl<SentenceComposite> {

    private static long id = 1;

    @Override
    public long create(SentenceComposite textLeaf) {
        textLeaf.setId(id);
        this.leaves.put(id, textLeaf);
        return id++;
    }
}
