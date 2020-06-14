package by.trainig.module2.repository;

import by.trainig.module2.model.WholeTextComposite;

public class WholeTextCompositeRepository extends TextRepositoryImpl<WholeTextComposite> {

    private static long id = 1;

    @Override
    public long create(WholeTextComposite textLeaf) {
        textLeaf.setId(id);
        this.leaves.put(id, textLeaf);
        return id++;
    }

}
