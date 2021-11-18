package project.props.enums;

public enum KeysGroup {
    PATH(".path"),
    LONG(".long"),
    NONE("");

    public final String value;

    KeysGroup(String value) {
        this.value = value;
    }
}
