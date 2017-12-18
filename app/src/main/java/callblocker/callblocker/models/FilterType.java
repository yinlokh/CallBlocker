package callblocker.callblocker.models;

import callblocker.callblocker.R;

public enum FilterType {
    STARTS_WITH(R.string.filter_type_starts_with),
    EXACTLY(R.string.filter_type_exactly),
    REGEX(R.string.filter_type_regex);

    public final int displayStringResId;

    FilterType(int displayStringResId) {
        this.displayStringResId = displayStringResId;
    }
}
