package by.training.module1.validator;

import java.util.HashMap;
import java.util.Map;

public class ValidationResult {
    private Map<String, String> result;

    public ValidationResult() {
        result = new HashMap<>();
    }

    public boolean isValid(){
        if(result.isEmpty()){
            return true;
        }
        return false;
    }

    public void addErrorMessage(String key, String message){
        result.put(key, message);
    }

    public Map<String, String> getResult() {
        return result;
    }
}
