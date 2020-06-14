package by.trainig.module2.service;

import by.trainig.module2.model.ParagraphComposite;
import by.trainig.module2.model.SentenceComposite;
import by.trainig.module2.model.WholeTextComposite;
import by.trainig.module2.model.WordLeaf;
import by.trainig.module2.repository.WholeTextCompositeRepository;

import java.util.Comparator;
import java.util.List;

public class WholeTextCompositeService {
    private WordLeafService wordService;
    private SentenceCompositeService sentService;
    private ParagraphCompositeService parService;

    private WholeTextCompositeRepository textRepository;

    public WholeTextCompositeService(WordLeafService wordService, SentenceCompositeService sentService,
                                     ParagraphCompositeService parService, WholeTextCompositeRepository textRepository) {
        this.wordService = wordService;
        this.sentService = sentService;
        this.parService = parService;
        this.textRepository = textRepository;
    }

    public void create(WholeTextComposite text) {
        textRepository.create(text);
    }

    public WholeTextComposite read(long id) {
        return textRepository.read(id);
    }

    public boolean delete(long id) {
        return textRepository.delete(id);
    }

    public boolean update(WholeTextComposite textLeaf) {
        return textRepository.update(textLeaf);
    }

    public List<WholeTextComposite> getAllWords() {
        return textRepository.getAll();
    }

    public List<WholeTextComposite> getAllTexts() {
        return textRepository.getAll();
    }

    public List<WordLeaf> sortWordsInSentence(long parNum, long sentNum, Comparator<WordLeaf> comparator) {
        return wordService.sortWordsInSentence(parNum, sentNum, comparator);
    }

    public List<SentenceComposite> sortSentences(long parNum, Comparator<SentenceComposite> comparator) {
        return sentService.sortSentences(parNum, comparator);
    }

    public List<ParagraphComposite> sortParagraphs(Comparator<ParagraphComposite> comparator) {
        return parService.sortParagraphs(comparator);
    }


}
