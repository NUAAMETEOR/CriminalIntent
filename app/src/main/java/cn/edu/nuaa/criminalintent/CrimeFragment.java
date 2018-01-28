package cn.edu.nuaa.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import cn.edu.nuaa.model.Crime;
import cn.edu.nuaa.model.CrimeRepository;

/**
 * Created by Meteor on 2018/1/12.
 */

public class CrimeFragment extends Fragment {
    public static final  String UUID_EXTRA_KEY    = "uuid";
    private static final String LOG_TAG           = CrimeFragment.class.getName();
    public static final  String DATE_DIALOG_TAG   = "DATE";
    public static final  String DATE_INTENT_TAG   = "DATE";
    public static final  int    DATE_REQUEST_CODE = 0;
    private Crime    crimeInst;
    private EditText titleText;
    private Button   dateButton;
    private CheckBox solveOption;

    public static CrimeFragment createInstance(UUID crimeId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(UUID_EXTRA_KEY, crimeId);
        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(bundle);
        return crimeFragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DATE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Date date = (Date) data.getSerializableExtra(DATE_INTENT_TAG);
                crimeInst.setCrimeDate(date);
                updateButtonText();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(LOG_TAG, "onAttach called");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeAsUp: {
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            }
            case 16908332: {//返回键，id不是R.id.homeAsUp,是这个,fragment会自动根据manifest文件中配置的父activity返回，无需自行处理
                return false;
            }
            default:
                return false;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID uuid = (UUID) getArguments().getSerializable(UUID_EXTRA_KEY);
        crimeInst = CrimeRepository.getCrimeRepository(getActivity().getApplicationContext()).getCrime(uuid);
//        getActivity().setTitle(R.string.activity_crime_title);

    }

    private void updateButtonText() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String           currentTime      = simpleDateFormat.format(crimeInst.getCrimeDate());
        dateButton.setText(currentTime);
    }

    @Nullable
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView called");
        View v = inflater.inflate(R.layout.crime_fragment, container, false);
        if (v == null) {
            Log.e(LOG_TAG, "inflate view failed");
            return null;
        }

        titleText = (EditText) v.findViewById(R.id.crimeTitle);
        titleText.setText(crimeInst.getCrimeTitle());
        dateButton = v.findViewById(R.id.crimeDate);
        updateButtonText();
        dateButton.setEnabled(true);
        solveOption = v.findViewById(R.id.crimeSolved);
        solveOption.setChecked(crimeInst.isCrimeSolved());
        if (titleText != null) {
            titleText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    crimeInst.setCrimeTitle(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
        solveOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                crimeInst.setCrimeSolved(b);
            }
        });
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager    fragmentManager    = getActivity().getSupportFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment.createInstance(crimeInst.getCrimeDate());
                datePickerFragment.setTargetFragment(CrimeFragment.this, DATE_REQUEST_CODE);
                datePickerFragment.show(fragmentManager, DATE_DIALOG_TAG);
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityName(getActivity()) != null) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        Log.d(LOG_TAG, "onActivityCreate called");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop called");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "onDestroyView called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy called");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(LOG_TAG, "onDetach called");
    }

    public Crime getCrimeInst() {
        return crimeInst;
    }

    public void setCrimeInst(Crime crimeInst) {
        this.crimeInst = crimeInst;
    }

    public Button getDateButton() {
        return dateButton;
    }

    public void setDateButton(Button dateButton) {
        this.dateButton = dateButton;
    }

    public CheckBox getSolveOption() {
        return solveOption;
    }

    public void setSolveOption(CheckBox solveOption) {
        this.solveOption = solveOption;
    }

    public EditText getTitleText() {
        return titleText;
    }

    public void setTitleText(EditText titleText) {
        this.titleText = titleText;
    }
}
