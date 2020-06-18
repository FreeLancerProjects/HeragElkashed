package com.creative.share.apps.heragelkashed.activities_fragments.activity_home.fragments.fragment_company;

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
import com.creative.share.apps.heragelkashed.activities_fragments.activity_companies.CompanyActivity;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.heragelkashed.adapter.CompanyFeatureAdapter;
import com.creative.share.apps.heragelkashed.databinding.FragmentCompaniesFeatureBinding;
import com.creative.share.apps.heragelkashed.models.SubCategoryDataModel;
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

public class Fragment_Companies_Feature extends Fragment {
    private HomeActivity activity;
    private FragmentCompaniesFeatureBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private List<SubCategoryDataModel.SubCategoryModel> subCategoryModelList;
    private CompanyFeatureAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_companies_feature, container, false);


        initView();
        return binding.getRoot();
    }

    private void initView() {
        subCategoryModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.recView.setLayoutManager(new GridLayoutManager(activity,3));
        adapter = new CompanyFeatureAdapter(activity,subCategoryModelList,this);
        binding.recView.setAdapter(adapter);

        getSubCategories();
    }

    private void getSubCategories() {

        try {

            Api.getService(Tags.base_url)
                    .getAllSubCategory()
                    .enqueue(new Callback<SubCategoryDataModel>() {
                        @Override
                        public void onResponse(Call<SubCategoryDataModel> call, Response<SubCategoryDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                            {
                                subCategoryModelList.clear();
                                subCategoryModelList.addAll(response.body().getData());
                                if (subCategoryModelList.size()>0)
                                {
                                    adapter.notifyDataSetChanged();
                                    binding.tvNoCategory.setVisibility(View.GONE);
                                }else
                                    {
                                        binding.tvNoCategory.setVisibility(View.VISIBLE);

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
                        public void onFailure(Call<SubCategoryDataModel> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);
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
            binding.progBar.setVisibility(View.GONE);
        }
    }


    public static Fragment_Companies_Feature newInstance() {
        return new Fragment_Companies_Feature();
    }

    public void setItem(SubCategoryDataModel.SubCategoryModel subCategoryModel) {
        Intent intent = new Intent(activity, CompanyActivity.class);
        intent.putExtra("sub_cat_id",subCategoryModel.getId());
        startActivity(intent);
    }
}
