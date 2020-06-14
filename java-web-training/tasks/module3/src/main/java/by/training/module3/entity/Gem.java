package by.training.module3.entity;

import by.training.module3.annotation.Valid;
import by.training.module3.annotation.ValidDoubleSize;

import java.util.Objects;

@Valid
public class Gem {
    private long id;
    private String name;
    private Preciousness preciousness;
    private String origin;
    private VisualParameters visualParameters;
    @ValidDoubleSize(minSize = 3, maxSize = 460)
    private double value;
    private ValueDimension valueDimension;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Preciousness getPreciousness() {
        return preciousness;
    }

    public void setPreciousness(Preciousness preciousness) {
        this.preciousness = preciousness;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public VisualParameters getVisualParameters() {
        return visualParameters;
    }

    public void setVisualParameters(VisualParameters visualParameters) {
        this.visualParameters = visualParameters;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public ValueDimension getValueDimension() {
        return valueDimension;
    }

    public void setValueDimension(ValueDimension valueDimension) {
        this.valueDimension = valueDimension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gem gem = (Gem) o;
        return id == gem.id &&
                Double.compare(gem.value, value) == 0 &&
                Objects.equals(name, gem.name) &&
                preciousness == gem.preciousness &&
                Objects.equals(origin, gem.origin) &&
                Objects.equals(visualParameters, gem.visualParameters) &&
                valueDimension == gem.valueDimension;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, preciousness, origin, visualParameters, value, valueDimension);
    }

    @Override
    public String toString() {
        return "Gem{" +
                "id=" + id +
                ", name=" + name +
                ", preciousness=" + preciousness +
                ", origin=" + origin +
                ",\n\tvisualParameters=" + visualParameters +
                ",\n\tvalue=" + value + valueDimension.getValue() +
                '}';
    }
}
