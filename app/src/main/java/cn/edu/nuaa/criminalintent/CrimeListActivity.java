package cn.edu.nuaa.criminalintent;

import android.app.Fragment;

/**
 * Created by Meteor on 2018/1/14.
 */

public class CrimeListActivity extends BaseFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
