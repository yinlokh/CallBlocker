package callblocker.callblocker.core;

import android.content.Context;
import android.content.SharedPreferences;

public class CallBlockerPreferences {

    private static final String FILE_NAME = "CallBlockerPreferences";
    private static final String WHITELIST_CONTACTS_PREFKEY = "whitelist_contacts";

    private final SharedPreferences sharedPreferences;

    public CallBlockerPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public void setWhitelistAllContacts(boolean whitelistAllContacts) {
        sharedPreferences.edit()
                .putBoolean(WHITELIST_CONTACTS_PREFKEY, whitelistAllContacts)
                .commit();
    }

    public boolean getWhitelistAllContacts() {
        return sharedPreferences.getBoolean(WHITELIST_CONTACTS_PREFKEY, false);
    }
}
