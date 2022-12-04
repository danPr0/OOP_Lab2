package util.schedule_props;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Faculty {
    faculty1 ("Факультет комп'ютерних наук та кібернетики"),
    faculty2 ("Факультет інформаційних технологій");

    private String value;

    Faculty(String value) {
        this.value = value;
    }

    public static String[] getValues() {
        List<String> values = new ArrayList<>();
        Arrays.stream(Faculty.values()).toList().forEach(v -> values.add(v.getValue()));

        return values.toArray(String[]::new);
    }

    public String getValue() {
        return value;
    }
}
