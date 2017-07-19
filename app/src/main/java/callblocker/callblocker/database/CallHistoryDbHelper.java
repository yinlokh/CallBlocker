package callblocker.callblocker.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import callblocker.callblocker.database.CallHistoryContract.CallHistoryEntry;

public class CallHistoryDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CallHistoryEntry.TABLE_NAME + " (" +
                    CallHistoryEntry.COLUMN_NAME_TIME + " INTEGER PRIMARY KEY," +
                    CallHistoryEntry.COLUMN_NAME_PHONE_NUMBER + " TEXT," +
                    CallHistoryEntry.COLUMN_NAME_BLOCKED + " INTEGER)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CallHistoryEntry.TABLE_NAME;
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "CallHistory.db";

    public CallHistoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public CallHistoryDbHelper(
            Context context,
            String name,
            SQLiteDatabase.CursorFactory factory,
            int version,
            DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
    }
}
