package parser;

import org.xml.sax.SAXException;
import parser.strategy.ParseAndSaveStrategy;
import util.ScheduleAttribute;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Map;

public interface Parser {
    void parse(String filename) throws ParserConfigurationException, SAXException, IOException, XMLStreamException;
    void filterAndSort(Map<ScheduleAttribute, String> filters, String sortBy);
    void save(String filename) throws XMLStreamException, IOException, TransformerException, ParserConfigurationException;
    void setStrategy(ParseAndSaveStrategy strategy);
}
