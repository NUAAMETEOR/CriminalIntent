package cn.edu.nuaa.criminalintent;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.edu.nuaa.common.PictureUtils;

/**
 * Created by Meteor on 2018/3/1.
 */

public class ImageFragment extends DialogFragment {
    public static ImageFragment createInstance(@NonNull String photoFilePath) {
        ImageFragment imageFragment = new ImageFragment();
        Bundle        bundle        = new Bundle();
        bundle.putString(CrimeFragment.PHOTO_PATH, photoFilePath);
        imageFragment.setArguments(bundle);
        imageFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return imageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ImageView      imageView      = new ImageView(getContext());
        String         filePath       = getArguments().getString(CrimeFragment.PHOTO_PATH);
        BitmapDrawable bitmapDrawable = PictureUtils.getScaledDrawable(getActivity(), filePath);
        imageView.setImageDrawable(bitmapDrawable);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageFragment.this.dismiss();
            }
        });
        return imageView;
    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.freeBitmap((ImageView) getView());
    }
}
