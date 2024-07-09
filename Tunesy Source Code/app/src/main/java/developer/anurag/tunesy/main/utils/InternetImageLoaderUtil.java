package developer.anurag.tunesy.main.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

public class InternetImageLoaderUtil {
    public static void loadImage(Context context,ImageView imageView, String imageUri,int width){
        Glide.with(context).load(imageUri).apply(new RequestOptions().override(width)).centerCrop().into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                imageView.setImageDrawable(drawable);
                Log.i("ImageInfo","Image width:- "+drawable.getIntrinsicWidth());
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }
}
