package project.props.validator;

import static project.colors.Colors.paintRed;

public class LongValidator implements Validator {

    @Override
    public boolean validate(String key, String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            System.out.println(paintRed("Введите корректное значение для ключа " + key));
            System.out.println(paintRed("Требуемый формат число без символов и пробелов"));
            System.out.println(paintRed(e.toString()));
            return false;
        }
    }
}
