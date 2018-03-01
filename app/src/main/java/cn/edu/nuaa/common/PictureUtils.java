package cn.edu.nuaa.common;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import java.io.IOException;

/**
 * Created by Meteor on 2018/2/8.
 */

public class PictureUtils {

    public static BitmapDrawable getScaledDrawable(Activity activity, String path) {
        Display               display   = activity.getWindowManager().getDefaultDisplay();
        float                 desWidth  = display.getWidth();
        float                 desHeight = display.getHeight();
        BitmapFactory.Options options   = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        float srcHeight = options.outHeight;
        float srcWidth  = options.outWidth;
        options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        if (srcHeight > desHeight || srcWidth > desWidth) {
            if (srcHeight > desHeight) {
                options.inSampleSize = Math.round(srcHeight / desHeight);
            } else {
                options.inSampleSize = Math.round(srcWidth / desWidth);
            }
        }
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return new BitmapDrawable(activity.getResources(), bitmap);
    }

    public static void freeBitmap(@NonNull ImageView imageView) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        if (bitmapDrawable != null) {
            bitmapDrawable.getBitmap().recycle();
            imageView.setImageDrawable(null);
        }
    }
    /*private static final String LOG_TAG = PictureUtils.class.getName();
    private String                                       fileName;
    private net.coobird.thumbnailator.Thumbnails.Builder builder;

    public PictureUtils(String fileName) {
        this.fileName = fileName;
        BitmapFactory.Options options=new BitmapFactory.Options();
        BitmapFactory.decodeFile(fileName, options);
        builder = Thumbnails.of(fileName).size(options.outWidth,options.outHeight);
    }

    public void scaleByRatio(double ratio) {
        builder = builder.scale(ratio);
    }

    public void scaleBySize(int width, int height) {
        builder = builder.size(width, height);
    }

    public void rotate(double degree) {
        builder = builder.scale(1).rotate(degree);
    }

    public void cut(CUT_START_POINT start, int width, int height) {
        Positions pos;
        switch (start) {
            case CENTER:
                pos = Positions.CENTER;
                break;
            case LEFT_TOP:
                pos = Positions.TOP_LEFT;
                break;
            case LEFT_BOTTOM:
                pos = Positions.BOTTOM_LEFT;
                break;
            case RIGHT_TOP:
                pos = Positions.TOP_RIGHT;
                break;
            case RIGHT_BOTTOM:
                pos = Positions.BOTTOM_RIGHT;
                break;
            default:
                pos = Positions.CENTER;
                break;
        }
        builder = builder.sourceRegion(pos, width, height);
    }

    public void cut(int startX, int startY, int width, int height) {
        builder = builder.sourceRegion(startX, startY, width, height).scale(1);
    }

    public void generateFile() {
        try {
            builder.toFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "generate file error,message:" + e.getMessage());
        }
    }

    enum CUT_START_POINT {
        LEFT_TOP, RIGHT_TOP, LEFT_BOTTOM, RIGHT_BOTTOM, CENTER
    }*//*private static final String LOG_TAG = PictureUtils.class.getName();
    private String                                       fileName;
    private net.coobird.thumbnailator.Thumbnails.Builder builder;

    public PictureUtils(String fileName) {
        this.fileName = fileName;
        BitmapFactory.Options options=new BitmapFactory.Options();
        BitmapFactory.decodeFile(fileName, options);
        builder = Thumbnails.of(fileName).size(options.outWidth,options.outHeight);
    }

    public void scaleByRatio(double ratio) {
        builder = builder.scale(ratio);
    }

    public void scaleBySize(int width, int height) {
        builder = builder.size(width, height);
    }

    public void rotate(double degree) {
        builder = builder.scale(1).rotate(degree);
    }

    public void cut(CUT_START_POINT start, int width, int height) {
        Positions pos;
        switch (start) {
            case CENTER:
                pos = Positions.CENTER;
                break;
            case LEFT_TOP:
                pos = Positions.TOP_LEFT;
                break;
            case LEFT_BOTTOM:
                pos = Positions.BOTTOM_LEFT;
                break;
            case RIGHT_TOP:
                pos = Positions.TOP_RIGHT;
                break;
            case RIGHT_BOTTOM:
                pos = Positions.BOTTOM_RIGHT;
                break;
            default:
                pos = Positions.CENTER;
                break;
        }
        builder = builder.sourceRegion(pos, width, height);
    }

    public void cut(int startX, int startY, int width, int height) {
        builder = builder.sourceRegion(startX, startY, width, height).scale(1);
    }

    public void generateFile() {
        try {
            builder.toFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "generate file error,message:" + e.getMessage());
        }
    }

    enum CUT_START_POINT {
        LEFT_TOP, RIGHT_TOP, LEFT_BOTTOM, RIGHT_BOTTOM, CENTER
    }*/
}
