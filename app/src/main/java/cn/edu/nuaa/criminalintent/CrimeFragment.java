package cn.edu.nuaa.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cn.edu.nuaa.common.CrimePhoto;
import cn.edu.nuaa.common.PictureUtils;
import cn.edu.nuaa.model.Crime;
import cn.edu.nuaa.model.CrimeRepository;

/**
 * Created by Meteor on 2018/1/12.
 */

public class CrimeFragment extends Fragment {
    public static final  String UUID_EXTRA_KEY     = "uuid";
    public static final  String DATE_DIALOG_TAG    = "DATE";
    public static final  String DATE_INTENT_TAG    = "DATE";
    public static final  String PHOTO_PATH         = "photo_path";
    public static final  String PHOTO_DIALOG       = "photo_dialog";
    public static final  int    DATE_REQUEST_CODE  = 0;
    public static final  int    PHOTO_REQUEST_CODE = 1;
    public static final  int    CONTACT_REQUEST_CODE = 2;
    private static final String LOG_TAG            = CrimeFragment.class.getName();
    private Crime     crimeInst;
    private EditText  titleText;
    private Button    dateButton;
    private CheckBox  solveOption;
    private ImageView photoThumb;
    private Button    chooseSuspect;
    private Button    sendCrime;

    public static CrimeFragment createInstance(UUID crimeId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(UUID_EXTRA_KEY, crimeId);
        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(bundle);
        return crimeFragment;
    }

    public void showPhoto() {
        CrimePhoto     crimePhoto     = crimeInst.getCrimePhoto();
        BitmapDrawable bitmapDrawable = null;
        if (crimePhoto != null && crimePhoto.getFilePath() != null) {
            bitmapDrawable = PictureUtils.getScaledDrawable(getActivity(), crimePhoto.getFilePath());
        }
        photoThumb.setImageDrawable(bitmapDrawable);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DATE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Date date = (Date) data.getSerializableExtra(DATE_INTENT_TAG);
                crimeInst.setCrimeDate(date);
                updateButtonText();
            }
        } else if (requestCode == PHOTO_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String photoFileName = data.getStringExtra(AutoFocusCameraFragment.PHOTO_FILE_NAME);
                if (photoThumb.getDrawable() != null) {
                    PictureUtils.freeBitmap(photoThumb);
                }
                crimeInst.setCrimePhoto(new CrimePhoto(photoFileName));
                showPhoto();
            }
        } else if (requestCode == CONTACT_REQUEST_CODE) {
            Uri contactUri=data.getData();
            ContentResolver contentResolver=getActivity().getContentResolver();
            String[] fields=new String[]{ContactsContract.Contacts.DISPLAY_NAME};
            Cursor cursor = contentResolver.query(contactUri, fields, null, null, null);
            if (cursor.getCount() == 0) {
                cursor.close();
                return;
            } else {
                cursor.moveToFirst();
                crimeInst.setSuspect(cursor.getString(0));
                chooseSuspect.setText(crimeInst.getSuspect());
                cursor.close();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(LOG_TAG, "onAttach called");
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

    @Nullable
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView called");
        View v = inflater.inflate(R.layout.crime_fragment, container, false);
        if (v == null) {
            Log.e(LOG_TAG, "inflate view failed");
            return null;
        }

        titleText = (EditText) v.findViewById(R.id.crimeTitle);
        titleText.setText(crimeInst.getCrimeTitle());
        titleText.setSelection(titleText.length());
        dateButton = v.findViewById(R.id.crimeDate);
        updateButtonText();
        dateButton.setEnabled(true);
        solveOption = v.findViewById(R.id.crimeSolved);
        solveOption.setChecked(crimeInst.isCrimeSolved());
        ImageButton imageButton = v.findViewById(R.id.start_camera);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                startActivityForResult(intent, PHOTO_REQUEST_CODE);
            }
        });
        photoThumb = v.findViewById(R.id.crime_photo);
        photoThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CrimePhoto crimePhoto = crimeInst.getCrimePhoto();
                if (crimePhoto == null || crimePhoto.getFilePath() == null) {
                    return;
                }
                ImageFragment.createInstance(crimePhoto.getFilePath()).show(getFragmentManager(), PHOTO_DIALOG);
            }
        });
        PackageManager pm = getActivity().getPackageManager();
        boolean hasCamera = (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
                pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) && Camera.getNumberOfCameras() > 0;
        if (!hasCamera) {
            imageButton.setEnabled(false);
        }
        if (titleText != null) {
            titleText.setSelectAllOnFocus(true);
            titleText.setFocusable(true);
            titleText.setFocusableInTouchMode(true);
            titleText.addTextChangedListener(new TextWatcher() {
                private String originalVal;

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    //此处可以保存更改前原来的值
                    originalVal = charSequence.toString();
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    /*String text = charSequence.toString().trim();
                    if (charSequence.length() != originalVal.length() && originalVal.equals(text)) {
                        titleText.setText(text);
                    }*/
                    crimeInst.setCrimeTitle(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
//                    titleText.setSelection(editable.length());
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
        chooseSuspect = v.findViewById(R.id.choose_suspect);
        if (crimeInst.getSuspect() != null) {
            chooseSuspect.setText(crimeInst.getSuspect());
        }
        chooseSuspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent,CONTACT_REQUEST_CODE);
            }
        });
        sendCrime = v.findViewById(R.id.send_crime);
        sendCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());

                //使用createChooser包装intent后，每次都会显示一个选择器。而不用createChooser包装，会使用系统默认的
                //选择器，该选择器可以选择默认的应用，不必每次都选
                intent = Intent.createChooser(intent, getString(R.string.send_report));
                startIntent(intent);
            }
        });
        return v;
    }

    public void startIntent(Intent intent) {
        PackageManager    pm   =getActivity().getPackageManager();
        List<ResolveInfo> list =pm.queryIntentActivities(intent, 0);
        if (list.size()>0) {
            startActivity(intent);
        }else{
            Toast.makeText(getActivity(),"不支持的操作",Toast.LENGTH_SHORT).show();
        }
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
        showPhoto();
        Log.d(LOG_TAG, "onStart called");
    }

    public String getCrimeReport() {
        String solvedStr;
        if (crimeInst.isCrimeSolved()) {
            solvedStr = getString(R.string.crime_report_solved);
        } else {
            solvedStr = getString(R.string.crime_report_unsolved);
        }
        String suspect = crimeInst.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String dateFormat = "EEE, MMM dd";
        String dateStr    = android.text.format.DateFormat.format(dateFormat, crimeInst.getCrimeDate()).toString();
        String report     = getString(R.string.crime_report_content, crimeInst.getCrimeTitle(), dateStr, solvedStr, suspect);
        return report;
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
        PictureUtils.freeBitmap(photoThumb);
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

    private void updateButtonText() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String           currentTime      = simpleDateFormat.format(crimeInst.getCrimeDate());
        dateButton.setText(currentTime);
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
