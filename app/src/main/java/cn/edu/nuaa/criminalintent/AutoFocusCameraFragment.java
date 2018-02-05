package cn.edu.nuaa.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import static android.content.ComponentCallbacks2.TRIM_MEMORY_BACKGROUND;

/**
 * Created by Meteor on 2018/2/3.
 */

public class AutoFocusCameraFragment extends Fragment implements View.OnClickListener {
    private MyCamera    camera;
    private SurfaceView surfaceView;
    private static final String CAMERA_LOG_TAG = "AutoFocusCameraLog";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        camera = new MyCamera(getActivity());
    }


    private void initView() {
        View        view        = getView();
        ImageButton imageButton = view.findViewById(R.id.cameraButton);
        imageButton.setOnClickListener(this);
        surfaceView = view.findViewById(R.id.cameraSurface);
        surfaceView.setFocusable(true);
        surfaceView.setOnClickListener(this);
        surfaceView.setBackgroundColor(TRIM_MEMORY_BACKGROUND);
        camera.initCamera(surfaceView);
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cameraButton:
                camera.takePicture();
                break;
            case R.id.cameraSurface:
                camera.autoFocus();
                break;
            default:
                break;
        }
    }

}
