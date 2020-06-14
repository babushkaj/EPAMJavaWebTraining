package by.training.module3.parser;

import by.training.module3.entity.Gem;
import by.training.module3.entity.Preciousness;
import by.training.module3.entity.ValueDimension;
import by.training.module3.entity.VisualParameters;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class StAXGemsParser implements Parser<Gem> {

    private List<Gem> gems;
    private Gem gem;
    private VisualParameters vp;
    private String content;

    public StAXGemsParser() {
        this.gems = new ArrayList<>();
    }

    public List<Gem> parse(String xmlPath) throws StAXParserException {

        XMLInputFactory factory = XMLInputFactory.newInstance();
        try {
            XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(xmlPath));
            while (reader.hasNext()) {
                int event = reader.next();
                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        switch (reader.getLocalName()) {
                            case "gem":
                                gem = new Gem();
                                long id = Long.parseLong(reader.getAttributeValue(0));
                                gem.setId(id);
                                break;
                            case "visualParameters":
                                vp = new VisualParameters();
                                break;
                            case "value":
                                ValueDimension dimension = ValueDimension.valueOf(reader.getAttributeValue(0).toUpperCase());
                                gem.setValueDimension(dimension);
                                break;
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        content = reader.getText().trim();
                        break;
                    case XMLStreamConstants.END_ELEMENT: {
                        switch (reader.getLocalName()) {
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
                    break;
                }
            }
        } catch (XMLStreamException | FileNotFoundException e) {
            throw new StAXParserException(e);
        }

        return gems;
    }
}
