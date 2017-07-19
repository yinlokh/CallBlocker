package callblocker.callblocker.models;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DrawerOption {

    public static DrawerOption create(String title, int iconResId) {
        return new AutoValue_DrawerOption(title, iconResId);
    }

    public abstract String title();

    public abstract int iconResId();
}
