package by.training.module3.parser;

import by.training.module3.entity.Gem;
import by.training.module3.entity.Preciousness;
import by.training.module3.entity.ValueDimension;
import by.training.module3.entity.VisualParameters;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class SAXHandler extends DefaultHandler {

    private List<Gem> gems;
    private Gem gem;
    private VisualParameters vp;
    private String content;

    public SAXHandler() {
        gems = new ArrayList<>();
    }

    public List<Gem> getGems() {
        return this.gems;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        switch (qName) {
            case "gem":
                gem = new Gem();
                long id = Long.parseLong(attributes.getValue("id"));
                gem.setId(id);
                break;
            case "visualParameters":
                vp = new VisualParameters();
                break;
            case "value":
                ValueDimension dimension = ValueDimension.valueOf(attributes.getValue("dimension").toUpperCase());
                gem.setValueDimension(dimension);
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        switch (qName) {
            case "gem":
                gems.add(gem);
                break;
            case "visualParameters":
                gem.setVisualParameters(vp);
                break;
            case "name":
                gem.setName(content);
                break;
            case "preciousness":
                gem.setPreciousness(Preciousness.valueOf(content.toUpperCase()));
                break;
            case "origin":
                gem.setOrigin(content);
                break;
            case "color":
                vp.setColor(content);
                break;
            case "transparency":
                double transparency = Integer.parseInt(content) * 0.01;
                vp.setTransparency(transparency);
                break;
            case "numberOfFaces":
                int numberOfFaces = Integer.parseInt(content);
                vp.setNumberOfFaces(numberOfFaces);
                break;
            case "value":
                double value = Double.parseDouble(content);
                gem.setValue(value);
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        content = String.copyValueOf(ch, start, length).trim();
    }
}
