package com.creative.share.apps.heragelkashed.activities_fragments.activity_sub_sub_categories_details;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_slider_details.SliderDetailsActivity;
import com.creative.share.apps.heragelkashed.adapter.AdsAdapter;
import com.creative.share.apps.heragelkashed.databinding.FragmentSubSubCategoryDetailsBinding;
import com.creative.share.apps.heragelkashed.models.AdDataModel;
import com.creative.share.apps.heragelkashed.models.AdModel;
import com.creative.share.apps.heragelkashed.models.UserModel;
import com.creative.share.apps.heragelkashed.preferences.Preferences;
import com.creative.share.apps.heragelkashed.remote.Api;
import com.creative.share.apps.heragelkashed.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentSubSubCategoryDetails extends Fragment {

    private static final String DATA1 = "sub_sub_id";
    private static final String DATA2 = "type_id";


    private FragmentSubSubCategoryDetailsBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private int sub_sub_id = 1;
    private int type_id = 1;
    private int type = 1;

    private SubSubCategoryDetailsActivity activity;
    private int current_page = 1;
    private boolean isLoading = false;
    private List<AdModel> adModelList;
    private AdsAdapter adapter;

    public static FragmentSubSubCategoryDetails newInstance(int sub_sub_id, int type_id) {
        FragmentSubSubCategoryDetails fragment = new FragmentSubSubCategoryDetails();

        Bundle bundle = new Bundle();
        bundle.putInt(DATA1, sub_sub_id);
        bundle.putInt(DATA2, type_id);
        fragment.setArguments(bundle);

        return fragment;


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sub_sub_category_details, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        adModelList = new ArrayList<>();

        activity = (SubSubCategoryDetailsActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        adapter = new AdsAdapter(activity, adModelList,this);

        Bundle bundle = getArguments();
        if (bundle != null) {
            sub_sub_id = bundle.getInt(DATA1);
            type_id = bundle.getInt(DATA2);




        }

        updateManager();
        binding.recView.setAdapter(adapter);

        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0)
                {
                    int total = binding.recView.getAdapter().getItemCount();

                    int lastVisibleItem=0;
                    if (binding.recView.getLayoutManager() instanceof LinearLayoutManager)
                    {
                        lastVisibleItem = ((LinearLayoutManager)binding.recView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    }else if (binding.recView.getLayoutManager() instanceof GridLayoutManager)
                    {
                        lastVisibleItem = ((GridLayoutManager)binding.recView.getLayoutManager()).findLastCompletelyVisibleItemPosition();

                    }

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
        getData();


    }

    public void updateManager()
    {
        if (type == 1) {

            binding.recView.setLayoutManager(new GridLayoutManager(activity, 2));
            adapter.updateType(1);
            type = 2;

        } else {
            binding.recView.setLayoutManager(new LinearLayoutManager(activity));
            adapter.updateType(2);
            type = 1;


        }
    }
    private void getData()
    {
        try {

            int user_id;
            if (userModel == null) {
                user_id = 0;
            } else {
                user_id = userModel.getId();
            }


            Api.getService(Tags.base_url)
                    .getAdsByType(sub_sub_id, type_id, user_id, 0)
                    .enqueue(new Callback<AdDataModel>() {
                        @Override
                        public void onResponse(Call<AdDataModel> call, Response<AdDataModel> response) {
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
                        public void onFailure(Call<AdDataModel> call, Throwable t) {
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

            int user_id;
            if (userModel == null) {
                user_id = 0;
            } else {
                user_id = userModel.getId();
            }


            Api.getService(Tags.base_url)
                    .getAdsByType(sub_sub_id, type_id, user_id, page)
                    .enqueue(new Callback<AdDataModel>() {
                        @Override
                        public void onResponse(Call<AdDataModel> call, Response<AdDataModel> response) {
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
                        public void onFailure(Call<AdDataModel> call, Throwable t) {
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

    public int getStyle()
    {
        return this.type;
    }


    public List<AdModel> getAdModelList()
    {
        return this.adModelList;
    }

    public void setItemData(AdModel adModel) {
        Intent intent = new Intent(activity, SliderDetailsActivity.class);
        intent.putExtra("ad_id",adModel.getId());
        startActivity(intent);
    }
}
