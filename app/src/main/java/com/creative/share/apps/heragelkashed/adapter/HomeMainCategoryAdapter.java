package com.creative.share.apps.heragelkashed.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_home.fragments.Fragment_Main;
import com.creative.share.apps.heragelkashed.databinding.HomeMainCategoryRowBinding;
import com.creative.share.apps.heragelkashed.databinding.HomeRecyclerViewHeaderBinding;
import com.creative.share.apps.heragelkashed.models.MainCategoryDataModel;
import com.creative.share.apps.heragelkashed.models.SliderModelData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

public class HomeMainCategoryAdapter extends RecyclerView.Adapter {
    private final int SLIDER = 1;
    private final int DATA = 2;

    private Context context;
    private Fragment_Main fragment_main;
    private List<MainCategoryDataModel.MainCategoryModel> mainCategoryModelList;
    private List<SliderModelData.SliderModel> sliderModelList;
    private HomeActivity activity;
    private String lang;
    private SliderHolder sliderHolder;
    private Timer timer;
    private TimerTask timerTask;

    public HomeMainCategoryAdapter(Context context, Fragment_Main fragment_main, List<MainCategoryDataModel.MainCategoryModel> mainCategoryModelList) {
        this.context = context;
        this.activity = (HomeActivity) context;
        this.fragment_main = fragment_main;
        this.mainCategoryModelList = mainCategoryModelList;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        sliderModelList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == DATA) {
            HomeMainCategoryRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.home_main_category_row, parent, false);
            return new MyHolder(binding);
        } else {
            HomeRecyclerViewHeaderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.home_recycler_view_header, parent, false);
            return new SliderHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            MainCategoryDataModel.MainCategoryModel mainCategoryModel = mainCategoryModelList.get(position);
            GridLayoutManager manager = new GridLayoutManager(context, 2);
            myHolder.binding.recView.setLayoutManager(manager);
            myHolder.binding.recView.setNestedScrollingEnabled(true);
            myHolder.binding.tvTitle.setText(mainCategoryModel.getTitle());
            HomeSubCategoryAdapter adapter = new HomeSubCategoryAdapter(context, fragment_main, mainCategoryModel.getSub_categories());
            myHolder.binding.recView.setAdapter(adapter);

            if (mainCategoryModel.getSub_categories().size() > 0) {
                myHolder.binding.tvNoAds.setVisibility(View.GONE);
            } else {
                myHolder.binding.tvNoAds.setVisibility(View.VISIBLE);

            }
        } else if (holder instanceof SliderHolder) {

            sliderHolder = (SliderHolder) holder;
            sliderHolder.binding.progBarSlider.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


            sliderHolder.binding.tabLayout.setupWithViewPager(sliderHolder.binding.pager);

        }


    }

    @Override
    public int getItemCount() {
        return mainCategoryModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private HomeMainCategoryRowBinding binding;

        public MyHolder(@NonNull HomeMainCategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class SliderHolder extends RecyclerView.ViewHolder {
        private HomeRecyclerViewHeaderBinding binding;

        public SliderHolder(@NonNull HomeRecyclerViewHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setSliderData(List<SliderModelData.SliderModel> sliderModelList) {
        this.sliderModelList.addAll(sliderModelList);
        setUpSlider(sliderModelList);
    }

    private void setUpSlider(List<SliderModelData.SliderModel> sliderModelList) {

        sliderHolder.binding.progBarSlider.setVisibility(View.GONE);
        sliderHolder.binding.pager.setOffscreenPageLimit(sliderModelList.size());
        if (sliderModelList.size() > 0) {
            sliderHolder.binding.flSlider.setVisibility(View.VISIBLE);
            SliderAdapter sliderAdapter = new SliderAdapter(context, sliderModelList, fragment_main);
            sliderHolder.binding.pager.setAdapter(sliderAdapter);

            if (sliderModelList.size() > 1) {
                timer = new Timer();
                timerTask = new MyTimerTask();
                timer.scheduleAtFixedRate(timerTask, 6000, 6000);

            }

        } else {
            sliderHolder.binding.flSlider.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public int getItemViewType(int position) {
        if (mainCategoryModelList.get(position) == null) {
            return SLIDER;
        } else {
            return DATA;
        }
    }


    public class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            activity.runOnUiThread(() -> {
                if (sliderHolder.binding.pager.getCurrentItem() < sliderModelList.size() - 1) {
                    sliderHolder.binding.pager.setCurrentItem(sliderHolder.binding.pager.getCurrentItem() + 1);
                } else {
                    sliderHolder.binding.pager.setCurrentItem(0);

                }
            });
        }


    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }

        if (timerTask != null) {
            timerTask.cancel();
        }
    }
}
