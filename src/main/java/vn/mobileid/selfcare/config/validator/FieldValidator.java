/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.selfcare.config.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldValidator implements ConstraintValidator<Validate, String> {

    private int min;
    private int max;
    private String regexp;
    private Pattern pattern;
    private Matcher matcher;

    @Override
    public void initialize(Validate validate) {
        min = validate.min();
        max = validate.max();
        regexp = validate.regexp();
    }

    @Override
    public boolean isValid(String input, ConstraintValidatorContext context) {
        if (input != null && input.length() < min) {
            return false;
        }
        if (input != null && input.length() > max) {
            return false;
        }
        if (input != null && regexp.length() > 1) {
            pattern = Pattern.compile(regexp);
            matcher = pattern.matcher(input);
            if (!matcher.matches()) {
                return false;
            }
        }
        return true;
    }

}
