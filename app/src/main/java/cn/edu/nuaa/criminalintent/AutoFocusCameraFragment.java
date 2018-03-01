package cn.edu.nuaa.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import static android.content.ComponentCallbacks2.TRIM_MEMORY_BACKGROUND;

/**
 * Created by Meteor on 2018/2/3.
 */

public class AutoFocusCameraFragment extends Fragment implements View.OnClickListener, Camera.ShutterCallback, Camera.PictureCallback {
    private static final String CAMERA_LOG_TAG = "AutoFocusCameraLog";
    public static final String PHOTO_FILE_NAME = "PhotoPath";
    private MyCamera    camera;
    private SurfaceView surfaceView;
    private ProgressBar progressBar;
    private static final String sdcardPath = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        camera = new MyCamera(getActivity());
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

    private void initView() {
        View view = getView();
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        ImageButton imageButton = view.findViewById(R.id.cameraButton);
        imageButton.setOnClickListener(this);
        surfaceView = view.findViewById(R.id.cameraSurface);
        surfaceView.setFocusable(true);
        surfaceView.setOnClickListener(this);
        surfaceView.setBackgroundColor(TRIM_MEMORY_BACKGROUND);
        camera.initCamera(surfaceView, this, null, this);
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

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        String               fileName = UUID.randomUUID() + ".jpg";
        String fullName=sdcardPath+"/DCIM/Camera/"+fileName;
        boolean              succ     = false;
        BufferedOutputStream bos=null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(fullName));
            bos.write(bytes);
            succ = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (succ) {
            Log.d(CAMERA_LOG_TAG, "save JPEG to " + fullName);
            try {
                File file =new File(fullName);
                MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), fullName, fileName, null); //保存图片后发送广播通知更新数据库 Uri uri = Uri.fromFile(file); context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                Uri uri = Uri.fromFile(file);
                getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                Intent intent = new Intent();
                intent.putExtra(PHOTO_FILE_NAME, fullName);
                getActivity().setResult(Activity.RESULT_OK, intent);
            } catch (FileNotFoundException e) {
                getActivity().setResult(Activity.RESULT_CANCELED);
                e.printStackTrace();
            }
        }
        getActivity().finish();
    }

    @Override
    public void onShutter() {
        progressBar.setVisibility(View.VISIBLE);
    }
}
