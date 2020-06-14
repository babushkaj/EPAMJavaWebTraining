package by.trainig.module2.repository;

import by.trainig.module2.model.SentenceComposite;

public class ByParagraphSentenceCompositeSpec implements TextPartSpecification<SentenceComposite> {
    private long parId;

    public ByParagraphSentenceCompositeSpec(long parId) {
        this.parId = parId;
    }

    @Override
    public boolean match(SentenceComposite entity) {
        return entity.getParNumber() == parId;
    }
}
