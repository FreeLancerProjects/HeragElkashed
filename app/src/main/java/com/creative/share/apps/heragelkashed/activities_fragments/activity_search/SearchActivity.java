package com.creative.share.apps.heragelkashed.activities_fragments.activity_search;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_sub_sub_categories_details.SubSubCategoryDetailsActivity;
import com.creative.share.apps.heragelkashed.adapter.SearchAdapter;
import com.creative.share.apps.heragelkashed.adapter.SpinnerSubCategoryAdapter;
import com.creative.share.apps.heragelkashed.databinding.ActivitySearchBinding;
import com.creative.share.apps.heragelkashed.interfaces.Listeners;
import com.creative.share.apps.heragelkashed.language.LanguageHelper;
import com.creative.share.apps.heragelkashed.models.AdModel;
import com.creative.share.apps.heragelkashed.models.AdTypeDataModel;
import com.creative.share.apps.heragelkashed.models.SearchDataModel;
import com.creative.share.apps.heragelkashed.models.SubCategoryDataModel;
import com.creative.share.apps.heragelkashed.models.UserModel;
import com.creative.share.apps.heragelkashed.preferences.Preferences;
import com.creative.share.apps.heragelkashed.remote.Api;
import com.creative.share.apps.heragelkashed.share.Common;
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

public class SearchActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivitySearchBinding binding;
    private List<AdTypeDataModel.AdTypeModel> adTypeModelList;
    private List<SubCategoryDataModel.SubCategoryModel> subCategoryModelList;
    private String lang;
    private SpinnerSubCategoryAdapter spinnerSubCategoryAdapter;
    private List<AdModel> adModelList;
    private SearchAdapter searchAdapter;
    private int cat_id = 0;
    private String query="";
    private UserModel userModel;
    private Preferences preferences;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        initView();
    }

    private void initView()
    {
        adTypeModelList = new ArrayList<>();
        subCategoryModelList = new ArrayList<>();
        adModelList = new ArrayList<>();

        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);

        searchAdapter = new SearchAdapter(this,adModelList);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter = new SearchAdapter(this,adModelList);
        binding.recView.setAdapter(searchAdapter);


        spinnerSubCategoryAdapter = new SpinnerSubCategoryAdapter(subCategoryModelList,this);
        binding.spinnerCategory.setAdapter(spinnerSubCategoryAdapter);
        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    ((TextView)(adapterView.getChildAt(0))).setTextColor(ContextCompat.getColor(SearchActivity.this,R.color.white));
                }catch (Exception e){}

                cat_id = subCategoryModelList.get(i).getId();
                if (!query.isEmpty())
                {
                    search();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length()>0)
                {
                    query = editable.toString();
                    binding.tvNoSearch.setVisibility(View.GONE);
                    binding.llSearchResult.setVisibility(View.VISIBLE);
                    search();
                }else
                    {
                        query ="";
                        binding.tvNoSearch.setVisibility(View.VISIBLE);
                        binding.llSearchResult.setVisibility(View.GONE);
                        adModelList.clear();
                        searchAdapter.notifyDataSetChanged();


                    }
            }
        });
        getSubCategory();
        getAdType();


    }

    private void search()
    {
        try {
            int user_id;
            if (userModel==null)
            {
                user_id = 0;
            }else
                {
                    user_id = userModel.getId();
                }

            Api.getService(Tags.base_url)
                    .searchByName(query,cat_id,user_id)
                    .enqueue(new Callback<SearchDataModel>() {
                        @Override
                        public void onResponse(Call<SearchDataModel> call, Response<SearchDataModel> response) {
                            if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                            {
                                adModelList.clear();
                                adModelList.addAll(response.body().getData());
                                searchAdapter.notifyDataSetChanged();
                                if (adModelList.size()>0)
                                {
                                    binding.tvNoSearch.setVisibility(View.GONE);
                                }else
                                    {
                                        binding.tvNoSearch.setVisibility(View.VISIBLE);

                                    }

                            }else
                            {
                                if (response.code() == 500) {
                                    Toast.makeText(SearchActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(SearchActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SearchDataModel> call, Throwable t) {
                            try {

                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(SearchActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(SearchActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
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
                            if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                            {
                                adTypeModelList.clear();
                                adTypeModelList.addAll(response.body().getData());
                            }else
                            {
                                if (response.code() == 500) {
                                    Toast.makeText(SearchActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(SearchActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

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

                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(SearchActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(SearchActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){

        }
    }
    private void getSubCategory()
    {
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .getAllSubCategory()
                    .enqueue(new Callback<SubCategoryDataModel>() {
                        @Override
                        public void onResponse(Call<SubCategoryDataModel> call, Response<SubCategoryDataModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                            {
                                subCategoryModelList.clear();
                                subCategoryModelList.add(new SubCategoryDataModel.SubCategoryModel(0,getString(R.string.all_dept)));
                                subCategoryModelList.addAll(response.body().getData());
                                spinnerSubCategoryAdapter.notifyDataSetChanged();
                            }else
                            {
                                if (response.code() == 500) {
                                    Toast.makeText(SearchActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(SearchActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

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
                                dialog.dismiss();
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(SearchActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(SearchActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){
            dialog.dismiss();
        }
    }

    @Override
    public void back() {
        finish();
    }

    public void setAdData(AdModel adModel)
    {

        Intent intent = new Intent(this, SubSubCategoryDetailsActivity.class);
        intent.putExtra("ad_type_list", (Serializable) adTypeModelList);
        intent.putExtra("sub_sub_id",adModel.getCategory_id());
        intent.putExtra("ad_type_id",adModel.getAdv_type_id());
        intent.putExtra("position",0);
        startActivity(intent);
    }
}
