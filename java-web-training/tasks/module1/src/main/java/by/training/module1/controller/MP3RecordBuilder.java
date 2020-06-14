package by.training.module1.controller;

import by.training.module1.model.*;
import by.training.module1.parser.ID3TagsParser;

import java.util.Map;

public class MP3RecordBuilder implements RecordBuilder {

    @Override
    public MP3Record build(Map<String, String> oneRecordString) {

        String author = oneRecordString.get("author");
        String title = oneRecordString.get("title");
        int duration = Integer.parseInt(oneRecordString.get("duration"));
        Style style = Style.valueOf(oneRecordString.get("style").toUpperCase());
        Compression compression = Compression.valueOf(oneRecordString.get("comp").toUpperCase());
        ID3TagsParser parser = new ID3TagsParser();
        Map<String, String> ID3Tags = parser.parseTags(oneRecordString.get("id3tags"));

        return new MP3Record(author, title, duration, style, compression, ID3Tags);
    }
}
