package by.training.module1.model;

public abstract class Record {
    private String author;
    private String title;
    private int duration;
    private Style style;
    private Format format;

    public Record(String author, String title, int duration, Style style, Format format) {
        this.author = author;
        this.title = title;
        this.duration = duration;
        this.style = style;
        this.format = format;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }
}
