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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_slider_details.SliderDetailsActivity;
import com.creative.share.apps.heragelkashed.adapter.MyAdsAdapter;
import com.creative.share.apps.heragelkashed.databinding.FragmentMyAdversimentBinding;
import com.creative.share.apps.heragelkashed.models.AdModel;
import com.creative.share.apps.heragelkashed.models.MyAdsDataModel;
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

public class Fragment_My_Adversiment extends Fragment {
    private HomeActivity activity;
    private FragmentMyAdversimentBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private boolean isLoading = false;
    private int current_page = 1;
    private List<AdModel> adModelList;
    private MyAdsAdapter adapter;
    public static Fragment_My_Adversiment newInstance() {
        return new Fragment_My_Adversiment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_adversiment, container, false);


        initView();
        return binding.getRoot();
    }

    private void initView() {

        adModelList = new ArrayList<>();

        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        binding.setUserModel(userModel);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new MyAdsAdapter(activity,adModelList,this);
        binding.recView.setAdapter(adapter);

        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0)
                {
                    int total = binding.recView.getAdapter().getItemCount();

                    int lastVisibleItem = ((LinearLayoutManager)binding.recView.getLayoutManager()).findLastCompletelyVisibleItemPosition();


                    if (total>6&&(total-lastVisibleItem)==2&&!isLoading)
                    {
                        isLoading = true;
                        int page = current_page+1;
                        adModelList.add(null);
                        adapter.notifyDataSetChanged();
                        loadMore(page);
                    }
                }
            }
        });

        getMyAds();

    }

    public void getMyAds() {

        try {
            userModel = preferences.getUserData(activity);
            binding.setUserModel(userModel);

            Api.getService(Tags.base_url)
                    .getMyAds(userModel.getId(),1)
                    .enqueue(new Callback<MyAdsDataModel>() {
                        @Override
                        public void onResponse(Call<MyAdsDataModel> call, Response<MyAdsDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                adModelList.clear();
                                adModelList.addAll(response.body().getData());
                                if (adModelList.size() > 0) {
                                    adapter.notifyDataSetChanged();
                                    binding.tvNoAds.setVisibility(View.GONE);
                                } else {
                                    binding.tvNoAds.setVisibility(View.VISIBLE);

                                }
                            } else {
                                if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyAdsDataModel> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);

                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    private void loadMore(int page)
    {
        try {



            Api.getService(Tags.base_url)
                    .getMyAds(userModel.getId(), page)
                    .enqueue(new Callback<MyAdsDataModel>() {
                        @Override
                        public void onResponse(Call<MyAdsDataModel> call, Response<MyAdsDataModel> response) {
                            isLoading = false;
                            adModelList.remove(adModelList.size()-1);
                            adapter.notifyItemRemoved(adModelList.size()-1);

                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                adModelList.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();
                                if (response.body().getData().size()>0)
                                {
                                    current_page = response.body().getCurrent_page();

                                }
                            } else {
                                if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyAdsDataModel> call, Throwable t) {
                            try {
                                if (adModelList.get(adModelList.size()-1)==null)
                                {
                                    isLoading = false;
                                    adModelList.remove(adModelList.size()-1);
                                    adapter.notifyItemRemoved(adModelList.size()-1);

                                }
                                binding.progBar.setVisibility(View.GONE);

                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }


    public void setItemData(AdModel adModel) {
        Intent intent = new Intent(activity, SliderDetailsActivity.class);
        intent.putExtra("ad_id",adModel.getId());
        startActivity(intent);
    }
}
