package com.arp.datepickerpreference;

import android.content.Context;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Aswin R Pillai on 15-02-2016.
 * Custom Date Picker for saving preference
 */

public class DatePickerPreference extends DialogPreference {

    int year, month, day;
    Context mContext;
    DatePicker datePicker;
    AttributeSet mAttrs;
    String type;
    long time;

    public DatePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAttrs = attrs;
        type = getKey();

        String dateString = PreferenceManager.getDefaultSharedPreferences(mContext).getString(type, "Set Date");
        time = stringToDate(dateString).getTime();
        setSummary(dateString);
    }

    public static String dateToString(Date date) {
        String dateString;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
        dateString = dateFormat.format(date);
        return dateString;
    }

    public static Date stringToDate(String dateString) {
        Date date = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
        if (dateString != null) {
            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                date = Calendar.getInstance().getTime();
            }
        }
        return date;
    }

    @Override
    protected View onCreateDialogView() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker = new DatePicker(mContext);
        datePicker.updateDate(year, month, day);
        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");
        return datePicker;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
            calendar.set(Calendar.MONTH, datePicker.getMonth());
            calendar.set(Calendar.YEAR, datePicker.getYear());
            String dateString = dateToString(calendar.getTime());
            PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString(type, dateString).commit();
            setSummary(dateString);
            Toast toast = Toast.makeText(mContext, "New Date Set : " + dateString, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
