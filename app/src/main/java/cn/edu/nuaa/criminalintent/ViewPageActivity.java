package cn.edu.nuaa.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.UUID;

import cn.edu.nuaa.animation.ViewPageAnimation;
import cn.edu.nuaa.model.Crime;
import cn.edu.nuaa.model.CrimeRepository;

/**
 * Created by Meteor on 2018/1/21.
 */

public class ViewPageActivity extends AppCompatActivity {
    private ViewPager viewPager = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPager = new ViewPager(this);
        viewPager.setId(R.id.viewPage);
        setContentView(viewPager);

        final ArrayList<Crime> crimeArrayList = CrimeRepository.getCrimeRepository(ViewPageActivity.this).getCrimeList();
        Intent                 intent         = getIntent();
        Crime                  crime          = CrimeRepository.getCrimeRepository(this).getCrime((UUID) (intent.getSerializableExtra(CrimeFragment.UUID_EXTRA_KEY)));
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return CrimeFragment.createInstance(crimeArrayList.get(position).getCrimeId());
            }

            @Override
            public int getCount() {
                return crimeArrayList.size();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ViewPageActivity.this.setTitle(crimeArrayList.get(position).getCrimeTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(crimeArrayList.indexOf(crime));
        viewPager.setPageTransformer(true, new ViewPageAnimation());
    }
}
