package by.training.module4.builder;

import java.util.Map;

public interface Builder<T> {
    T build(Map<String, String> carInfo);
}
