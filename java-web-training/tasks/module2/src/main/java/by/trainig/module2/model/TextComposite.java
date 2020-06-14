package by.trainig.module2.model;

import java.util.List;

public interface TextComposite extends TextLeaf {
    void addText(TextLeaf text);

    List<TextLeaf> getTextLeaves();
}
