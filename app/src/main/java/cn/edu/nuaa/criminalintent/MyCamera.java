package cn.edu.nuaa.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaActionSound;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by Meteor on 2018/2/3.
 */
@SuppressWarnings("deprecation")
public class MyCamera implements SurfaceHolder.Callback {
    private static final String LOG_TAG = MyCamera.class.getName();
    private Camera.ShutterCallback shutterCallback = new Shutter();
    private Camera.PictureCallback rawPicCallback  = new RawPictureCallback();
    private Camera.PictureCallback jpegPicCallback = new RawPictureCallback();
    private MediaActionSound mediaActionSound=new MediaActionSound();
    private Camera camera = null;
    private Activity activity;
    private int cameraIndex = 0;

    public MyCamera(Activity activity) {
        this.activity = activity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            camera = Camera.open(cameraIndex);
        } else {
            camera = Camera.open();
        }
    }

    private Camera.Size getBestSupportSize(int width, int height) {
        List<Camera.Size> allSize = camera.getParameters().getSupportedPreviewSizes();//getSupportedXX这个方法要熟记
        int               area    = 0;
        Camera.Size       size    = camera.new Size(400, 300);
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

    private void setAutoFocusMode() {
        Camera.Parameters parameters = camera.getParameters();
        List<String>      focusModes = parameters.getSupportedFocusModes();
        for (String s : focusModes) {
            Log.d(LOG_TAG, s + " supported");
        }
        if (focusModes.contains("continuous-picture")) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 1连续对焦
            Log.i(LOG_TAG, "设置自动对焦模式为 FOCUS_MODE_CONTINUOUS_PICTURE");
        } else {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            Log.i(LOG_TAG, "设置自动对焦模式为 FOCUS_MODE_AUTO");
        }
        camera.setParameters(parameters);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void initCamera(@NonNull SurfaceView surfaceView) {
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//设置闪光灯
        parameters.setPictureFormat(PixelFormat.JPEG);//设置图像格式
camera.setAutoFocusMoveCallback(new Camera.AutoFocusMoveCallback() {
    @Override
    public void onAutoFocusMoving(boolean b, Camera camera) {
        if (b) {
            Log.d(LOG_TAG, "start continuous auto focus");
        } else {
            mediaActionSound.play(MediaActionSound.FOCUS_COMPLETE);
            Log.d(LOG_TAG, "continuous auto focus complete");
        }
    }
});
        camera.setParameters(parameters);
        setCameraDisplayOrientation();

        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.setKeepScreenOn(true);
    }

    private void startPreview() {
        mediaActionSound.load(MediaActionSound.FOCUS_COMPLETE);
        camera.cancelAutoFocus();
        camera.startPreview();
        setAutoFocusMode();
    }

    private void stopPreview() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
            mediaActionSound.release();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Camera.Size       bestSize   = getBestSupportSize(i1, i2);
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(bestSize.width, bestSize.height);
        camera.setParameters(parameters);
        startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        stopPreview();
    }

    public void takePicture() {
        camera.takePicture(shutterCallback, rawPicCallback, jpegPicCallback);
    }

    /**
     * Continuous auto focus mode intended for video recording.
     * The camera continuously tries to focus.
     * This is the best choice for video recording because the focus changes smoothly .
     * Applications still can call takePicture(Camera.ShutterCallback,
     * Camera.PictureCallback, Camera.PictureCallback) in this mode
     * but the subject may not be in focus.
     * Auto focus starts when the parameter is set.
     * <p>
     * Since API level 14, applications can call autoFocus(AutoFocusCallback) in this mode
     * .
     * The focus callback will immediately return with a boolean
     * that indicates whether the focus is sharp or not.
     * The focus position is locked after autoFocus call.
     * If applications want to resume the continuous focus,
     * cancelAutoFocus must be called.
     * Restarting the preview will not resume the continuous autofocus.
     * To stop continuous focus, applications should change the focus mode to other modes.
     * 此方法在手动对焦时调用
     */
    public void autoFocus() {
        Camera.Parameters parameters = camera.getParameters();
        //将对焦模式从FOCUS_MODE_CONTINUOUS_PICTURE变为FOCUS_MODE_AUTO
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        camera.setParameters(parameters);
        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean b, Camera camera) {
                if (b) {
                    Log.i(LOG_TAG, "自动对焦成功。autoFocus()成功后，相机的焦点会被锁定");
                    mediaActionSound.play(MediaActionSound.FOCUS_COMPLETE);
                    camera.cancelAutoFocus();//调用cancelAutoFocus()取消相机的焦点锁定，否则FOCUS_MODE_CONTINUOUS_PICTURE不生效
                    setAutoFocusMode();//对焦模式重新改为不用手点的自动对焦
                } else {
                    Log.i(LOG_TAG, "自动对焦失败");
                }
            }
        });
    }

    /**
     * 屏幕朝向角度可能有0，90，180，270四个值。
     * 如果手机右边朝上，角度是90°，否则是270°
     */
    public void setCameraDisplayOrientation() {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraIndex, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees  = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;   // compensate the mirror
        } else {
            // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    public int getCameraIndex() {
        return cameraIndex;
    }

    public void setCameraIndex(int cameraIndex) {
        this.cameraIndex = cameraIndex;
    }

    private class Shutter implements Camera.ShutterCallback {

        @Override
        public void onShutter() {
            Log.i(LOG_TAG, "shutter callback");
        }
    }

    private class RawPictureCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            Log.i(LOG_TAG, "raw picture callback");
        }
    }

    private class JpegPictureCallback implements Camera.PictureCallback {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            Log.i(LOG_TAG, "jpeg picture callback");
            camera.stopPreview();
        }
    }
}
