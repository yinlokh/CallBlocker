package callblocker.callblocker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import callblocker.callblocker.database.FilterListContract.FilterListEntry;
import callblocker.callblocker.models.FilterType;
import callblocker.callblocker.models.FilterRule;

public class FilterListStore {

    private final FilterListDbHelper filterListDbHelper;

    public FilterListStore(Context context) {
        filterListDbHelper = new FilterListDbHelper(context);
    }

    public void addFilterEntry(FilterRule filterRule) {
        SQLiteDatabase db = filterListDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FilterListEntry.COLUMN_NAME_TIME, filterRule.creationTimeMs());
        values.put(FilterListEntry.COLUMN_NAME_TYPE, filterRule.filterType().toString());
        values.put(FilterListEntry.COLUMN_NAME_VALUE, filterRule.value());
        db.insert(FilterListEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void removeFilterEntry(FilterRule filterRule) {
        SQLiteDatabase db = filterListDbHelper.getWritableDatabase();
        String selection = FilterListEntry.COLUMN_NAME_TIME + " == ?";
        String selectionArgs[] = {Long.toString(filterRule.creationTimeMs())};
        db.delete(FilterListEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    public List<FilterRule> getFilterEntries() {
        List<FilterRule> filterRules = new ArrayList<>();
        SQLiteDatabase db = filterListDbHelper.getReadableDatabase();
        String[] projection = {
                FilterListEntry.COLUMN_NAME_TIME,
                FilterListEntry.COLUMN_NAME_TYPE,
                FilterListEntry.COLUMN_NAME_VALUE};
        String sortOrder = FilterListEntry.COLUMN_NAME_TIME + " DESC";
        Cursor cursor = db.query(
                FilterListEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder);
        int timeIdx = cursor.getColumnIndex(FilterListEntry.COLUMN_NAME_TIME);
        int typeIdx = cursor.getColumnIndex(FilterListEntry.COLUMN_NAME_TYPE);
        int valueIdx = cursor.getColumnIndex(FilterListEntry.COLUMN_NAME_VALUE);
        while (cursor.moveToNext()) {
            FilterRule rule = FilterRule.create(
                    cursor.getLong(timeIdx),
                    FilterType.valueOf(cursor.getString(typeIdx)),
                    cursor.getString(valueIdx));
            filterRules.add(rule);
        }
        cursor.close();
        db.close();
        return filterRules;
    }
}
