package com.utilsframework.android.view;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;

/**
 * Created by CM on 9/2/2014.
 */
public abstract class TextChangedAfterTimeListener {
    private Runnable listenerRunnable;
    private TextWatcher textWatcher;

    public interface Remover {
        public void removeListener();
    }

    private long delay;

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    protected abstract void onTextChanged(String newText);

    public Remover setForEditText(final EditText editText){
        final WeakReference<EditText> editTextWeakReference = new WeakReference<EditText>(editText);
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                EditText editText = editTextWeakReference.get();
                if(editText == null){
                    return;
                }

                if (listenerRunnable != null) {
                    editText.removeCallbacks(listenerRunnable);
                }

                listenerRunnable = new Runnable() {
                    @Override
                    public void run() {
                        TextChangedAfterTimeListener.this.onTextChanged(s.toString());
                    }
                };
                editText.postDelayed(listenerRunnable, delay);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editText.addTextChangedListener(textWatcher);

        return new Remover() {
            @Override
            public void removeListener() {
                editText.removeTextChangedListener(textWatcher);
            }
        };
    }
}
