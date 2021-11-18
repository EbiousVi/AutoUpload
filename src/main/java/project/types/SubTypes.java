package project.types;

public enum SubTypes {
    G_BASE_BASE("м"),
    G_ADV_BASE("мб"),
    G_ADV_ADV("б"),
    T_BASE_BASE("т"),
    T_ADV_ADV("тд");

    public final String name;

    SubTypes(String name) {
        this.name = name;
    }
}
