package by.trainig.module2.service;

import by.trainig.module2.model.ParagraphComposite;
import by.trainig.module2.repository.ParagraphCompositeRepository;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ParagraphCompositeService {
    private ParagraphCompositeRepository repository;

    public ParagraphCompositeService(ParagraphCompositeRepository repository) {
        this.repository = repository;
    }

    public void create(ParagraphComposite paragraph) {
        repository.create(paragraph);
    }

    public ParagraphComposite read(long id) {
        return repository.read(id);
    }

    public boolean delete(long id) {
        return repository.delete(id);
    }

    public boolean update(ParagraphComposite textLeaf) {
        return repository.update(textLeaf);
    }

    public List<ParagraphComposite> getAllWords() {
        return repository.getAll();
    }

    public List<ParagraphComposite> getAllParagraphs() {
        return new LinkedList<>(repository.getAll());
    }

    List<ParagraphComposite> sortParagraphs(Comparator<ParagraphComposite> comparator) {
        List<ParagraphComposite> list = getAllParagraphs();
        list.sort(comparator);
        return list;
    }
}
