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
        if (getActionBar() != null) {
            new AlertDialog.Builder(this).setTitle("alert").setMessage("get action bar succ").create().show();
        }
    }

    @Override
    protected android.support.v4.app.Fragment createFragment() {
        return new CrimeListFragment();
    }
}
