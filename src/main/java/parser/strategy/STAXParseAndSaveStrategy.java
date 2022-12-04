package parser.strategy;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.xml.sax.SAXException;
import parser.ParserImpl;
import util.ScheduleAttribute;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class STAXParseAndSaveStrategy extends SAXParseAndSaveStrategy{
    @Override
    public List<ParserImpl.Schedule> parse(String filename) throws ParserConfigurationException, SAXException, IOException, XMLStreamException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(filename));

        List<ParserImpl.Schedule> container = new ArrayList<>();
        ScheduleAttribute lastAttr = null;

        while (reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();

            if (nextEvent.isStartElement()) {
                String tagName = nextEvent.asStartElement().getName().getLocalPart();

                if (tagName.equals("schedule")) {
                    ParserImpl.Schedule schedule = new ParserImpl.Schedule();
                    schedule.audiences = new ArrayList<>();
                    container.add(schedule);
                }
                else {
                    try {
                        lastAttr = ScheduleAttribute.valueOf(tagName);
                    }
                    catch (IllegalArgumentException ignored) {
                    }
                }
            }

            if (nextEvent.isCharacters()) {
                String text = nextEvent.asCharacters().getData();

                if (!text.isBlank()) {
                    ParserImpl.Schedule schedule = container.get(container.size() - 1);

                    switch (Objects.requireNonNull(lastAttr)) {
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
            }
        }

        reader.close();
        return container;
    }
}
