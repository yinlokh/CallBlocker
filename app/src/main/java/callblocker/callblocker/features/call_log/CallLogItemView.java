package callblocker.callblocker.features.call_log;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import callblocker.callblocker.R;
import callblocker.callblocker.models.CallHistory;

public class CallLogItemView extends FrameLayout {

    private final ImageView icon;
    private final TextView timeText;
    private final TextView phoneNumberText;

    public CallLogItemView(
            @NonNull Context context) {
        this(context, null, 0);;
    }

    public CallLogItemView(
            @NonNull Context context,
            @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CallLogItemView(
            @NonNull Context context,
            @Nullable AttributeSet attrs,
            @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.call_log_item, this);
        icon = (ImageView) findViewById(R.id.icon);
        timeText = (TextView) findViewById(R.id.time);
        phoneNumberText = (TextView) findViewById(R.id.phone_number);
    }

    public void setCallHistory(CallHistory callHistory) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(callHistory.currentTimeMillis());
        timeText.setText(sdf.format(resultdate));
        phoneNumberText.setText(callHistory.phoneNumber());

        icon.setImageResource(callHistory.blocked()
                ? R.drawable.call_missed
                : R.drawable.call_received);
    }
}
