package com.creative.share.apps.heragelkashed.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_add_ads.AddAdsActivity;
import com.creative.share.apps.heragelkashed.databinding.ImageAdRowBinding;
import com.creative.share.apps.heragelkashed.databinding.VideoAdRowBinding;
import com.creative.share.apps.heragelkashed.models.AdImageVideoModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAddAdAdapter extends PagerAdapter {

    private Context context;
    private List<AdImageVideoModel> sliderModelList;
    private AddAdsActivity activity;
    public SliderAddAdAdapter(Context context, List<AdImageVideoModel> sliderModelList) {
        this.context = context;
        this.sliderModelList = sliderModelList;
        this.activity = (AddAdsActivity) context;
    }

    @Override
    public int getCount() {
        return sliderModelList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {


        AdImageVideoModel sliderModel = sliderModelList.get(position);

        if (sliderModel.isVideo())
        {
            VideoAdRowBinding binding =DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.video_ad_row,container,false);

            binding.imagePlay.setOnClickListener(view ->
                activity.setVideoUri(Uri.parse(sliderModel.getUri()))

            );

            container.addView(binding.getRoot());
            return binding.getRoot();

        }else {
            ImageAdRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.image_ad_row,container,false);


            Picasso.with(context).load(Uri.parse(sliderModel.getUri())).fit().into(binding.image);


            container.addView(binding.getRoot());
            return binding.getRoot();
        }

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


}
