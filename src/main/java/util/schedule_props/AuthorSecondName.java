package util.schedule_props;

public enum AuthorSecondName {
    firstname1("Бондаренко"),
    firstname2("Коваленко"),
    firstname3("Шевченко"),
    firstname4("Мельник"),
    firstname5("Ткаченко");

    private String value;

    AuthorSecondName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
