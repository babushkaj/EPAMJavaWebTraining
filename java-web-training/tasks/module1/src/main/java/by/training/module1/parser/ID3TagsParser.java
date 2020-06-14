package by.training.module1.parser;

import java.util.HashMap;
import java.util.Map;

public class ID3TagsParser {

    public Map<String, String> parseTags(String tagsLine){
        Map<String, String> ID3TagsMap = new HashMap<>();

        String [] keyValuePairs = tagsLine.split(",");
        for (String keyValue: keyValuePairs) {
            String [] keyValueArr = keyValue.split("\\*");
            ID3TagsMap.put(keyValueArr[0].trim(), keyValueArr[1].trim());
        }

        return ID3TagsMap;
    }
}
