package by.trainig.module2.repository;

import by.trainig.module2.model.WordLeaf;

public class ByParagraphAndSentenceWordLeafSpec implements TextPartSpecification<WordLeaf> {
    private long parNum;
    private long sentNum;

    public ByParagraphAndSentenceWordLeafSpec(long parNum, long sentNum) {
        this.parNum = parNum;
        this.sentNum = sentNum;
    }

    @Override
    public boolean match(WordLeaf entity) {
        return entity.getParNumber() == parNum && entity.getSentNumber() == sentNum;
    }
}
