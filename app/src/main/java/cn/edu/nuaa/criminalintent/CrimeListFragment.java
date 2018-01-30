package cn.edu.nuaa.criminalintent;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import cn.edu.nuaa.model.Crime;
import cn.edu.nuaa.model.CrimeAdapter;
import cn.edu.nuaa.model.CrimeRepository;

/**
 * Created by Meteor on 2018/1/13.
 */

public class CrimeListFragment extends ListFragment {
    private static String  LOG_TAG            = CrimeListFragment.class.getName();
    private static int     CRIME_REQUEST_CODE = 0;
    private        boolean isSubtitleVisible  = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=super.onCreateView(inflater, container, savedInstanceState);
        registerForContextMenu(v);
        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Crime crime = ((CrimeAdapter) getListAdapter()).getItem(position);
//        Intent intent = new Intent(getActivity(), CrimeActivity.class);
        Intent intent = new Intent(getActivity(), ViewPageActivity.class);
        intent.putExtra(CrimeFragment.UUID_EXTRA_KEY, crime.getCrimeId());
        startActivityForResult(intent, CRIME_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ((CrimeAdapter) getListAdapter()).notifyDataSetChanged();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        getActivity().setTitle(R.string.app_name);
        setListAdapter(new CrimeAdapter(getActivity()));

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
        setEmptyText("还没有crime记录！");
        ((CrimeAdapter) getListAdapter()).notifyDataSetChanged();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.crime_list_menu, menu);
        if (isSubtitleVisible) {
            MenuItem menuItem = menu.findItem(R.id.show_subtitle_menu);
            if (menuItem != null && isSubtitleVisible) {
                menuItem.setTitle(R.string.hide_subtitle);
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.crime_list_context_menu,menu);
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeRepository.getCrimeRepository(getActivity().getApplicationContext()).saveCrime();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime_menu: {
                Crime crime = new Crime();
                CrimeRepository.getCrimeRepository(getActivity().getApplicationContext()).getCrimeList().add(crime);
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
