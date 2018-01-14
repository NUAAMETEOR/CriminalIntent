package cn.edu.nuaa.criminalintent;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import cn.edu.nuaa.model.Crime;
import cn.edu.nuaa.model.CrimeRepository;

/**
 * Created by Meteor on 2018/1/13.
 */

public class CrimeListFragment extends ListFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.app_name);
        setListAdapter(new ArrayAdapter<Crime>(getActivity(),android.R.layout.simple_list_item_1, CrimeRepository.getCrimeRepository(getActivity()).getCrimeList()));
    }
}
