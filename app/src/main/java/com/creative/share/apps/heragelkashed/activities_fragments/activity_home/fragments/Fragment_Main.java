package com.creative.share.apps.heragelkashed.activities_fragments.activity_home.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_slider_details.SliderDetailsActivity;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_sub_category.SubCategoryActivity;
import com.creative.share.apps.heragelkashed.adapter.HomeMainCategoryAdapter;
import com.creative.share.apps.heragelkashed.databinding.FragmentMainBinding;
import com.creative.share.apps.heragelkashed.models.MainCategoryDataModel;
import com.creative.share.apps.heragelkashed.models.SliderModelData;
import com.creative.share.apps.heragelkashed.models.UserModel;
import com.creative.share.apps.heragelkashed.preferences.Preferences;
import com.creative.share.apps.heragelkashed.remote.Api;
import com.creative.share.apps.heragelkashed.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Main extends Fragment {
    private HomeActivity activity;
    private FragmentMainBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private HomeMainCategoryAdapter adapter;
    private List<MainCategoryDataModel.MainCategoryModel> mainCategoryModelList;
    private List<SliderModelData.SliderModel> sliderModelList;
    public static Fragment_Main newInstance() {
        return new Fragment_Main();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);


        initView();
        return binding.getRoot();
    }

    private void initView() {
        sliderModelList = new ArrayList<>();
        mainCategoryModelList = new ArrayList<>();
        mainCategoryModelList.add(null);

        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.recView.setNestedScrollingEnabled(true);
        binding.recView.setLayoutManager(new GridLayoutManager(activity, 1));
        adapter = new HomeMainCategoryAdapter(activity, this, mainCategoryModelList);
        binding.recView.setAdapter(adapter);
        getSliderData();



    }

    private void getSliderData()
    {
        int user_id;
        if (userModel==null)
        {
            user_id =0;
        }else
            {
                user_id = userModel.getId();
            }

        try {

            Api.getService(Tags.base_url)
                    .getSliderData(user_id)
                    .enqueue(new Callback<SliderModelData>() {
                        @Override
                        public void onResponse(Call<SliderModelData> call, Response<SliderModelData> response) {
                            if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                            {
                              sliderModelList.clear();
                              sliderModelList.addAll(response.body().getData());
                              if (adapter!=null)
                              {
                                  adapter.setSliderData(sliderModelList);
                              }

                            }else
                            {

                                if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SliderModelData> call, Throwable t) {
                            try {

                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(activity,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(activity,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){


        }

    }

    public void getMainCategory(List<MainCategoryDataModel.MainCategoryModel> mainCategoryModelList,int code) {

        if (code==200)
        {
            binding.progBar.setVisibility(View.GONE);
            this.mainCategoryModelList.addAll(mainCategoryModelList);

            if (this.mainCategoryModelList.size() > 0) {
                binding.tvNoAds.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            } else {
                binding.tvNoAds.setVisibility(View.VISIBLE);

            }
        }else
            {
                binding.progBar.setVisibility(View.GONE);

            }

    }

    public void setSliderItem(SliderModelData.SliderModel sliderModel) {
        Log.e("slider_id",sliderModel.getId()+"__");
        Intent intent = new Intent(activity, SliderDetailsActivity.class);
        intent.putExtra("ad_id",sliderModel.getId());
        startActivity(intent);
    }

    public void setSubCategoryItem(MainCategoryDataModel.SubCategoryModel subCategoryModel) {
        Intent intent = new Intent(activity, SubCategoryActivity.class);
        intent.putExtra("sub_id",subCategoryModel.getId());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter!=null)
        {
            adapter.stopTimer();
        }
    }



}
