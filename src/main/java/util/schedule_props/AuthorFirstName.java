package util.schedule_props;

public enum AuthorFirstName {
    firstname1("Андрій"),
    firstname2("Валерій"),
    firstname3("Максим"),
    firstname4("Ігор"),
    firstname5("Олександр");

    private String value;

    AuthorFirstName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
