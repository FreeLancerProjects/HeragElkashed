package com.creative.share.apps.heragelkashed.activities_fragments.activity_companies;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_company_ads.CompanyAdsActivity;
import com.creative.share.apps.heragelkashed.adapter.CompanyAdapter;
import com.creative.share.apps.heragelkashed.databinding.ActivityCompanyBinding;
import com.creative.share.apps.heragelkashed.interfaces.Listeners;
import com.creative.share.apps.heragelkashed.language.LanguageHelper;
import com.creative.share.apps.heragelkashed.models.CompanyDataModel;
import com.creative.share.apps.heragelkashed.models.CompanyModel;
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

public class CompanyActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityCompanyBinding binding;
    private List<CompanyModel> companyModelListMain;
    private String lang;
    private int sub_cat_id = 0;
    private String query="";
    private UserModel userModel;
    private Preferences preferences;
    private CompanyAdapter adapter;
    private int current_page=1;
    private boolean isLoading = false;



    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("sub_cat_id"))
        {
            sub_cat_id = intent.getIntExtra("sub_cat_id",0);

        }
    }

    private void initView()
    {
        companyModelListMain = new ArrayList<>();


        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.recView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new CompanyAdapter(this,companyModelListMain);
        binding.recView.setAdapter(adapter);



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
                    if (!isLoading)
                    {
                        current_page = 1;
                        companyModelListMain.clear();
                        adapter.notifyDataSetChanged();
                        search();
                    }

                }else
                {
                    query ="";
                    binding.tvNoSearch.setVisibility(View.GONE);
                    binding.tvNoCompany.setVisibility(View.GONE);
                    getCompanies();
                    current_page = 1;


                }
            }
        });

        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0)
                {
                    int total_items = adapter.getItemCount();
                    int lastItemPos = ((GridLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();

                    if (total_items>=6&&(lastItemPos==total_items-2)&&!isLoading)
                    {
                        isLoading = true;
                        companyModelListMain.add(null);
                        adapter.notifyItemInserted(companyModelListMain.size()-1);
                        int page = current_page+1;
                        if (query.isEmpty())
                        {
                            loadMoreCompanies(page);
                        }else
                            {
                                loadMoreSearch(page);
                            }
                    }

                }
            }
        });
        getCompanies();


    }

    private void getCompanies() {
        try {

            Api.getService(Tags.base_url)
                    .getCompanies(sub_cat_id,1)
                    .enqueue(new Callback<CompanyDataModel>() {
                        @Override
                        public void onResponse(Call<CompanyDataModel> call, Response<CompanyDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                            {
                                companyModelListMain.clear();
                                companyModelListMain.addAll(response.body().getData());
                                if (companyModelListMain.size()>0)
                                {
                                    adapter.notifyDataSetChanged();
                                    binding.tvNoCompany.setVisibility(View.GONE);
                                }else
                                    {
                                        binding.tvNoCompany.setVisibility(View.VISIBLE);

                                    }
                            }else
                            {
                                if (response.code() == 500) {
                                    Toast.makeText(CompanyActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(CompanyActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CompanyDataModel> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(CompanyActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(CompanyActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){

                            }
                        }
                    });
        }catch (Exception e){
            Log.e("eeee",e.getMessage()+"__");
            binding.progBar.setVisibility(View.GONE);
        }
    }

    private void loadMoreCompanies(int page)
    {
        try {

            Api.getService(Tags.base_url)
                    .getCompanies(sub_cat_id,page)
                    .enqueue(new Callback<CompanyDataModel>() {
                        @Override
                        public void onResponse(Call<CompanyDataModel> call, Response<CompanyDataModel> response) {
                            companyModelListMain.remove(companyModelListMain.size()-1);
                            adapter.notifyItemRemoved(companyModelListMain.size()-1);
                            isLoading = false;

                            if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                            {
                                companyModelListMain.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();
                                current_page = response.body().getCurrent_page();
                            }else
                            {
                                if (response.code() == 500) {
                                    Toast.makeText(CompanyActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(CompanyActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CompanyDataModel> call, Throwable t) {
                            try {
                                if (companyModelListMain.get(companyModelListMain.size()-1)==null)
                                {
                                    companyModelListMain.remove(companyModelListMain.size()-1);
                                    adapter.notifyItemRemoved(companyModelListMain.size()-1);
                                    isLoading = false;
                                }


                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(CompanyActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(CompanyActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){
            binding.progBar.setVisibility(View.GONE);
        }
    }

    private void search()
    {

        try {
            Api.getService(Tags.base_url)
                    .searchCompanyByName(query,1)
                    .enqueue(new Callback<CompanyDataModel>() {
                        @Override
                        public void onResponse(Call<CompanyDataModel> call, Response<CompanyDataModel> response) {
                            if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                            {
                                companyModelListMain.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();

                                if (companyModelListMain.size()>0)
                                {
                                    binding.tvNoSearch.setVisibility(View.GONE);
                                }else
                                {
                                    binding.tvNoSearch.setVisibility(View.VISIBLE);

                                }

                            }else
                            {
                                if (response.code() == 500) {
                                    Toast.makeText(CompanyActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(CompanyActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CompanyDataModel> call, Throwable t) {
                            try {

                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(CompanyActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(CompanyActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){

        }
    }


    private void loadMoreSearch(int page)
    {

        try {
            Api.getService(Tags.base_url)
                    .searchCompanyByName(query,page)
                    .enqueue(new Callback<CompanyDataModel>() {
                        @Override
                        public void onResponse(Call<CompanyDataModel> call, Response<CompanyDataModel> response) {
                            companyModelListMain.remove(companyModelListMain.size()-1);
                            adapter.notifyItemRemoved(companyModelListMain.size()-1);
                            isLoading = false;
                            if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                            {
                                companyModelListMain.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();

                                if (companyModelListMain.size()>0)
                                {
                                    binding.tvNoSearch.setVisibility(View.GONE);
                                }else
                                {
                                    binding.tvNoSearch.setVisibility(View.VISIBLE);

                                }

                            }else
                            {
                                if (response.code() == 500) {
                                    Toast.makeText(CompanyActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(CompanyActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CompanyDataModel> call, Throwable t) {
                            try {
                                if (companyModelListMain.get(companyModelListMain.size()-1)==null)
                                {
                                    companyModelListMain.remove(companyModelListMain.size()-1);
                                    adapter.notifyItemRemoved(companyModelListMain.size()-1);
                                    isLoading = false;
                                }
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(CompanyActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(CompanyActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){

        }
    }

    public void setItemData(CompanyModel companyModel) {
        Intent intent = new Intent(this, CompanyAdsActivity.class);
        intent.putExtra("company_data",companyModel);
        startActivity(intent);
    }
    @Override
    public void back() {
        finish();
    }


}
