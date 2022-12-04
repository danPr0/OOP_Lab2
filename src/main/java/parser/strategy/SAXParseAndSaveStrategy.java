package parser.strategy;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import parser.ParserImpl;
import util.ScheduleAttribute;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SAXParseAndSaveStrategy implements ParseAndSaveStrategy{
    private static class Handler extends DefaultHandler {
        private List<ParserImpl.Schedule> container;
        private ScheduleAttribute lastAttr;

        @Override
        public void startDocument() {
            container = new ArrayList<>();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if (qName.equals("schedule")) {
                ParserImpl.Schedule schedule = new ParserImpl.Schedule();
                schedule.audiences = new ArrayList<>();
                container.add(schedule);
            }
            else {
                try {
                    lastAttr = ScheduleAttribute.valueOf(qName);
                }
                catch (IllegalArgumentException ignored) {
                }
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            String text = String.valueOf(ch).substring(start, start + length);
            if (text.isBlank())
                return;

            ParserImpl.Schedule schedule = container.get(container.size() - 1);

            switch (lastAttr) {
                case author -> schedule.author = text;
                case faculty -> schedule.faculty = text;
                case department -> schedule.department = text;
                case audiences ->  {
                    Pair<String, String> audience;
                    if (schedule.audiences.isEmpty() || !schedule.audiences.get(schedule.audiences.size() - 1).getRight().isEmpty()) {
                        audience = new MutablePair<>(text, "");
                        schedule.audiences.add(audience);
                    }
                    else {
                        audience = schedule.audiences.get(schedule.audiences.size() - 1);
                        audience.setValue(text);
                    }
                }
                case students -> schedule.students = Integer.parseInt(text);
            }
        }

        public List<ParserImpl.Schedule> getContainer() {
            return container;
        }
    }
    
    @Override
    public List<ParserImpl.Schedule> parse(String filename) throws ParserConfigurationException, SAXException, IOException, XMLStreamException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        Handler handler = new Handler();

        parser.parse(new File(filename), handler);
        return handler.getContainer();
    }

    @Override
    public void save(List<ParserImpl.Schedule> filteredContainer, String filename) throws XMLStreamException, IOException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        FileOutputStream outputStream = new FileOutputStream(filename);
        XMLStreamWriter writer = factory.createXMLStreamWriter(outputStream, "UTF-8");

        writer.writeStartDocument("UTF-8", "1.0");

        writer.writeStartElement("schedules");
        for (ParserImpl.Schedule schedule : filteredContainer) {
            writer.writeStartElement("schedule");

            writerSimpleElement(ScheduleAttribute.author.name(), schedule.author, writer);
            writerSimpleElement(ScheduleAttribute.faculty.name(), schedule.faculty, writer);
            writerSimpleElement(ScheduleAttribute.department.name(), schedule.department, writer);

            writer.writeStartElement(ScheduleAttribute.audiences.name());
            for (Pair<String, String> audience : schedule.audiences) {
                writer.writeStartElement("audience");
                writerSimpleElement("number", audience.getLeft(), writer);
                writerSimpleElement("description", audience.getRight(), writer);
                writer.writeEndElement();
            }
            writer.writeEndElement();

            writerSimpleElement(ScheduleAttribute.students.name(), String.valueOf(schedule.students), writer);
            writer.writeEndElement();
        }
        writer.writeEndElement();

        writer.writeEndDocument();
        writer.flush();
        writer.close();

        outputStream.close();
    }

    private void writerSimpleElement(String tag, String text, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(tag);
        writer.writeCharacters(text);
        writer.writeEndElement();
    }
}
