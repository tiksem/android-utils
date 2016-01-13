package com.utilsframework.android.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.utils.framework.strings.Strings;

import java.util.List;

/**
 * Created by CM on 6/24/2015.
 */
public class EditTextUtils {
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void applyInputMaxLinesBehavior(EditText editText) {
        if (Build.VERSION.SDK_INT < 16) {
            throw new UnsupportedOperationException("Build.VERSION.SDK_INT < 16");
        }

        applyInputMaxLinesBehavior(editText, -1);
    }

    private static void applyEditTextsInputMaxLinesBehavior(ViewGroup viewGroup, int limit) {
        List<EditText> children = GuiUtilities.getAllChildrenRecursive(viewGroup, EditText.class);
        for (EditText editText : children) {
            applyInputMaxLinesBehavior(editText, limit);
        }
    }

    private static OnKeyListenerSetter applyInputMaxLinesBehavior(EditText editText, int limit) {
        ApplyInputLimitOnKeyListener onKeyListener = new ApplyInputLimitOnKeyListener(editText, limit);
        editText.setOnKeyListener(onKeyListener);
        return onKeyListener;
    }

    public static void showKeyboard(EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) editText.getContext().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private static class ApplyInputLimitOnKeyListener implements View.OnKeyListener, OnKeyListenerSetter {
        private final EditText editText;
        private final int limit;
        View.OnKeyListener additionalOnKeyListener;

        public ApplyInputLimitOnKeyListener(EditText editText, int limit) {
            this.editText = editText;
            this.limit = limit;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            // if enter is pressed start calculating
            if (keyCode == KeyEvent.KEYCODE_ENTER
                    && event.getAction() == KeyEvent.ACTION_UP) {

                // get EditText text
                CharSequence text = editText.getText();
                int maxLines = limit;
                if (limit < 0) {
                    maxLines = editText.getMaxLines();
                }
                text = Strings.limitCharOccurrences(text, '\n', maxLines);
                editText.setText(text);
            }

            if (additionalOnKeyListener != null) {
                additionalOnKeyListener.onKey(v, keyCode, event);
            }

            return false;
        }

        @Override
        public void setOnKeyListener(View.OnKeyListener onKeyListener) {
            additionalOnKeyListener = onKeyListener;
        }
    }

    public static void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager)editText.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
