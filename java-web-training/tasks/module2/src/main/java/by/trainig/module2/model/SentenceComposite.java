package by.trainig.module2.model;

import java.util.LinkedList;
import java.util.List;

public class SentenceComposite implements TextComposite {
    private long id;
    private long sentNum;
    private long parNumber;
    private List<TextLeaf> words;

    public SentenceComposite(long sentNum, long parNumber) {
        this.sentNum = sentNum;
        this.parNumber = parNumber;
        words = new LinkedList<>();
    }

    @Override
    public void addText(TextLeaf text) {
        words.add(text);
    }

    @Override
    public List<TextLeaf> getTextLeaves() {
        return new LinkedList<>(this.words);
    }

    public long getParNumber() {
        return parNumber;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder();

        for (TextLeaf t : this.words) {
            sb.append(t.getText());
            sb.append(" ");
        }

        return sb.toString();
    }


}
