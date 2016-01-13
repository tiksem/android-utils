package com.utilsframework.android.view;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by CM on 2/20/2015.
 */
public class Spinners {
    public static void initSpinnerFromStringArray(Spinner spinner, int arrayId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(spinner.getContext(),
                arrayId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
