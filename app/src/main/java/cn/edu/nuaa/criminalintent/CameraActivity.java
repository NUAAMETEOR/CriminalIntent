package cn.edu.nuaa.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Meteor on 2018/1/31.
 */

public class CameraActivity extends BaseFragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (this instanceof AppCompatActivity) {
            ActionBar actionBar=getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected Fragment createFragment() {
        return new AutoFocusCameraFragment();
    }
}
