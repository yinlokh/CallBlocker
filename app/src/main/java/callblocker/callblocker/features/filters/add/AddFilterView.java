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
import android.widget.Spinner;

import com.jakewharton.rxbinding2.view.RxView;
import com.wealthfront.magellan.Screen;
import com.wealthfront.magellan.ScreenView;

import callblocker.callblocker.R;
import callblocker.callblocker.models.FilterType;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class AddFilterView extends FrameLayout implements ScreenView {

    private Screen screen;
    private Spinner filterTypeSpinner;
    private EditText valueEditText;
    private Button addRuleButton;
    private EditText phoneNumberEditText;
    private Button testRuleButton;

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        valueEditText = (EditText) findViewById(R.id.value);
        addRuleButton = (Button) findViewById(R.id.add_button);
        phoneNumberEditText = (EditText) findViewById(R.id.phone_number);
        testRuleButton = (Button) findViewById(R.id.test_button);
    }

    @Override
    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    @Override
    public Screen getScreen() {
        return screen;
    }

    public Observable<Object> addButtonClicks() {
        return RxView.clicks(addRuleButton);
    }

    public Observable<String> testPhoneNumbers() {
        return RxView.clicks(testRuleButton).map(new Function<Object, String>() {
            @Override
            public String apply(Object o) throws Exception {
                return phoneNumberEditText.getText().toString();
            }
        });
    }

    public FilterType getFilterType() {
        return (FilterType) filterTypeSpinner.getSelectedItem();
    }

    public String getValue() {
        return valueEditText.getText().toString();
    }
}
