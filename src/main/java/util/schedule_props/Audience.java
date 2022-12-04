package util.schedule_props;

public enum Audience {
    audience1 ("101", "Вмістимість : 97"),
    audience2 ("102", "Вмістимість : 23"),
    audience3 ("103", "Вмістимість : 151"),
    audience4 ("104", "Вмістимість : 32"),
    audience5 ("105", "Вмістимість : 128"),
    audience6 ("106", "Вмістимість : 64"),
    audience7 ("107", "Вмістимість : 55"),
    audience8 ("108", "Вмістимість : 66");

    private String number;
    private String description;

    Audience(String number, String description) {
        this.number = number;
        this.description = description;
    }

    public String getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }
}
