package cn.edu.nuaa.criminalintent;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Meteor on 2018/1/14.
 */

public class CrimeListActivity extends BaseFragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
