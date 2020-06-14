package by.training.module1.parser;

import java.util.HashMap;
import java.util.Map;

public class LineParser {

    public Map<String, String> parseString(String line){
        Map<String, String> oneModelInfo = new HashMap<>();

        String [] allKeyValuePairs = line.split(";");

        for (String keyValue: allKeyValuePairs) {
            String [] keyValueArr = keyValue.split(":");
            oneModelInfo.put(keyValueArr[0].trim(), keyValueArr[1].trim());
        }

        return oneModelInfo;
    }

}
