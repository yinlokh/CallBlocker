package callblocker.callblocker.models;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class FilterRule {

    public static FilterRule create(long creationTimeMs, FilterType filterType, String value) {
        return new AutoValue_FilterRule(creationTimeMs, filterType, value);
    }

    public abstract long creationTimeMs();

    public abstract FilterType filterType();

    public abstract String value();
}
