package callblocker.callblocker.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;

import callblocker.callblocker.database.CallHistoryStore;
import callblocker.callblocker.database.FilterListStore;
import callblocker.callblocker.models.CallHistory;

public class CallBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            CallHistoryStore callHistoryStore = new CallHistoryStore(context);
            FilterListStore filterListStore = new FilterListStore(context);
            CallBlockerPreferences preferences = new CallBlockerPreferences(context);
            PhoneNumberChecker phoneNumberChecker = new PhoneNumberChecker(filterListStore.getFilterEntries());
            ContactsChecker contactsChecker = new ContactsChecker(context);
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            boolean shouldBlock = phoneNumberChecker.shouldBlockNumber(incomingNumber)
                    && (!preferences.getWhitelistAllContacts() || contactsChecker.phoneIsInContacts(incomingNumber));
            if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                callHistoryStore.addCallHistory(
                        CallHistory.create(System.currentTimeMillis(), incomingNumber, shouldBlock));
                if (shouldBlock) {
                    killCall(context);
                }
            }
        } catch (Exception e) {
            // do something???
        }
    }

    public boolean killCall(Context context) {
        try {
            // Get the boring old TelephonyManager
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            // Get the getITelephony() method
            Class classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");

            // Ignore that the method is supposed to be private
            methodGetITelephony.setAccessible(true);

            // Invoke getITelephony() to get the ITelephony interface
            Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);

            // Get the endCall method from ITelephony
            Class telephonyInterfaceClass =
                    Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");

            // Invoke endCall()
            methodEndCall.invoke(telephonyInterface);

        } catch (Exception ex) { // Many things can go wrong with reflection calls
            return false;
        }
        return true;
    }
}
