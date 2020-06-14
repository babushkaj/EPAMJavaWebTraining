package by.training.module1.model;

import java.util.Map;

public class MP3Record extends Record{

    private Compression compression;
    private Map<String, String> ID3Tags;

    public MP3Record(String author, String title, int duration, Style style, Compression compression,
                     Map<String, String> ID3Tags) {
        super(author, title, duration, style, Format.MP3);
        this.compression = compression;
        this.ID3Tags = ID3Tags;
    }

    public Compression getCompression() {
        return compression;
    }

    public void setCompression(Compression compression) {
        this.compression = compression;
    }

    public Map<String, String> getID3Tags() {
        return ID3Tags;
    }

    public void setID3Tags(Map<String, String> ID3Tags) {
        this.ID3Tags = ID3Tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MP3Record record = (MP3Record) o;

        if (super.getDuration() != record.getDuration()) return false;
        if (!super.getAuthor().equals(record.getAuthor())) return false;
        if (!super.getTitle().equals(record.getTitle())) return false;
        if (super.getStyle() != record.getStyle()) return false;
        if (super.getFormat() != record.getFormat()) return false;
        return compression == record.compression;
    }

    @Override
    public int hashCode() {
        int result = super.getAuthor().hashCode();
        result = 31 * result + super.getTitle().hashCode();
        result = 31 * result + super.getDuration();
        result = 31 * result + super.getStyle().hashCode();
        result = 31 * result + super.getFormat().hashCode();
        result = 31 * result + compression.hashCode();
        result = 31 * result + ID3Tags.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> e: ID3Tags.entrySet()) {
            sb.append(e.getKey());
            sb.append(" : ");
            sb.append(e.getValue());
            sb.append(", ");
        }
        sb.delete(sb.length()-2, sb.length());

        return "Record{" +
                "author='" + super.getAuthor() + '\'' +
                ", title='" + super.getTitle() + '\'' +
                ", duration=" + super.getDuration() +
                ", style=" + super.getStyle() +
                ", format=" + super.getFormat() +
                ", compression=" + compression +
                ", ID3Tags=[" + sb.toString() +
                "]}";
    }
}
