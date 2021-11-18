package project.types;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static project.colors.Colors.paintRed;

public enum Types {
    G("г"),
    T("т");

    public String name;
    private static List<String> names;

    private static final List<SubTypes> subTypesT = Arrays.asList(SubTypes.T_BASE_BASE, SubTypes.T_ADV_ADV);
    private static final List<SubTypes> subTypesG = Arrays.asList(SubTypes.G_BASE_BASE, SubTypes.G_ADV_BASE, SubTypes.G_ADV_ADV);

    public static List<SubTypes> getSubTypesByType(Types type) {
        if (type.equals(G)) {
            return subTypesG;
        } else if (type.equals(T)) {
            return subTypesT;
        }
        throw new IllegalArgumentException(paintRed("Данного типа нет в системе!"));
    }

    public static List<String> names() {
        if (names == null) {
            Types[] values = values();
            names = Arrays.stream(values).map(type -> type.name).collect(Collectors.toList());
        }
        return names;
    }

    Types(String name) {
        this.name = name;
    }
}
