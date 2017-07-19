package callblocker.callblocker.features.filters.add;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.wealthfront.magellan.Screen;
import com.wealthfront.magellan.ScreenView;

import callblocker.callblocker.R;
import callblocker.callblocker.models.FilterType;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

public class AddFilterView extends FrameLayout implements ScreenView {

    private Screen screen;
    private Spinner filterTypeSpinner;
    private EditText valueEditText;
    private EditText phoneNumberEditText;
    private ImageView testPassIndicator;
    private PublishSubject<Object> spinnerChanges = PublishSubject.create();

    public AddFilterView(@NonNull Context context) {
        this(context, null, 0);
    }

    public AddFilterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddFilterView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.add_filter_screen, this);
        filterTypeSpinner = (Spinner) findViewById(R.id.drop_down);
        ArrayAdapter adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                FilterType.values());
        filterTypeSpinner.setAdapter(adapter);
        filterTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (FilterType.values()[i] == FilterType.REGEX) {
                    valueEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    valueEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }

                spinnerChanges.onNext(new Object());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        valueEditText = (EditText) findViewById(R.id.value);
        phoneNumberEditText = (EditText) findViewById(R.id.phone_number);
        testPassIndicator = (ImageView) findViewById(R.id.test_indicator);
    }

    @Override
    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    @Override
    public Screen getScreen() {
        return screen;
    }

    public Observable<Object> changes() {
        return Observable.merge(
                RxTextView.textChanges(valueEditText).map(new Function<CharSequence, Object>() {
                    @Override
                    public Object apply(CharSequence charSequence) throws Exception {
                        return new Object();
                    }
                }),
                RxTextView.textChanges(phoneNumberEditText).map(new Function<CharSequence, Object>() {
                    @Override
                    public Object apply(CharSequence charSequence) throws Exception {
                        return new Object();
                    }
                }),
                spinnerChanges);
    }

    public FilterType getFilterType() {
        return (FilterType) filterTypeSpinner.getSelectedItem();
    }

    public String getValue() {
        return valueEditText.getText().toString();
    }

    public String getTestPhoneNumber() {
        return phoneNumberEditText.getText().toString();
    }

    public void setTestPassed(boolean passed) {
        boolean hasValidText =
                valueEditText.getText().length() > 0 && getTestPhoneNumber().length() > 0;
        testPassIndicator.setVisibility(hasValidText ? VISIBLE : INVISIBLE);
        testPassIndicator.setImageResource(passed ? R.drawable.check : R.drawable.close);
    }
}
