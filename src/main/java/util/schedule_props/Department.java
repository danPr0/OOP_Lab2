package util.schedule_props;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Department {
    department ("Кафедра обчислювальної математики"),
    department1 ("Кафедра моделювання складних систем"),
    department2 ("Кафедра дослідження операцій"),
    department3 ("Кафедра теоретичної кібернетики"),
    department4 ("Кафедра теорії та технології програмування"),
    department5 ("Кафедра математичної інформатики"),
    department6 ("Кафедра системного аналізу і теорії прийняття рішень"),
    department7 ("Кафедра прикладної статистики"),
    department8 ("Кафедра інформаційних систем");

    private String value;

    Department(String value) {
        this.value = value;
    }

    public static String[] getValues() {
        List<String> values = new ArrayList<>();
        Arrays.stream(Department.values()).toList().forEach(v -> values.add(v.getValue()));

        return values.toArray(String[]::new);
    }

    public String getValue() {
        return value;
    }
}
