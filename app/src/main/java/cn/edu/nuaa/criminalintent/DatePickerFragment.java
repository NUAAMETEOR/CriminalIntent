package cn.edu.nuaa.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Meteor on 2018/1/22.
 */

public class DatePickerFragment extends DialogFragment {
    public static final String DATE_TAG="date";
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View datePickerView = getActivity().getLayoutInflater().inflate(R.layout.date_pick, null);
        DatePicker datePicker = datePickerView.findViewById(R.id.crime_datePicker);
        Date date = (Date) (getArguments().getSerializable(DATE_TAG));
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar(i, i1, i2);
                getArguments().putSerializable(DATE_TAG, gregorianCalendar.getTime());
            }
        });
        return new AlertDialog.Builder(getActivity()).setView(datePickerView).setTitle(R.string.crime_date).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (getTargetFragment() == null) {
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(CrimeFragment.DATE_INTENT_TAG, getArguments().getSerializable(DATE_TAG));
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
            }
        }).create();
    }

    public static DatePickerFragment createInstance(Date date) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DATE_TAG,date);
        DatePickerFragment datePickerFragment=new DatePickerFragment();
        datePickerFragment.setArguments(bundle);
        return datePickerFragment;
    }
}
