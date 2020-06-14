package by.trainig.module2.validator;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class ValidationResult {
    private Map<String, String> result;

    public ValidationResult() {
        result = new HashMap<>();
    }

    public boolean isValid() {
        if (result.isEmpty()) {
            return true;
        }
        return false;
    }

    public void addErrorMessage(String key, String message) {
        result.put(key, message);
    }

    public String getErrorsAsString() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : result.entrySet()) {
            sb.append(MessageFormat.format("\t{0}\n", entry.getValue()));
        }

        return sb.toString();
    }
}
