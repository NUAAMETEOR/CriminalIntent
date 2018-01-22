package cn.edu.nuaa.criminalintent;

/**
 * Created by Meteor on 2018/1/14.
 */

public class CrimeListActivity extends BaseFragmentActivity {
    @Override
    protected android.support.v4.app.Fragment createFragment() {
        return new CrimeListFragment();
    }
}
