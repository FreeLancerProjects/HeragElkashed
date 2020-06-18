package com.creative.share.apps.heragelkashed.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_home.fragments.Fragment_Main;
import com.creative.share.apps.heragelkashed.databinding.SliderHomeBinding;
import com.creative.share.apps.heragelkashed.models.SliderModelData;
import com.creative.share.apps.heragelkashed.tags.Tags;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private List<SliderModelData.SliderModel> sliderModelList;
    private Fragment_Main fragment_main;

    public SliderAdapter(Context context, List<SliderModelData.SliderModel> sliderModelList,Fragment_Main fragment_main) {
        this.context = context;
        this.sliderModelList = sliderModelList;
        this.fragment_main = fragment_main;
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
        SliderHomeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.slider_home,container,false);
        SliderModelData.SliderModel sliderModel = sliderModelList.get(position);
        binding.setSliderModel(sliderModel);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL_ADS+sliderModel.getMain_image())).into(binding.image, new Callback() {
            @Override
            public void onSuccess() {
                binding.progBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });

        binding.getRoot().setOnClickListener(view -> {
            if (fragment_main!=null)
            {
                fragment_main.setSliderItem(sliderModel);

            }
        });
        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
