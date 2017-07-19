package callblocker.callblocker.models;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class CallHistory {

    public static CallHistory create(long currentTimeMillis, String phoneNumber, boolean blocked) {
        return new AutoValue_CallHistory(currentTimeMillis, phoneNumber, blocked);
    }

    public abstract long currentTimeMillis();

    public abstract String phoneNumber();

    public abstract boolean blocked();
}
