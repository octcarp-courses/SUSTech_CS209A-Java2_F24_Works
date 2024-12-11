package annotation;

import java.lang.reflect.Field;

public enum Rule {
    ALL_LOWERCASE("should be all lowercase") {
        @Override
        public boolean apply(String fieldName, String valStr, Object obj) {
            boolean isValid = valStr.equals(valStr.toLowerCase());

            if (!isValid) {
                printErr(fieldName);
            }
            return isValid;
        }
    },

    NO_USERNAME("should not contain username") {
        @Override
        public boolean apply(String fieldName, String valStr, Object obj) {
            boolean isValid;

            try {
                Field usernameField = obj.getClass().getDeclaredField("username");
                usernameField.setAccessible(true);

                String username = usernameField.get(obj).toString();
                isValid = !valStr.contains(username);
            } catch (Exception e) {
                e.printStackTrace();
                isValid = false;
            }

            if (!isValid) {
                printErr(fieldName);
            }
            return isValid;
        }
    },

    HAS_BOTH_DIGITS_AND_LETTERS("should have both letters and digits") {
        @Override
        public boolean apply(String fieldName, String valStr, Object obj) {
            boolean isValid = valStr.matches("^(?=.*[A-Za-z])(?=.*\\d).+$");
            if (!isValid) {
                printErr(fieldName);
            }
            return isValid;
        }
    };

    private final String ruleDescription;

    Rule(String ruleDescription) {
        this.ruleDescription = ruleDescription;
    }

    public abstract boolean apply(String fieldName, String valStr, Object obj);

    protected void printErr(String fieldName) {
        System.out.println("Validation failed for field *" + fieldName +
                "*: " + this.ruleDescription);
    }
}
