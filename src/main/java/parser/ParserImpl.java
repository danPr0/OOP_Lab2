package parser;

import org.apache.commons.lang3.tuple.Pair;
import org.xml.sax.SAXException;
import parser.strategy.ParseAndSaveStrategy;
import util.ScheduleAttribute;
import util.schedule_props.Faculty;
import util.schedule_props.StudentsRange;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParserImpl implements Parser {
    private List<Schedule> container;
    private List<Schedule> filteredContainer;

    private ParseAndSaveStrategy strategy;

    public static class Schedule {
        public String author;
        public String faculty;
        public String department;
        public List<Pair<String, String>> audiences;
        public String curriculum;
        public int students;
    }

    public ParserImpl(ParseAndSaveStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void parse(String filename) throws ParserConfigurationException, SAXException, IOException, XMLStreamException {
        container = strategy.parse(filename);
    }

    public void filterAndSort(Map<ScheduleAttribute, String> filters, String sortBy) {
        filteredContainer = container.stream().filter(schedule ->
                ScheduleAttribute.author.filter(schedule.author, filters.get(ScheduleAttribute.author)) &&
                ScheduleAttribute.faculty.filter(schedule.faculty, filters.get(ScheduleAttribute.faculty)) &&
                ScheduleAttribute.department.filter(schedule.department, filters.get(ScheduleAttribute.department)) &&
                ScheduleAttribute.audiences.filter(schedule.audiences, filters.get(ScheduleAttribute.audiences)) &&
                ScheduleAttribute.students.filter(schedule.students, filters.get(ScheduleAttribute.students))).collect(Collectors.toList());

        if (!sortBy.isEmpty()) {
            ScheduleAttribute sortAttr = ScheduleAttribute.valueOf(sortBy);
            switch (sortAttr) {
                case author -> filteredContainer = filteredContainer.stream().sorted(Comparator.comparing(sch -> sch.author)).collect(Collectors.toList());
                case faculty -> filteredContainer = filteredContainer.stream().sorted(Comparator.comparing(sch -> sch.faculty)).collect(Collectors.toList());
                case department -> filteredContainer = filteredContainer.stream().sorted(Comparator.comparing(sch -> sch.department)).collect(Collectors.toList());
                case students -> filteredContainer = filteredContainer.stream().sorted(Comparator.comparing(sch -> sch.students)).collect(Collectors.toList());
            }
        }
    }

    @Override
    public void save(String filename) throws XMLStreamException, IOException, TransformerException, ParserConfigurationException {
        strategy.save(filteredContainer, filename);
    }

    public void setStrategy(ParseAndSaveStrategy strategy) {
        this.strategy = strategy;
    }
}
