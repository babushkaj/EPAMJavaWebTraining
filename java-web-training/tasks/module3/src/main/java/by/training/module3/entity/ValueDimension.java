package by.training.module3.entity;

public enum ValueDimension {
    CARAT("c."), GRAM("g.");

    String value;

    ValueDimension(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
