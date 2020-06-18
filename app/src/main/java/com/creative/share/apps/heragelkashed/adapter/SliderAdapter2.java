package com.creative.share.apps.heragelkashed.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.databinding.SliderBinding;
import com.creative.share.apps.heragelkashed.databinding.VideoRowBinding;
import com.creative.share.apps.heragelkashed.models.SliderImageVideoModel;
import com.creative.share.apps.heragelkashed.tags.Tags;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapter2 extends PagerAdapter {

    private Context context;
    private List<SliderImageVideoModel> sliderModelList;
    private VideoRowBinding binding;

    public SliderAdapter2(Context context, List<SliderImageVideoModel> sliderModelList) {
        this.context = context;
        this.sliderModelList = sliderModelList;
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


        SliderImageVideoModel sliderModel = sliderModelList.get(position);

        if (sliderModel.isVideo())
        {
            binding =DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.video_row,container,false);
            binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

            binding.videoView.setOnPreparedListener(mediaPlayer -> {
                binding.progBar.setVisibility(View.GONE);
                mediaPlayer.setOnInfoListener((mediaPlayer1, i, i1) -> {
                    if (i == MediaPlayer.MEDIA_INFO_BUFFERING_START)
                    {
                        binding.progBar.setVisibility(View.VISIBLE);

                    }else if (i == MediaPlayer.MEDIA_INFO_BUFFERING_END)
                    {
                        binding.progBar.setVisibility(View.GONE);

                    }
                    return false;
                });

                mediaPlayer.setOnCompletionListener(mediaPlayer12 -> binding.imgPlay.setVisibility(View.VISIBLE));

            });

            binding.videoView.setOnClickListener(view -> {
                if (binding.videoView.isPlaying())
                {
                    binding.imgPlay.setVisibility(View.VISIBLE);
                    binding.videoView.stopPlayback();
                }
            });


            binding.imgPlay.setOnClickListener(view -> {
                binding.imgPlay.setVisibility(View.GONE);
                binding.progBar.setVisibility(View.VISIBLE);
                binding.videoView.setVideoURI(Uri.parse(Tags.IMAGE_URL_VIDEOS+sliderModel.getEndPoint()));
                binding.videoView.start();

            });

            container.addView(binding.getRoot());
            return binding.getRoot();

        }else {
            SliderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.slider,container,false);
            binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL_ADS+sliderModel.getEndPoint())).into(binding.image, new Callback() {
                @Override
                public void onSuccess() {
                    binding.progBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });

            container.addView(binding.getRoot());
            return binding.getRoot();
        }

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (binding!=null)
        {
            binding.imgPlay.setVisibility(View.VISIBLE);
            binding.videoView.stopPlayback();

        }
        container.removeView((View) object);
    }


}
