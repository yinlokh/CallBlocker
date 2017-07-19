package callblocker.callblocker.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import callblocker.callblocker.database.FilterListContract.FilterListEntry;

public class FilterListDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FilterListEntry.TABLE_NAME + " (" +
                    FilterListEntry.COLUMN_NAME_TIME + " INTEGER PRIMARY KEY," +
                    FilterListEntry.COLUMN_NAME_TYPE + " TEXT," +
                    FilterListEntry.COLUMN_NAME_VALUE + " TEXT)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FilterListEntry.TABLE_NAME;
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "FilterList.db";

    public FilterListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public FilterListDbHelper(
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
