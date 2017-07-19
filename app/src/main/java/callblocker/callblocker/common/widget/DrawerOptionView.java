package callblocker.callblocker.common.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import callblocker.callblocker.R;

public class DrawerOptionView extends FrameLayout {

    private ImageView icon;
    private TextView textView;

    public DrawerOptionView(@NonNull Context context) {
        this(context, null, 0);
    }

    public DrawerOptionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerOptionView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.drawer_option_view, this);
        icon = (ImageView) findViewById(R.id.icon);
        textView = (TextView) findViewById(R.id.title);
    }

    public void setTitle(String title) {
        textView.setText(title);
    }

    public void setIcon(int resId) {
        icon.setImageResource(resId);
    }
}
