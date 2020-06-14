package by.training.module1.model;

public class CDDARecord extends Record {

    private Compression compression;

    public CDDARecord(String author, String title, int duration, Style style, Compression compression) {
        super(author, title, duration, style, Format.CDDA);
        this.compression = compression;
    }

    public Compression getCompression() {
        return compression;
    }

    public void setCompression(Compression compression) {
        this.compression = compression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CDDARecord record = (CDDARecord) o;

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
        return result;
    }

    @Override
    public String toString() {
        return "Record{" +
                "author='" + super.getAuthor() + '\'' +
                ", title='" + super.getTitle() + '\'' +
                ", duration=" + super.getDuration() +
                ", style=" + super.getStyle() +
                ", format=" + super.getFormat() +
                ", compression=" + compression +
                '}';
    }
}
