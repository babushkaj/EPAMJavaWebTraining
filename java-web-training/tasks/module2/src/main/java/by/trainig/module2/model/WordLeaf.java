package by.trainig.module2.model;

public class WordLeaf implements TextLeaf {
    private long id;
    private long wordNum;
    private long sentNumber;
    private long parNumber;
    private String word;
    private String starting;
    private String ending;

    public WordLeaf(long wordNum, long sentNumber, long parNumber, String starting, String word, String ending) {
        this.wordNum = wordNum;
        this.sentNumber = sentNumber;
        this.parNumber = parNumber;
        this.word = word;
        this.starting = starting;
        this.ending = ending;
    }

    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder();
        if (starting != null) {
            sb.append(starting);
        }
        sb.append(word);
        if (ending != null) {
            sb.append(ending);
        }
        return sb.toString();
    }

    public long getWordNum() {
        return wordNum;
    }

    public long getSentNumber() {
        return sentNumber;
    }

    public long getParNumber() {
        return parNumber;
    }

    public String getWord() {
        return word;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }
}
