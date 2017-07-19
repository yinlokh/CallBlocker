package callblocker.callblocker.database;

import android.provider.BaseColumns;

public class FilterListContract {

    private FilterListContract() {}

    public static class FilterListEntry implements BaseColumns {
        public static final String TABLE_NAME = "filters";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_VALUE = "value";
    }
}
