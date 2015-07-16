package com.lsyiverson.bigben.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.lsyiverson.bigben.R;
import com.lsyiverson.bigben.model.BeaconInfo;
import com.lsyiverson.bigben.utils.Constants;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import butterknife.Bind;

public class AdContentActivity extends BaseActivity {
    @Bind(R.id.ad_image)
    ImageView image;

    @Bind(R.id.ad_title)
    TextView title;

    @Bind(R.id.ad_content)
    TextView content;

    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_content);

        imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(this);
        builder.diskCache(new UnlimitedDiskCache(getCacheDir()));
        builder.diskCacheFileCount(10);
        imageLoader.init(builder.build());

        BeaconInfo beaconInfo = (BeaconInfo) getIntent().getSerializableExtra(Constants.BEACON_INFO);
        populateAdContent(beaconInfo);
    }

    private void populateAdContent(BeaconInfo beaconInfo) {
        DisplayImageOptions.Builder optionBuilder = new DisplayImageOptions.Builder();
        optionBuilder.cacheOnDisk(true);
        imageLoader.displayImage(beaconInfo.getImageUrl(), image, optionBuilder.build());
        title.setText(beaconInfo.getTitle());
        content.setText(beaconInfo.getContent());
    }
}
