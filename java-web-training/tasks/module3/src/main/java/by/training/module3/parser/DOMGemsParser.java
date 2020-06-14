package by.training.module3.parser;

import by.training.module3.entity.Gem;
import by.training.module3.entity.Preciousness;
import by.training.module3.entity.ValueDimension;
import by.training.module3.entity.VisualParameters;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DOMGemsParser implements Parser<Gem> {
    public List<Gem> parse(String xmlPath) throws DOMParserException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new DOMParserException(e);
        }

        Document document;
        try {
            document = builder.parse(xmlPath);
        } catch (SAXException | IOException e) {
            throw new DOMParserException(e);
        }

        List<Gem> gemsList = new ArrayList<>();
        NodeList gemsNodeList = document.getDocumentElement().getChildNodes();

        for (int i = 0; i < gemsNodeList.getLength(); i++) {
            Node gemNode = gemsNodeList.item(i);

            if (gemNode instanceof Element) {
                Gem gem = new Gem();
                long id = Long.parseLong(gemNode.getAttributes().getNamedItem("id").getNodeValue());
                gem.setId(id);
                NodeList childNodes = gemNode.getChildNodes();

                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node fieldNode = childNodes.item(j);

                    if (fieldNode instanceof Element) {
                        String fieldContent = fieldNode.getLastChild().getTextContent().trim();
                        switch (fieldNode.getNodeName()) {
                            case "name":
                                gem.setName(fieldContent);
                                break;
                            case "preciousness":
                                gem.setPreciousness(Preciousness.valueOf(fieldContent.toUpperCase()));
                                break;
                            case "origin":
                                gem.setOrigin(fieldContent);
                                break;
                            case "visualParameters": {
                                VisualParameters vp = new VisualParameters();
                                NodeList vpNodesList = fieldNode.getChildNodes();
                                for (int k = 0; k < vpNodesList.getLength(); k++) {
                                    Node vpNode = vpNodesList.item(k);
                                    if (vpNode instanceof Element) {
                                        String vpFieldContent = vpNode.getTextContent().trim();
                                        switch (vpNode.getNodeName()) {
                                            case "color":
                                                vp.setColor(vpFieldContent);
                                                break;
                                            case "transparency": {
                                                double transparency = Integer.parseInt(vpFieldContent) * 0.01;
                                                vp.setTransparency(transparency);
                                                break;
                                            }
                                            case "numberOfFaces": {
                                                int numberOfFaces = Integer.parseInt(vpFieldContent);
                                                vp.setNumberOfFaces(numberOfFaces);
                                                break;
                                            }
                                        }
                                    }
                                    gem.setVisualParameters(vp);
                                }
                                break;
                            }
                            case "value": {
                                double value = Double.parseDouble(fieldContent);
                                gem.setValue(value);
                                ValueDimension dimension = ValueDimension.valueOf(fieldNode.getAttributes()
                                        .getNamedItem("dimension").getNodeValue().toUpperCase());
                                gem.setValueDimension(dimension);
                                break;
                            }
                        }
                    }
                }
                gemsList.add(gem);
            }
        }

        return gemsList;
    }


}
