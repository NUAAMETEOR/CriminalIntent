package cn.edu.nuaa.criminalintent;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.nuaa.model.Crime;
import cn.edu.nuaa.model.CrimeAdapter;
import cn.edu.nuaa.model.CrimeRepository;

/**
 * Created by Meteor on 2018/1/27.
 * 这个fragment是第十六章的挑战练习，自定义一个listview，可自动显示空视图和非空视图
 */

public class CustomListFragment extends Fragment {
    private boolean  isSubtitleVisible = false;
    private ListView listView          = null;
    private MenuItem addMenuItem       = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //如果数据为空，则显示id为android:id/empty的组件;如果数据不为空，显示id为android:id/list的组件
        View view = inflater.inflate(R.layout.custom_list_view_fragment, container, false);
        listView = view.findViewById(android.R.id.list);
        final TextView textView = view.findViewById(android.R.id.empty);
        listView.setEmptyView(textView);
        listView.setAdapter(new CrimeAdapter(getActivity()));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(addMenuItem);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Crime crime = ((CrimeAdapter) adapterView.getAdapter()).getItem(i);
//        Intent intent = new Intent(getActivity(), CrimeActivity.class);
                Intent intent = new Intent(getActivity(), ViewPageActivity.class);
                intent.putExtra(CrimeFragment.UUID_EXTRA_KEY, crime.getCrimeId());
                startActivityForResult(intent, 0);
            }
        });
        return view;
    }

    public static Fragment createInstance() {
        return new CustomListFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ((CrimeAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        getActivity().setTitle(R.string.app_name);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (isSubtitleVisible) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.subtitle);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.crime_list_menu, menu);
        addMenuItem = menu.findItem(R.id.new_crime_menu);
        if (isSubtitleVisible) {
            MenuItem menuItem = menu.findItem(R.id.show_subtitle_menu);
            if (menuItem != null && isSubtitleVisible) {
                menuItem.setTitle(R.string.hide_subtitle);
            }
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime_menu: {
                Crime crime = new Crime();
                CrimeRepository.getCrimeRepository(getActivity()).getCrimeList().add(crime);
                Intent intent = new Intent(getActivity(), ViewPageActivity.class);
                intent.putExtra(CrimeFragment.UUID_EXTRA_KEY, crime.getCrimeId());
                startActivityForResult(intent, 0);
                return true;
            }
            case R.id.show_subtitle_menu: {
                android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                if (actionBar.getSubtitle() == null) {
                    actionBar.setSubtitle(R.string.subtitle);
                    item.setTitle(R.string.hide_subtitle);
                    isSubtitleVisible = true;
                } else {
                    actionBar.setSubtitle(null);
                    item.setTitle(R.string.show_subtitle);
                    isSubtitleVisible = false;
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
