package by.trainig.module2.service;

import by.trainig.module2.model.SentenceComposite;
import by.trainig.module2.repository.ByParagraphSentenceCompositeSpec;
import by.trainig.module2.repository.SentenceCompositeRepository;

import java.util.Comparator;
import java.util.List;

public class SentenceCompositeService {
    private SentenceCompositeRepository repository;

    public SentenceCompositeService(SentenceCompositeRepository repository) {
        this.repository = repository;
    }

    public void create(SentenceComposite sentence) {
        repository.create(sentence);
    }

    public SentenceComposite read(long id) {
        return repository.read(id);
    }

    public boolean delete(long id) {
        return repository.delete(id);
    }

    public boolean update(SentenceComposite textLeaf) {
        return repository.update(textLeaf);
    }

    public List<SentenceComposite> getAllWords() {
        return repository.getAll();
    }

    public List<SentenceComposite> getAllSentences() {
        return repository.getAll();
    }

    List<SentenceComposite> sortSentences(long parNum, Comparator<SentenceComposite> comparator) {
        List<SentenceComposite> list = repository.find(new ByParagraphSentenceCompositeSpec(parNum));
        list.sort(comparator);
        return list;
    }
}
