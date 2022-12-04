import gui.MyFrame;
import util.ScheduleAttribute;
import util.schedule_props.*;

import javax.swing.*;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        JFrame.setDefaultLookAndFeelDecorated(true);

        try {
            createInputFile();
        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
        }

        new MyFrame();
    }

    private static void createInputFile() throws XMLStreamException, FileNotFoundException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream("src/main/resources/input.xml"), "UTF-8");
        writer.writeStartDocument("UTF-8", "1.0");

        writer.writeStartElement("schedules");
        for (int i = 0; i < 800; i++) {
            writer.writeStartElement("schedule");

            AuthorFirstName[] firstNames = AuthorFirstName.values();
            AuthorSecondName[] secondNames = AuthorSecondName.values();
            Patronymic[] patronymics = Patronymic.values();

            writer.writeStartElement(ScheduleAttribute.author.name());
            writer.writeCharacters(secondNames[ThreadLocalRandom.current().nextInt(0, secondNames.length)].getValue() + " " +
                    firstNames[ThreadLocalRandom.current().nextInt(0, firstNames.length)].getValue() + " " +
                    patronymics[ThreadLocalRandom.current().nextInt(0, patronymics.length)].getValue());
            writer.writeEndElement();

            writer.writeStartElement(ScheduleAttribute.faculty.name());
            Faculty[] faculties = Faculty.values();
            writer.writeCharacters(faculties[ThreadLocalRandom.current().nextInt(0, faculties.length)].getValue());
            writer.writeEndElement();

            writer.writeStartElement(ScheduleAttribute.department.name());
            Department[] departments = Department.values();
            writer.writeCharacters(departments[ThreadLocalRandom.current().nextInt(0, departments.length)].getValue());
            writer.writeEndElement();

            writer.writeStartElement(ScheduleAttribute.audiences.name());
            List<Audience> audiences = Arrays.stream(Audience.values()).collect(Collectors.toList());
            for (int j = 0; j < 3; j++) {
                writer.writeStartElement("audience");
                int randomIndex = ThreadLocalRandom.current().nextInt(0, audiences.size());

                writer.writeStartElement("number");
                writer.writeCharacters(audiences.get(randomIndex).getNumber());
                writer.writeEndElement();

                writer.writeStartElement("description");
                writer.writeCharacters(audiences.get(randomIndex).getDescription());
                writer.writeEndElement();

                audiences.remove(randomIndex);
                writer.writeEndElement();
            }
            writer.writeEndElement();

            writer.writeStartElement(ScheduleAttribute.students.name());
            writer.writeCharacters(String.valueOf(ThreadLocalRandom.current().nextInt(1, StudentsRange.getMaxNoOfStudents())));
            writer.writeEndElement();

            writer.writeEndElement();
        }
        writer.writeEndElement();

        writer.writeEndDocument();
        writer.flush();
        writer.close();
    }
}
