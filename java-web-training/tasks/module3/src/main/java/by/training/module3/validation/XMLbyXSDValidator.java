package by.training.module3.validation;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class XMLbyXSDValidator {

    private String pathXsd;

    public XMLbyXSDValidator(String pathXsd) {
        this.pathXsd = pathXsd;
    }

    public boolean validate(String pathXml) {
        try {
            SchemaFactory factory = SchemaFactory
                    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(this.pathXsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(pathXml));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
