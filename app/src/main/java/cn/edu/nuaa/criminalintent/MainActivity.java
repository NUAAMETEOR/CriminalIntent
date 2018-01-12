package cn.edu.nuaa.criminalintent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
            if (fragment == null) {
                fragment = new CrimeFragment();
                fragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
            }
        } else {
            Log.e(LOG_TAG, "can not get fragment manager");
        }
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


//01-12 22:41:30.320 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.MainActivity: onCreate called
//        01-12 22:41:30.640 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeFragment: onCreate called
//        01-12 22:41:30.670 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeFragment: onCreateView called
//        01-12 22:41:30.690 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeFragment: onActivityCreate called
//        01-12 22:41:30.690 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.MainActivity: onStart called
//        01-12 22:41:30.690 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeFragment: onStart called
//        01-12 22:41:30.690 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.MainActivity: onResume called
//        01-12 22:41:30.690 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeFragment: onResume called

//01-12 22:43:18.620 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeFragment: onPause called
//        01-12 22:43:18.620 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.MainActivity: onPause called
//        01-12 22:43:18.940 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeFragment: onStop called
//        01-12 22:43:18.950 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.MainActivity: onStop called
//        01-12 22:43:18.950 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeFragment: onDestroyView called
//        01-12 22:43:18.950 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeFragment: onDestroy called
//        01-12 22:43:18.950 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.CrimeFragment: onDetach called
//        01-12 22:43:18.950 17793-17793/cn.edu.nuaa.criminalintent D/cn.edu.nuaa.criminalintent.MainActivity: onDestroy called