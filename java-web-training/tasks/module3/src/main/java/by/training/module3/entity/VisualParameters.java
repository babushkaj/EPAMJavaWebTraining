package by.training.module3.entity;

import by.training.module3.annotation.Valid;
import by.training.module3.annotation.ValidDoubleSize;
import by.training.module3.annotation.ValidIntSize;

import java.util.Objects;

@Valid
public class VisualParameters {

    private String color;
    @ValidDoubleSize(minSize = 0.8, maxSize = 0.9)
    private double transparency;
    @ValidIntSize(minSize = 10, maxSize = 13)
    private int numberOfFaces;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getTransparency() {
        return transparency;
    }

    public void setTransparency(double transparency) {
        this.transparency = transparency;
    }

    public int getNumberOfFaces() {
        return numberOfFaces;
    }

    public void setNumberOfFaces(int numberOfFaces) {
        this.numberOfFaces = numberOfFaces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisualParameters that = (VisualParameters) o;
        return Double.compare(that.transparency, transparency) == 0 &&
                numberOfFaces == that.numberOfFaces &&
                Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, transparency, numberOfFaces);
    }

    @Override
    public String toString() {
        return "VisualParameters{" +
                "color=" + color +
                ", transparency=" + transparency +
                ", numberOfFaces=" + numberOfFaces +
                '}';
    }
}
