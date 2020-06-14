package by.training.module4.builder;

import by.training.module4.model.Ferry;

import java.util.Map;

public class FerryBuilder implements Builder<Ferry> {
    @Override
    public Ferry build(Map<String, String> ferryInfo) {
        int maxWeight = Integer.parseInt(ferryInfo.get("maxWeight"));
        int maxArea = Integer.parseInt(ferryInfo.get("maxArea"));

        return new Ferry(maxWeight, maxArea);
    }
}
