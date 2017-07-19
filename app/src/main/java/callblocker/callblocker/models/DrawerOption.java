package callblocker.callblocker.models;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DrawerOption {

    public static DrawerOption create(String title) {
        return new AutoValue_DrawerOption(title);
    }

    public abstract String title();
}
