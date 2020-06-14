package by.trainig.module2.repository;

import by.trainig.module2.model.ParagraphComposite;

public class ParagraphCompositeRepository extends TextRepositoryImpl<ParagraphComposite> {

    private static long id = 1;

    @Override
    public long create(ParagraphComposite textLeaf) {
        textLeaf.setId(id);
        this.leaves.put(id, textLeaf);
        return id++;
    }

}
