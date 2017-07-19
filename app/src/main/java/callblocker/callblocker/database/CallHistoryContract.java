package callblocker.callblocker.database;

import android.provider.BaseColumns;

public class CallHistoryContract {

    private CallHistoryContract() {}

    public static class CallHistoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "call_history";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_PHONE_NUMBER = "phone_number";
        public static final String COLUMN_NAME_BLOCKED = "blocked";
    }
}
