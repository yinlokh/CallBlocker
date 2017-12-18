package callblocker.callblocker.core;

import android.content.Context;
import android.content.SharedPreferences;

public class CallBlockerPreferences {

    private static final String FILE_NAME = "CallBlockerPreferences";
    private static final String WHITELIST_CONTACTS_PREFKEY = "whitelist_contacts";
    private static final String PAUSE_BLOCKING_PREFKEY = "pause_blocking";

    private final SharedPreferences sharedPreferences;

    public CallBlockerPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public void setWhitelistAllContacts(boolean whitelistAllContacts) {
        sharedPreferences.edit()
                .putBoolean(WHITELIST_CONTACTS_PREFKEY, whitelistAllContacts)
                .commit();
    }

    public void setPauseBlocking(boolean pauseBlocking) {
        sharedPreferences.edit()
                .putBoolean(PAUSE_BLOCKING_PREFKEY, pauseBlocking)
                .commit();
    }

    public boolean getWhitelistAllContacts() {
        return sharedPreferences.getBoolean(WHITELIST_CONTACTS_PREFKEY, false);
    }

    public boolean getPauseBlocking() {
        return sharedPreferences.getBoolean(PAUSE_BLOCKING_PREFKEY, false);
    }
}
