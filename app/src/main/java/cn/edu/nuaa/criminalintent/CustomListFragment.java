package cn.edu.nuaa.criminalintent;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            registerForContextMenu(listView);//低版本使用上下文浮动菜单
        } else {
            listView.setChoiceMode(listView.CHOICE_MODE_MULTIPLE_MODAL);//高版本使用上下文操作栏
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                    /**
                     * i表示当前选中的item索引，l表示当前选中的item的id，boolean表示是否选中
                     * 日志显示i总是跟l相等
                     */
                    Log.i("ActionMode", "item(" + i + ") selected,id:" + l + ",check state:" + b);
                }

                @Override
                public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                    actionMode.getMenuInflater().inflate(R.menu.crime_list_context_menu, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.delete_crime_menu:
                            CrimeAdapter adapter = (CrimeAdapter) listView.getAdapter();
                            int count = adapter.getCount();
                            //一定要从后往前删，不然如果先删除前面的，后面的索引会变，就不对了
                            for (int i = count - 1; i >= 0; i--) {
                                if (listView.isItemChecked(i)) {
                                    CrimeRepository.getCrimeRepository(getActivity().getApplicationContext()).deleteCrime(i);
                                }
                            }
                            actionMode.finish();
                            adapter.notifyDataSetChanged();
                            return true;
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode actionMode) {

                }
            });
        }
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.crime_list_context_menu, menu);
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_crime_menu: {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                CrimeRepository.getCrimeRepository(getActivity().getApplicationContext()).deleteCrime(info.position);
                ((CrimeAdapter) listView.getAdapter()).notifyDataSetChanged();
                return true;
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeRepository.getCrimeRepository(getActivity().getApplicationContext()).saveCrime();
    }
}
