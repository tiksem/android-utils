package com.utilsframework.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.*;

/**
 * Created by CM on 9/2/2014.
 */
public class EditableSpinner extends FrameLayout {
    private Spinner spinner;
    private AutoCompleteTextView editText;
    private ToggleButton toggleButton;

    public EditableSpinner(Context context) {
        super(context);
    }

    public EditableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditableSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void updateSpinnerState() {
        if(toggleButton.isChecked()){
            spinner.setVisibility(GONE);
            editText.setVisibility(VISIBLE);
        } else {
            spinner.setVisibility(VISIBLE);
            editText.setVisibility(GONE);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        spinner = GuiUtilities.getFirstChildWithTypeRecursive(this, Spinner.class);
        if(spinner == null){
            throw new RuntimeException("could not find Spinner child");
        }

        editText = GuiUtilities.getFirstChildWithTypeRecursive(this, AutoCompleteTextView.class);
        if(editText == null){
            throw new RuntimeException("could not find AutoCompleteTextView child");
        }

        toggleButton = GuiUtilities.getFirstChildWithTypeRecursive(this, ToggleButton.class);
        if(toggleButton == null){
            throw new RuntimeException("could not find ToggleButton child");
        }

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSpinnerState();

                if(!isChecked){

                }
            }
        });

        updateSpinnerState();
    }
}
