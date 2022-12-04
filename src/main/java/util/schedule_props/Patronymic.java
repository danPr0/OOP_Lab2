package util.schedule_props;

public enum Patronymic {
    patronymic1("Андрійович"),
    patronymic2("Валерійович"),
    patronymic3("Максимович"),
    patronymic4("Ігорович"),
    patronymic5("Олександрович");

    private String value;

    Patronymic(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
