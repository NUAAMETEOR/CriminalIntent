package cn.edu.nuaa.criminalintent;

import android.os.Bundle;
import android.util.Log;

import java.util.UUID;

public class CrimeActivity extends BaseFragmentActivity {
    private static final String LOG_TAG = CrimeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate called");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected android.support.v4.app.Fragment createFragment() {
        UUID uuid = (UUID) getIntent().getSerializableExtra(CrimeFragment.UUID_EXTRA_KEY);
        return CrimeFragment.createInstance(uuid);
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
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy called");
    }
}


//01-12 22:41:30.320 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeActivity: onCreate called
//        01-12 22:41:30.640 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeFragment: onCreate called
//        01-12 22:41:30.670 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeFragment: onCreateView called
//        01-12 22:41:30.690 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeFragment: onActivityCreate called
//        01-12 22:41:30.690 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeActivity: onStart called
//        01-12 22:41:30.690 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeFragment: onStart called
//        01-12 22:41:30.690 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeActivity: onResume called
//        01-12 22:41:30.690 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeFragment: onResume called

//01-12 22:43:18.620 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeFragment: onPause called
//        01-12 22:43:18.620 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeActivity: onPause called
//        01-12 22:43:18.940 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeFragment: onStop called
//        01-12 22:43:18.950 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeActivity: onStop called
//        01-12 22:43:18.950 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeFragment: onDestroyView called
//        01-12 22:43:18.950 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeFragment: onDestroy called
//        01-12 22:43:18.950 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeFragment: onDetach called
//        01-12 22:43:18.950 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeActivity: onDestroy called
