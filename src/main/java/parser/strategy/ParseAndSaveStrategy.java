package parser.strategy;

import org.xml.sax.SAXException;
import parser.ParserImpl;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;

public interface ParseAndSaveStrategy {
    List<ParserImpl.Schedule> parse(String filename) throws ParserConfigurationException, SAXException, IOException, XMLStreamException;
    void save(List<ParserImpl.Schedule> filteredContainer, String filename) throws XMLStreamException, IOException, TransformerException, ParserConfigurationException;
}
