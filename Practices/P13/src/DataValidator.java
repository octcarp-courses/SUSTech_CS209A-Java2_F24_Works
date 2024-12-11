import annotation.CustomValidation;
import annotation.CustomValidations;
import annotation.MinLength;
import annotation.Rule;

import java.lang.reflect.Field;
import java.util.Scanner;

public class DataValidator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Username: ");
            String username = sc.next();
            System.out.print("Password: ");
            String pwd = sc.next();
            User user = new User(username, pwd);
            if (validate(user)) {
                System.out.println("Success!");
                break;
            }
        }
    }

    public static boolean validate(Object obj) {
        boolean isValid = true;
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            try {
                Object value = field.get(obj);
                String valStr = value.toString();

                if (field.isAnnotationPresent(MinLength.class)) {
                    MinLength minLength = field.getAnnotation(MinLength.class);
                    if (valStr.length() < minLength.min()) {
                        System.out.println("Validation failed for field *" + field.getName() + "*: should have a minimum length of " + minLength.min());
                        isValid = false;
                    }
                }

                CustomValidation[] validations;
                if (field.isAnnotationPresent(CustomValidations.class)) {
                    validations = field.getAnnotation(CustomValidations.class).value();
                } else if (field.isAnnotationPresent(CustomValidation.class)) {
                    validations = new CustomValidation[]{field.getAnnotation(CustomValidation.class)};
                } else {
                    validations = null;
                }
                if (validations!=null) {
                    for (CustomValidation validation : validations) {
                        Rule rule = validation.rule();
                        if (!rule.apply(field.getName(), valStr, obj)) {
                            isValid = false;
                        }
                    }
                }
            } catch (Exception _) {

            }
        }

        return isValid;
    }
} 