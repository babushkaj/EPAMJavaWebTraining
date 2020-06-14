package by.trainig.module2.model;

import java.util.LinkedList;
import java.util.List;

public class ParagraphComposite implements TextComposite {

    private long id;
    private long parNumber;
    private List<TextLeaf> sentences;

    public ParagraphComposite(long parNumber) {
        this.parNumber = parNumber;
        sentences = new LinkedList<>();
    }

    @Override
    public void addText(TextLeaf text) {
        sentences.add(text);
    }

    @Override
    public List<TextLeaf> getTextLeaves() {
        return new LinkedList<>(this.sentences);
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
        sb.append("\t");

        for (TextLeaf t : this.sentences) {
            sb.append(t.getText());
        }

        sb.append("\n");

        return sb.toString();
    }


}
