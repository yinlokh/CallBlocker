package callblocker.callblocker.core;

import java.util.List;
import java.util.regex.Pattern;

import callblocker.callblocker.models.FilterRule;

public class PhoneNumberChecker {

    private List<FilterRule> filterRules;

    public PhoneNumberChecker(List<FilterRule> filterRules) {
        this.filterRules = filterRules;
    }

    public boolean shouldBlockNumber(String phoneNumber) {
        for (FilterRule rule : filterRules) {
            switch (rule.filterType()) {
                case STARTS_WITH:
                    if (phoneNumber.startsWith(rule.value())) {
                        return true;
                    }
                    break;
                case EXACTLY:
                    if (phoneNumber.equals(rule.value())) {
                        return true;
                    }
                    break;
                case REGEX:
                    try {
                        Pattern pattern = Pattern.compile(rule.value());
                        if (pattern.matcher(phoneNumber).matches()) {
                            return true;
                        }
                    } catch (Exception e) { }
                    break;
            }
        }
        return false;
    }
}
