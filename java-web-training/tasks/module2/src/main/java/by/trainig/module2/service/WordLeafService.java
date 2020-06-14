package by.trainig.module2.service;

import by.trainig.module2.model.WordLeaf;
import by.trainig.module2.repository.ByParagraphAndSentenceWordLeafSpec;
import by.trainig.module2.repository.WordLeafRepository;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class WordLeafService {
    private WordLeafRepository repository;

    public WordLeafService(WordLeafRepository repository) {
        this.repository = repository;
    }

    public void create(WordLeaf word) {
        repository.create(word);
    }

    public WordLeaf read(long id) {
        return repository.read(id);
    }

    public boolean delete(long id) {
        return repository.delete(id);
    }

    public boolean update(WordLeaf textLeaf) {
        return repository.update(textLeaf);
    }

    public List<WordLeaf> getAllWords() {
        return repository.getAll();
    }

    List<WordLeaf> sortWordsInSentence(long parNum, long sentNum, Comparator<WordLeaf> comparator) {
        List<WordLeaf> list = new LinkedList<>(repository.find(new ByParagraphAndSentenceWordLeafSpec(parNum, sentNum)));
        list.sort(comparator);
        return list;
    }
}
