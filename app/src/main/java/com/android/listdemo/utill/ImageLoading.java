package com.android.listdemo.utill;

import android.content.Context;

import com.android.listdemo.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


/**
 * Created by admin on 03/10/16.
 */

public class ImageLoading {

    public static DisplayImageOptions getDisplayImageOption(int cornerRedius) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(true)
                .cacheOnDisc(true)
                .resetViewBeforeLoading(true)
                .displayer(new RoundedBitmapDisplayer(cornerRedius))
                .showImageForEmptyUri(R.drawable.default_image)
                .showImageOnFail(R.drawable.default_image)
                .showImageOnLoading(R.drawable.default_image).build();

        return options;
    }

    public static ImageLoader initImageLoader(Context context, ImageLoader imageLoader) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app\

        imageLoader.getInstance().init(config.build());
        return imageLoader;
    }
}
