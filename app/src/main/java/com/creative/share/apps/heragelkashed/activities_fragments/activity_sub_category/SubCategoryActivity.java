package com.creative.share.apps.heragelkashed.activities_fragments.activity_sub_category;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_sub_sub_categories_details.SubSubCategoryDetailsActivity;
import com.creative.share.apps.heragelkashed.adapter.AdTypeAdapter;
import com.creative.share.apps.heragelkashed.adapter.SubSubCategoryAdapter;
import com.creative.share.apps.heragelkashed.databinding.ActivitySubCategoryBinding;
import com.creative.share.apps.heragelkashed.interfaces.Listeners;
import com.creative.share.apps.heragelkashed.language.LanguageHelper;
import com.creative.share.apps.heragelkashed.models.AdTypeDataModel;
import com.creative.share.apps.heragelkashed.models.SubSubCategoryModel;
import com.creative.share.apps.heragelkashed.remote.Api;
import com.creative.share.apps.heragelkashed.tags.Tags;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivitySubCategoryBinding binding;
    private List<AdTypeDataModel.AdTypeModel> adTypeModelList;
    private AdTypeAdapter adTypeAdapter;
    private String lang;
    private int sub_id;
    private List<SubSubCategoryModel.SubCategories> subCategoriesList;
    private SubSubCategoryAdapter subSubCategoryAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sub_category);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("sub_id"))
        {
            sub_id = intent.getIntExtra("sub_id",0);
        }
    }

    private void initView() {
        adTypeModelList = new ArrayList<>();
        subCategoriesList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);
        binding.progBarAdType.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.progBarAd.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.recViewAd.setLayoutManager(new GridLayoutManager(this,2));
        binding.recViewAdType.setLayoutManager(new GridLayoutManager(this,3));

        adTypeAdapter = new AdTypeAdapter(this,adTypeModelList);
        binding.recViewAdType.setAdapter(adTypeAdapter);

        subSubCategoryAdapter = new SubSubCategoryAdapter(this,subCategoriesList);
        binding.recViewAd.setAdapter(subSubCategoryAdapter);

        getAdType();
        getSubSubCategories();

    }

    private void getSubSubCategories() {
        try {

            Api.getService(Tags.base_url)
                    .getSubSubCategories(sub_id)
                    .enqueue(new Callback<SubSubCategoryModel>() {
                        @Override
                        public void onResponse(Call<SubSubCategoryModel> call, Response<SubSubCategoryModel> response) {
                            binding.progBarAd.setVisibility(View.GONE);
                            if (response.isSuccessful()&&response.body()!=null&&response.body().getSub_categories()!=null)
                            {
                                subCategoriesList.clear();
                                subCategoriesList.add(null);
                                subCategoriesList.addAll(response.body().getSub_categories());
                                subSubCategoryAdapter.notifyDataSetChanged();
                            }else
                            {
                                if (response.code() == 500) {
                                    Toast.makeText(SubCategoryActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(SubCategoryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SubSubCategoryModel> call, Throwable t) {
                            try {
                                binding.progBarAd.setVisibility(View.GONE);

                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(SubCategoryActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(SubCategoryActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){

        }
    }


    private void getAdType()
    {
        try {

            Api.getService(Tags.base_url)
                    .getAdTypes()
                    .enqueue(new Callback<AdTypeDataModel>() {
                        @Override
                        public void onResponse(Call<AdTypeDataModel> call, Response<AdTypeDataModel> response) {
                            binding.progBarAdType.setVisibility(View.GONE);
                            if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                            {
                                adTypeModelList.clear();
                                adTypeModelList.addAll(response.body().getData());
                                adTypeAdapter.notifyDataSetChanged();
                            }else
                            {
                                if (response.code() == 500) {
                                    Toast.makeText(SubCategoryActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(SubCategoryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AdTypeDataModel> call, Throwable t) {
                            try {
                                binding.progBarAdType.setVisibility(View.GONE);

                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(SubCategoryActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(SubCategoryActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){

        }
    }

    @Override
    public void back() {
        finish();
    }

    public void setItemSubCategory(SubSubCategoryModel.SubCategories subCategories) {
        navigateToSubSubCategoryDetailsActivity(subCategories.getId(),0,0);

    }
    public void setAdTypeData(AdTypeDataModel.AdTypeModel adTypeModel, int adapterPosition) {

        navigateToSubSubCategoryDetailsActivity(0,adTypeModel.getId(),adapterPosition);
    }
    private void navigateToSubSubCategoryDetailsActivity(int sub_sub_id,int type,int pos)
    {
        Intent intent = new Intent(this, SubSubCategoryDetailsActivity.class);
        intent.putExtra("ad_type_list", (Serializable) adTypeModelList);
        intent.putExtra("sub_sub_id",sub_sub_id);
        intent.putExtra("ad_type_id",type);
        intent.putExtra("position",pos);
        startActivity(intent);
    }


    public void setAllDept() {
        Intent intent = new Intent(this, SubSubCategoryDetailsActivity.class);
        intent.putExtra("ad_type_list", (Serializable) adTypeModelList);
        intent.putExtra("sub_sub_id",0);
        intent.putExtra("ad_type_id",0);
        intent.putExtra("position",0);
        startActivity(intent);
    }
}
