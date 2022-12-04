package gui;

import parser.Parser;
import parser.ParserImpl;
import util.HTMLConverter;
import util.ParserName;
import util.ScheduleAttribute;
import util.schedule_props.Department;
import util.schedule_props.Faculty;
import util.schedule_props.StudentsRange;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static util.FileConstants.*;

public class FilterPanel extends JPanel {
    MyFrame myFrame;
    SpringLayout layout;
    String currentFileName;
    Parser parser = new ParserImpl(ParserName.DOMParser.getStrategy());

    public FilterPanel(MyFrame myFrame) {
        super();
        this.myFrame = myFrame;

        layout = new SpringLayout();
        setLayout(layout);
        setPreferredSize(new Dimension(400, 600));
        setBackground(Color.gray);

        JTextArea authorFilter = addAuthorFilter();
        JComboBox<String> facultyFilter = addComboBoxFilter("Faculty : ", authorFilter, Faculty.getValues());
        JComboBox<String> departmentFilter = addComboBoxFilter("Department : ", facultyFilter, Department.getValues());
        JTextArea audienceFilter = addAudienceFilter( departmentFilter);
        JComboBox<String> studentsFilter = addComboBoxFilter("Students : ", audienceFilter, StudentsRange.getValues());

        JButton submitButton = addApplyButton(studentsFilter);
        JComboBox<String> sortParam = addComboBoxFilter("Sort by : ", submitButton, ScheduleAttribute.author.name(),
                ScheduleAttribute.faculty.name(), ScheduleAttribute.department.name(), ScheduleAttribute.students.name());
        JButton chooseFileButton = addChooseFileButton();
        JComboBox<String> parserSelect = addParserSelect(chooseFileButton, ParserName.DOMParser.toString(), ParserName.SAXParser.toString(), ParserName.STAXParser.toString());

        parserSelect.addActionListener((e) -> {
            ParserName parserName = ParserName.getByValue((String) parserSelect.getSelectedItem());
            parser.setStrategy(parserName.getStrategy());
        });

        submitButton.addActionListener((event) -> {
            if (currentFileName == null)
                chooseFileButton.doClick();

            Map<ScheduleAttribute, String> filters = new HashMap<>();
            filters.put(ScheduleAttribute.author, authorFilter.getText());
            filters.put(ScheduleAttribute.faculty, (String) facultyFilter.getSelectedItem());
            filters.put(ScheduleAttribute.department, (String) departmentFilter.getSelectedItem());
            filters.put(ScheduleAttribute.audiences, audienceFilter.getText());
            filters.put(ScheduleAttribute.students, (String) studentsFilter.getSelectedItem());

            try {
                parser.parse(INPUT_XML_FILENAME);
                parser.filterAndSort(filters, (String) sortParam.getSelectedItem());
                parser.save(OUTPUT_XML_FILENAME);

                HTMLConverter.convertXmlToHtml(OUTPUT_XML_FILENAME, XSL_CONVERTER_FILENAME, currentFileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private JTextArea addAuthorFilter() {
        JTextArea nameFilter = new JTextArea();
        nameFilter.setPreferredSize(new Dimension(300, 20));

        JLabel nameFilterLabel = new JLabel("Author : ");
        nameFilterLabel.setPreferredSize(new Dimension(80, 20));
        nameFilterLabel.setLabelFor(nameFilter);

        layout.putConstraint(SpringLayout.WEST, nameFilterLabel, 10, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, nameFilterLabel, 10, SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, nameFilter, 5, SpringLayout.EAST, nameFilterLabel);
        layout.putConstraint(SpringLayout.NORTH, nameFilter, 10, SpringLayout.NORTH, this);

        add(nameFilterLabel);
        add(nameFilter);

        return nameFilter;
    }

    private JTextArea addAudienceFilter(Component upperBound)  {
        JTextArea filter = new JTextArea();
        JLabel label = new JLabel("Audiences :");
        setFilterLayout(filter, label, upperBound);

        return filter;
    }

    private JComboBox<String> addComboBoxFilter(String labelText, Component upperBound, String ...args) {
        List<String> values = Arrays.stream(args).collect(Collectors.toList());
        values.add(0, "");

        JComboBox<String> filter = new JComboBox<>(values.toArray(String[]::new));
        JLabel label = new JLabel(labelText);
        setFilterLayout(filter, label, upperBound);

        return filter;
    }

    private void setFilterLayout(Component filter, JLabel label, Component upperBound) {
        filter.setPreferredSize(new Dimension(300, 20));

        label.setPreferredSize(new Dimension(80, 20));
        label.setLabelFor(filter);

        layout.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, label, 10, SpringLayout.SOUTH, upperBound);

        layout.putConstraint(SpringLayout.WEST, filter, 5, SpringLayout.EAST, label);
        layout.putConstraint(SpringLayout.NORTH, filter, 10, SpringLayout.SOUTH, upperBound);

        add(label);
        add(filter);
    }

    private JButton addApplyButton(Component lastComponent) {
        JButton submitButton = new JButton("Apply");

        layout.putConstraint(SpringLayout.WEST, submitButton, 10, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, submitButton, 10, SpringLayout.SOUTH, lastComponent);

        add(submitButton);
        return submitButton;
    }

    public JButton addChooseFileButton() {
        JButton chooseFileButton = new JButton("Choose output file");

        JLabel buttonLabel = new JLabel();
//        buttonLabel.setText(currentFileName == null ? "Specify current file's location" : "Current file's location is : " + currentFileName);
        buttonLabel.setLabelFor(chooseFileButton);

        chooseFileButton.addActionListener((event) -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify the output file");
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.html", "html"));

            int userSelection = fileChooser.showSaveDialog(myFrame);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                currentFileName = selectedFile.getAbsolutePath();

                if (!currentFileName.endsWith(".html"))
                    currentFileName += ".html";

                buttonLabel.setText("Current file is : " + currentFileName.substring(currentFileName.lastIndexOf(File.separator) + 1));
            }
        });

        layout.putConstraint(SpringLayout.WEST, buttonLabel, 10, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, buttonLabel, 300, SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, chooseFileButton, 10, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, chooseFileButton, 10, SpringLayout.SOUTH, buttonLabel);

        add(chooseFileButton);
        add(buttonLabel);

        return chooseFileButton;
    }

    private JComboBox<String> addParserSelect(Component upperBound, String ...args) {
        JComboBox<String> filter = new JComboBox<>(args);
        JLabel label = new JLabel("Choose parser:");
        setFilterLayout(filter, label, upperBound);

        return filter;
    }
}
