package project.props.validator;

public class NoneValidator implements Validator {

    @Override
    public boolean validate(String key, String value) {
        return true;
    }
}
