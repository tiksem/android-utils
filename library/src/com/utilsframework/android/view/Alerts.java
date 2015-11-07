package com.utilsframework.android.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.utilsframework.android.R;
import com.utilsframework.android.threading.AsyncOperationCallback;
import com.utilsframework.android.threading.Cancelable;
import com.utilsframework.android.time.TimeUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * User: Tikhonenko.S
 * Date: 04.04.14
 * Time: 17:21
 */
public final class Alerts {
    public static class YesNoAlertSettings {
        public CharSequence title = "";
        public CharSequence message = null;
        public int yesButtonText = R.string.yes;
        public int noButtonText = R.string.no;
        public OnYes onYes;
        public OnNo onNo;
    }

    public static void showYesNoAlert(Context context, OnYes onYes, int messageId) {
        YesNoAlertSettings settings = new YesNoAlertSettings();
        settings.message = context.getString(messageId);
        settings.onYes = onYes;
        showYesNoAlert(context, settings);
    }

    public static void showYesNoAlert(Context context, final YesNoAlertSettings settings) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setPositiveButton(settings.yesButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(settings.onYes != null){
                    settings.onYes.onYes();
                }
            }
        });

        builder.setNegativeButton(settings.noButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (settings.onNo != null) {
                    settings.onNo.onNo();
                }
            }
        });

        builder.setTitle(settings.title);
        if (settings.message != null) {
            builder.setMessage(settings.message);
        }

        builder.create().show();
    }

    public static class OkAlertSettings {
        public CharSequence title = "";
        public CharSequence message = "";
        public CharSequence okButtonText = "OK";
        public OnOk onOk;
    }

    public static void showOkButtonAlert(Context context, final OkAlertSettings settings) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setPositiveButton(settings.okButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(settings.onOk != null){
                    settings.onOk.onOk();
                }
            }
        });

        builder.setTitle(settings.title);
        builder.setMessage(settings.message);

        builder.create().show();
    }
    public static void showOkButtonAlert(Context context, String message) {
        OkAlertSettings okAlertSettings = new OkAlertSettings();
        okAlertSettings.message = message;
        showOkButtonAlert(context, okAlertSettings);
    }
    public static void showOkButtonAlert(Context context, int message) {
        showOkButtonAlert(context, context.getString(message));
    }

    public static AlertDialog showAlertWithCustomView(Context context, View view, CharSequence message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(view);
        builder.setMessage(message);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    public static class InputAlertSettings {
        public int message;
        public int title;
        public int ok = R.string.ok;
        public int cancel = R.string.cancel;
        public int layoutId = R.layout.input_alert;
        public int hint = 0;
        public OnInputOk onInputOk;
    }

    public interface OnInputOk {
        void onOk(String value);
    }

    public static AlertDialog showAlertWithInput(Context context, int message, OnInputOk onInputOk) {
        InputAlertSettings settings = new Alerts.InputAlertSettings();
        settings.message = message;
        settings.onInputOk = onInputOk;
        return showAlertWithInput(context, settings);
    }

    public static AlertDialog showAlertWithInput(Context context, final InputAlertSettings settings) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View root = View.inflate(context, settings.layoutId, null);
        final EditText editText = GuiUtilities.getFirstChildWithTypeRecursive(root, EditText.class);
        builder.setView(root);

        builder.setPositiveButton(settings.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (settings.onInputOk != null) {
                    settings.onInputOk.onOk(editText.getText().toString());
                }
            }
        });

        if (settings.cancel != 0) {
            builder.setNegativeButton(settings.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        if (settings.title != 0) {
            builder.setTitle(settings.title);
        }
        if (settings.message != 0) {
            builder.setMessage(settings.message);
        }

        if (settings.hint != 0) {
            editText.setHint(settings.hint);
        }

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final Button okButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                okButton.setEnabled(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        okButton.setEnabled(false);

        return alertDialog;
    }

    public static ProgressDialog showCircleProgressDialog(Context context, int messageId) {
        return showCircleProgressDialog(context, context.getString(messageId));
    }


    public static ProgressDialog showCircleProgressDialog(Context context, CharSequence message) {
        return ProgressDialog.show(context, null, message);
    }

    public static <T> Cancelable runAsyncOperationWithCircleLoading(Context context,
                                                                CharSequence message,
                                                                final AsyncOperationCallback<T> callback) {
        final ProgressDialog progressDialog = showCircleProgressDialog(context, message);

        new AsyncTask<Void, Void, T>(){
            @Override
            protected T doInBackground(Void... params) {
                return callback.runOnBackground();
            }

            @Override
            protected void onPostExecute(T result) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    callback.onFinish(result);
                }
            }
        }.execute();

        // TODO improve cancelable
        return new Cancelable() {
            @Override
            public void cancel() {
                progressDialog.dismiss();
            }
        };
    }

    public static <T> Cancelable runAsyncOperationWithCircleLoading(Context context,
                                                                    int resourceId,
                                                                    final AsyncOperationCallback<T> callback) {
        return runAsyncOperationWithCircleLoading(context, context.getString(resourceId), callback);
    }

    public static <T> Cancelable runAsyncOperationWithCircleLoading(Context context,
                                                                    CharSequence message,
                                                                    final Runnable operation) {
        return runAsyncOperationWithCircleLoading(context, message, new AsyncOperationCallback<Object>() {
            @Override
            public Object runOnBackground() {
                operation.run();
                return null;
            }

            @Override
            public void onFinish(Object result) {

            }
        });
    }

    public static <T> Cancelable runAsyncOperationWithCircleLoading(Context context,
                                                                    int resourceId,
                                                                    final Runnable operation) {
        return runAsyncOperationWithCircleLoading(context, context.getString(resourceId), operation);
    }

    private static class PickerSettings {
        public CharSequence title;
        public CharSequence message;
        public int okButtonNameId = R.string.set;
        public int cancelButtonNameId = R.string.cancel;
    }

    public static class NumberPickerAlertSettings extends PickerSettings {
        public int min = 0;
        public int max = 10;
        public Integer current;
        public OnNumberSelected onNumberSelected;
    }

    private static AlertDialog.Builder setupPickerDialogBuilder(Context context, PickerSettings settings) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(settings.message);
        builder.setTitle(settings.title);

        builder.setNegativeButton(settings.cancelButtonNameId,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder;
    }

    public static AlertDialog showNumberPickerAlert(Context context, final NumberPickerAlertSettings settings) {
        AlertDialog.Builder builder = setupPickerDialogBuilder(context, settings);

        View view = View.inflate(context, R.layout.number_picker_dialog, null);

        final NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        numberPicker.setMinValue(settings.min);
        numberPicker.setMaxValue(settings.max);
        numberPicker.setWrapSelectorWheel(false);
        if (settings.current == null) {
            settings.current = settings.min;
        }
        numberPicker.setValue(settings.current);

        builder.setView(view);

        builder.setPositiveButton(settings.okButtonNameId,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (settings.onNumberSelected != null) {
                            int value = numberPicker.getValue();
                            settings.onNumberSelected.onSelected(value);
                        }
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    public interface OnDateTimeSelected {
        void onSelected(GregorianCalendar gregorianCalendar);
    }

    public static class DateTimePickerSettings extends PickerSettings {
        public OnDateTimeSelected onDateTimeSelected;
        public long currentTimeInMillis;
        public boolean clearTime;
    }

    public static AlertDialog showTimePickerAlert(Context context, final DateTimePickerSettings settings) {
        AlertDialog.Builder builder = setupPickerDialogBuilder(context, settings);

        View view = View.inflate(context, R.layout.time_picker_dialog, null);
        final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
        final GregorianCalendar calendar = new GregorianCalendar();
        long currentTime = calendar.getTimeInMillis();
        if (settings.currentTimeInMillis > currentTime) {
            calendar.setTimeInMillis(settings.currentTimeInMillis);
        }
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));

        builder.setView(view);

        builder.setPositiveButton(settings.okButtonNameId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (settings.onDateTimeSelected != null) {
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                    settings.onDateTimeSelected.onSelected(calendar);
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    private static void fixUpDatePickerCalendarView(DatePicker datePicker, Calendar date) {
        // Workaround for CalendarView bug relating to setMinDate():
        // https://code.google.com/p/android/issues/detail?id=42750
        // Set then reset the date on the calendar so that it properly
        // shows today's date. The choice of 24 months is arbitrary.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            final CalendarView cal = datePicker.getCalendarView();
            if (cal != null) {
                date.add(Calendar.MONTH, 24);
                cal.setDate(date.getTimeInMillis(), false, true);
                date.add(Calendar.MONTH, -24);
                cal.setDate(date.getTimeInMillis(), false, true);
            }
        }
    }

    public static AlertDialog showDatePickerAlert(Context context, final DateTimePickerSettings settings) {
        AlertDialog.Builder builder = setupPickerDialogBuilder(context, settings);

        View view = View.inflate(context, R.layout.date_picker_dialog, null);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
        datePicker.setSpinnersShown(false);
        datePicker.setCalendarViewShown(true);
        final GregorianCalendar calendar = new GregorianCalendar();
        long currentTime = calendar.getTimeInMillis();
        if (settings.currentTimeInMillis > currentTime) {
            calendar.setTimeInMillis(settings.currentTimeInMillis);
        }
        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        GregorianCalendar minimumDateCalendar = new GregorianCalendar();
        minimumDateCalendar.set(Calendar.MINUTE, 0);
        minimumDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
        minimumDateCalendar.set(Calendar.SECOND, 0);
        minimumDateCalendar.set(Calendar.MILLISECOND, 0);
        datePicker.setMinDate(minimumDateCalendar.getTimeInMillis());
        fixUpDatePickerCalendarView(datePicker, minimumDateCalendar);

        if (settings.clearTime) {
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        }

        builder.setView(view);

        builder.setPositiveButton(settings.okButtonNameId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (settings.onDateTimeSelected != null) {
                    calendar.set(Calendar.YEAR, datePicker.getYear());
                    calendar.set(Calendar.MONTH, datePicker.getMonth());
                    calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                    settings.onDateTimeSelected.onSelected(calendar);
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    public interface OnDateRangeSelected {
        void onSelected(GregorianCalendar start, GregorianCalendar end);
    }

    public static class DateRangePickerSettings extends PickerSettings {
        public OnDateRangeSelected onDateRangeSelected;
        public int rootLayoutId = R.layout.date_range_picker;
        public int invalidRangeErrorId = R.string.invalid_date_range_error;
    }

    public static AlertDialog showDateRangePickerAlert(final Context context, final DateRangePickerSettings settings) {
        AlertDialog.Builder builder = setupPickerDialogBuilder(context, settings);

        View view = View.inflate(context, settings.rootLayoutId, null);
        final CalendarView start = (CalendarView) view.findViewById(R.id.start);
        final CalendarView end = (CalendarView) view.findViewById(R.id.end);
        builder.setView(view);

        long startOfCurrentDay = TimeUtils.getStartOfCurrentDay();
        start.setDate(startOfCurrentDay);
        end.setDate(startOfCurrentDay + TimeUtils.MILLISECONDS_IN_DAY);

        builder.setPositiveButton(settings.okButtonNameId, null);

        final AlertDialog alertDialog = builder.create();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start.getDate() >= end.getDate()) {
                    Toasts.error(context, context.getString(settings.invalidRangeErrorId));
                } else {
                    if (settings.onDateRangeSelected != null) {
                        GregorianCalendar first = new GregorianCalendar();
                        GregorianCalendar second = new GregorianCalendar();
                        first.setTimeInMillis(start.getDate());
                        second.setTimeInMillis(end.getDate());
                        settings.onDateRangeSelected.onSelected(first,
                                second);
                    }
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog.show();
        return alertDialog;
    }
}
