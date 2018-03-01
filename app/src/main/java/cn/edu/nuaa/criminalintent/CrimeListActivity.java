package cn.edu.nuaa.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Meteor on 2018/1/14.
 */

public class CrimeListActivity extends BaseFragmentActivity {
    private static final String LOG_TAG=CrimeListActivity.class.getName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "Screen orientation :" + getWindowManager().getDefaultDisplay().getRotation());
        Log.d(LOG_TAG, "Screen width :" + getWindowManager().getDefaultDisplay().getWidth()+"Screen height :" + getWindowManager().getDefaultDisplay().getHeight());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected android.support.v4.app.Fragment createFragment() {
//        return new CrimeListFragment();//按照书上做的fragment
        return CustomListFragment.createInstance();//完成了第16张挑战用的fragment
    }
}
