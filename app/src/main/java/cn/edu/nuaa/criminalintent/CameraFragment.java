package cn.edu.nuaa.criminalintent;

import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.List;

/**
 * Created by Meteor on 2018/1/31.
 */

public class CameraFragment extends Fragment {
    private Camera      camera;
    private SurfaceView surfaceView;
    private static final String CAMERA_LOG_TAG = "camera-log";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View        view        = inflater.inflate(R.layout.camera, container, false);
        ImageButton imageButton = view.findViewById(R.id.cameraButton);
        surfaceView = view.findViewById(R.id.cameraSurface);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (camera != null) {
                    try {
                        camera.setPreviewDisplay(holder);
                    } catch (IOException e) {
                        Log.e(CAMERA_LOG_TAG, "camera set preview display failed");
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                /**
                 * i format
                 * i1 width
                 * i2 height
                 */
                if (camera != null) {
                    Camera.Parameters cameraParameteor = camera.getParameters();
                    Camera.Size bestSize = getBestSupportSize(i1, i2);
                    cameraParameteor.setPreviewSize(bestSize.width, bestSize.height);
                    camera.setParameters(cameraParameteor);
                    camera.startPreview();
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                if (camera != null) {
                    camera.stopPreview();
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            camera = Camera.open(0);
        } else {
            camera = Camera.open();
        }
    }

    private Camera.Size getBestSupportSize(int width, int height) {
        List<Camera.Size> allSize = camera.getParameters().getSupportedPreviewSizes();
        int               area    = 0;
        Camera.Size       size    = null;
        for (Camera.Size s :
                allSize) {
            if (s.width > width || s.height > height) {
                continue;
            } else if (s.width == width && s.height == height) {
                return s;
            } else {
                if (area == 0) {
                    area = s.height * s.width;
                    size = s;
                } else {
                    int temp = s.width * s.height;
                    if (area < temp) {
                        area = temp;
                        size = s;
                    }
                }
            }
        }
        return size;
    }

    public void onPause() {
        super.onPause();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}
