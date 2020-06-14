package by.training.module1.controller;

import by.training.module1.model.CDDARecord;
import by.training.module1.model.Compression;
import by.training.module1.model.Style;

import java.util.Map;

public class CDDARecordBuilder implements RecordBuilder {

    @Override
    public CDDARecord build(Map<String, String> oneRecordString) {

        String author = oneRecordString.get("author");
        String title = oneRecordString.get("title");
        int duration = Integer.parseInt(oneRecordString.get("duration"));
        Style style = Style.valueOf(oneRecordString.get("style").toUpperCase());
        Compression compression = Compression.valueOf(oneRecordString.get("comp").toUpperCase());

        return new CDDARecord(author, title, duration, style, compression);
    }
}
