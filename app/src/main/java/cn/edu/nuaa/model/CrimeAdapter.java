package cn.edu.nuaa.model;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import cn.edu.nuaa.criminalintent.R;

/**
 * Created by Meteor on 2018/1/14.
 */

public class CrimeAdapter extends ArrayAdapter<Crime> {
    private Context context;

    public CrimeAdapter(@NonNull Context context) {
        super(context, 0, CrimeRepository.getCrimeRepository(context.getApplicationContext()).getCrimeList());
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.crime_list_item, null);
        }
        TextView         titleText        = convertView.findViewById(R.id.crimeItemTitle);
        TextView         dateText         = convertView.findViewById(R.id.crimeItemDate);
        CheckBox         solvedCheckBox   = convertView.findViewById(R.id.crimeItemSolved);
        Crime            crime            = getItem(position);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        titleText.setText(crime.getCrimeTitle());
        dateText.setText(simpleDateFormat.format(crime.getCrimeDate()));
        solvedCheckBox.setChecked(crime.isCrimeSolved());
        solvedCheckBox.setFocusable(false);
        solvedCheckBox.setFocusableInTouchMode(false);
        return convertView;
    }
}
