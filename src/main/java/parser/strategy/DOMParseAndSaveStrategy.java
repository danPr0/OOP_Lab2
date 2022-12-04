package parser.strategy;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import parser.ParserImpl;
import util.ScheduleAttribute;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DOMParseAndSaveStrategy implements ParseAndSaveStrategy {
    @Override
    public List<ParserImpl.Schedule> parse(String filename) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = factory.newDocumentBuilder().parse(new File(filename));

        List<ParserImpl.Schedule> container = new ArrayList<>();
        NodeList schedules = doc.getLastChild().getChildNodes();

        for (int i = 0; i < schedules.getLength(); i++) {
            if (schedules.item(i).getNodeType() == Node.ELEMENT_NODE) {
                ParserImpl.Schedule schedule = new ParserImpl.Schedule();
                NodeList scheduleInfo = schedules.item(i).getChildNodes();

                for (int j = 0; j < scheduleInfo.getLength(); j++) {
                    if (scheduleInfo.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        ScheduleAttribute attr = ScheduleAttribute.valueOf(scheduleInfo.item(j).getNodeName());
                        switch (attr) {
                            case author -> schedule.author = scheduleInfo.item(j).getTextContent();
                            case faculty -> schedule.faculty = scheduleInfo.item(j).getTextContent();
                            case department -> schedule.department = scheduleInfo.item(j).getTextContent();
                            case audiences -> schedule.audiences = parseAudiences(scheduleInfo.item(j).getChildNodes());
                            case students -> schedule.students = Integer.parseInt(scheduleInfo.item(j).getTextContent());
                        }
                    }
                }
                container.add(schedule);
            }
        }

        return container;
    }

    public List<Pair<String, String>> parseAudiences(NodeList nodes) {
        List<Pair<String, String>> audiences = new ArrayList<>();

        for (int i= 0; i < nodes.getLength(); i++) {
            Node audience = nodes.item(i);

            if (audience.getNodeType() == Node.ELEMENT_NODE) {
                audiences.add(new ImmutablePair<>(audience.getChildNodes().item(0).getTextContent(),
                        audience.getChildNodes().item(1).getTextContent()));
            }
        }

        return audiences;
    }

    @Override
    public void save(List<ParserImpl.Schedule> filteredContainer, String filename) throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Node schedulesNode = doc.appendChild(doc.createElement("schedules"));
        for (ParserImpl.Schedule schedule : filteredContainer) {
            Node scheduleNode = doc.createElement("schedule");

            scheduleNode.appendChild(createSimpleNode(ScheduleAttribute.author.name(), schedule.author, doc));
            scheduleNode.appendChild(createSimpleNode(ScheduleAttribute.faculty.name(), schedule.faculty, doc));
            scheduleNode.appendChild(createSimpleNode(ScheduleAttribute.department.name(), schedule.department, doc));
            scheduleNode.appendChild(createAudiencesNode(schedule.audiences, doc));
            scheduleNode.appendChild(createSimpleNode(ScheduleAttribute.students.name(), String.valueOf(schedule.students), doc));

            schedulesNode.appendChild(scheduleNode);
        }

        DOMSource dom = new DOMSource(doc);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();

        StreamResult result = new StreamResult(new File(filename));
        transformer.transform(dom, result);
    }

    private Node createSimpleNode(String tag, String value, Document doc) {
        Node author = doc.createElement(tag);
        author.setTextContent(value);
        return author;
    }

    private Node createAudiencesNode(List<Pair<String, String>> audiences, Document doc) {
        Node audiencesNode = doc.createElement(ScheduleAttribute.audiences.name());

        for (Pair<String, String> audience : audiences) {
            Node audienceNode = doc.createElement("audience");

            Node audienceNumber = doc.createElement("number");
            audienceNumber.setTextContent(audience.getLeft());
            audienceNode.appendChild(audienceNumber);

            Node audienceDescription = doc.createElement("description");
            audienceDescription.setTextContent(audience.getRight());
            audienceNode.appendChild(audienceDescription);

            audiencesNode.appendChild(audienceNode);
        }
        return audiencesNode;
    }
}
