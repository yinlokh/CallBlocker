package callblocker.callblocker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import callblocker.callblocker.database.CallHistoryContract.CallHistoryEntry;
import callblocker.callblocker.models.CallHistory;

public class CallHistoryStore {

    private static final int HISTORY_LIMIT = 50;

    private final CallHistoryDbHelper callHistoryDbHelper;

    public CallHistoryStore(Context context) {
        callHistoryDbHelper = new CallHistoryDbHelper(context);
    }

    public void addCallHistory(CallHistory callHistory) {
        insertCallHistory(callHistory);
        trimDbToLimit();
    }

    public List<CallHistory> getCallHistory() {
        List<CallHistory> callHistoryList = new ArrayList<>();
        SQLiteDatabase db = callHistoryDbHelper.getReadableDatabase();
        String[] projection = {
                CallHistoryEntry.COLUMN_NAME_TIME,
                CallHistoryEntry.COLUMN_NAME_BLOCKED,
                CallHistoryEntry.COLUMN_NAME_PHONE_NUMBER};
        String sortOrder = CallHistoryEntry.COLUMN_NAME_TIME + " DESC";
        Cursor cursor = db.query(
                CallHistoryEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder);
        int timeIdx = cursor.getColumnIndex(CallHistoryEntry.COLUMN_NAME_TIME);
        int blockedIdx = cursor.getColumnIndex(CallHistoryEntry.COLUMN_NAME_BLOCKED);
        int phoneNumberIdx = cursor.getColumnIndex(CallHistoryEntry.COLUMN_NAME_PHONE_NUMBER);
        while (cursor.moveToNext()) {
            CallHistory history = CallHistory.create(
                    cursor.getLong(timeIdx),
                    cursor.getString(phoneNumberIdx),
                    cursor.getInt(blockedIdx) == 1);
            callHistoryList.add(history);
        }
        cursor.close();
        db.close();
        return callHistoryList;
    }

    private void insertCallHistory(CallHistory callHistory) {
        SQLiteDatabase db = callHistoryDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CallHistoryEntry.COLUMN_NAME_TIME, callHistory.currentTimeMillis());
        values.put(CallHistoryEntry.COLUMN_NAME_PHONE_NUMBER, callHistory.phoneNumber());
        values.put(CallHistoryEntry.COLUMN_NAME_BLOCKED, callHistory.blocked() ? 1 : 0);
        db.insert(CallHistoryEntry.TABLE_NAME, null, values);
        db.close();
    }

    private void trimDbToLimit() {
        List<CallHistory> callHistoryList = getCallHistory();
        if (callHistoryList.size() <= HISTORY_LIMIT) {
            return;
        }

        SQLiteDatabase db = callHistoryDbHelper.getWritableDatabase();
        long cutoffTime = callHistoryList.get(HISTORY_LIMIT).currentTimeMillis();
        String selection = CallHistoryEntry.COLUMN_NAME_TIME + " < ?";
        String selectionArgs[] = {Long.toString(cutoffTime)};
        db.delete(CallHistoryEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
    }
}
