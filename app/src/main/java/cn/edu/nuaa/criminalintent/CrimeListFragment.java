package cn.edu.nuaa.criminalintent;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import cn.edu.nuaa.model.Crime;
import cn.edu.nuaa.model.CrimeAdapter;

/**
 * Created by Meteor on 2018/1/13.
 */

public class CrimeListFragment extends ListFragment {
    private static String  LOG_TAG=CrimeListFragment.class.getName();

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Crime crime = ((CrimeAdapter) getListAdapter()).getItem(position);
        Log.d(LOG_TAG, crime.getCrimeTitle() + "clicked");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.app_name);
        setListAdapter(new CrimeAdapter(getActivity(), this));
    }
}
