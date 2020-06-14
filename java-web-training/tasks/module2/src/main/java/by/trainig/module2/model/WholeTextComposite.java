package by.trainig.module2.model;

import java.util.LinkedList;
import java.util.List;

public class WholeTextComposite implements TextComposite {

    private long id;
    private List<TextLeaf> paragraphs;

    public WholeTextComposite() {
        paragraphs = new LinkedList<>();
    }

    @Override
    public void addText(TextLeaf text) {
        paragraphs.add(text);
    }

    @Override
    public List<TextLeaf> getTextLeaves() {
        return new LinkedList<>(this.paragraphs);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder();

        for (TextLeaf t : this.paragraphs) {
            sb.append(t.getText());
        }

        return sb.toString();
    }
}
