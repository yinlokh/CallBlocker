package callblocker.callblocker.features.filters.list;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import callblocker.callblocker.R;
import callblocker.callblocker.models.FilterRule;
import io.reactivex.Observable;

public class FilterRuleItemView extends FrameLayout {

    private final TextView typeText;
    private final TextView valueText;
    private final ImageView deleteButton;

    public FilterRuleItemView(
            @NonNull Context context) {
        this(context, null, 0);;
    }

    public FilterRuleItemView(
            @NonNull Context context,
            @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterRuleItemView(
            @NonNull Context context,
            @Nullable AttributeSet attrs,
            @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.filter_rule_item, this);
        typeText = (TextView) findViewById(R.id.type);
        valueText = (TextView) findViewById(R.id.value);
        deleteButton = (ImageView) findViewById(R.id.delete);
    }

    public void setFilterRule(FilterRule filterRule) {
        typeText.setText(filterRule.filterType().displayStringResId);
        valueText.setText(filterRule.value());
    }

    public Observable<Object> deleteClicks() {
        return RxView.clicks(deleteButton);
    }
}
