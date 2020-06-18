package com.creative.share.apps.heragelkashed.activities_fragments.activity_company_ads;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.adapter.CompanyAdsAdapter;
import com.creative.share.apps.heragelkashed.databinding.ActivityCompanyAdsBinding;
import com.creative.share.apps.heragelkashed.interfaces.Listeners;
import com.creative.share.apps.heragelkashed.language.LanguageHelper;
import com.creative.share.apps.heragelkashed.models.AdModel;
import com.creative.share.apps.heragelkashed.models.CompanyAdDataModel;
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

public class CompanyAdsActivity extends AppCompatActivity implements Listeners.BackListener ,Listeners.CompanyActionListener{
    private ActivityCompanyAdsBinding binding;
    private List<AdModel> adModelList;
    private String lang;
    private UserModel userModel;
    private Preferences preferences;
    private CompanyModel companyModel;
    private CompanyAdsAdapter adapter;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company_ads);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("company_data"))
        {
            companyModel = (CompanyModel) intent.getSerializableExtra("company_data");


        }
    }

    private void initView()
    {
        adModelList = new ArrayList<>();


        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setListener(this);
        binding.setLang(lang);
        binding.setCompanyModel(companyModel);

        adapter = new CompanyAdsAdapter(this,adModelList);
        binding.recView.setLayoutManager(new GridLayoutManager(this,2));
        binding.recView.setAdapter(adapter);




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
                        adModelList.add(null);
                        adapter.notifyItemInserted(adModelList.size()-1);
                        int page = current_page+1;
                        loadMore(page);

                    }

                }
            }
        });
        getAds();


    }

    private void getAds() {
        try {

            Api.getService(Tags.base_url)
                    .getCompanyAds(companyModel.getId(),1)
                    .enqueue(new Callback<CompanyAdDataModel>() {
                        @Override
                        public void onResponse(Call<CompanyAdDataModel> call, Response<CompanyAdDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                            {
                                adModelList.clear();
                                adModelList.addAll(response.body().getData());
                                if (adModelList.size()>0)
                                {
                                    adapter.notifyDataSetChanged();
                                    binding.tvNoAds.setVisibility(View.GONE);
                                }else
                                {
                                    binding.tvNoAds.setVisibility(View.VISIBLE);

                                }
                            }else
                            {
                                if (response.code() == 500) {
                                    Toast.makeText(CompanyAdsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(CompanyAdsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CompanyAdDataModel> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(CompanyAdsActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(CompanyAdsActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void loadMore(int page)
    {
        try {

            Api.getService(Tags.base_url)
                    .getCompanyAds(companyModel.getId(),page)
                    .enqueue(new Callback<CompanyAdDataModel>() {
                        @Override
                        public void onResponse(Call<CompanyAdDataModel> call, Response<CompanyAdDataModel> response) {
                            adModelList.remove(adModelList.size()-1);
                            adapter.notifyItemRemoved(adModelList.size()-1);
                            isLoading = false;

                            if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                            {
                                adModelList.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();
                                current_page = response.body().getCurrent_page();
                            }else
                            {
                                if (response.code() == 500) {
                                    Toast.makeText(CompanyAdsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(CompanyAdsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CompanyAdDataModel> call, Throwable t) {
                            try {
                                if (adModelList.get(adModelList.size()-1)==null)
                                {
                                    adModelList.remove(adModelList.size()-1);
                                    //adapter.notifyItemRemoved(adModelList.size()-1);
                                    isLoading = false;
                                }


                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(CompanyAdsActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(CompanyAdsActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){
            binding.progBar.setVisibility(View.GONE);
        }
    }


    @Override
    public void back() {
        finish();
    }

    @Override
    public void call() {
        if (companyModel.getPhone()!=null&&!companyModel.getPhone().isEmpty())
        {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+companyModel.getPhone()));
            startActivity(intent);
        }else
            {
                Toast.makeText(this, getString(R.string.no_phone), Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void sms() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:"+companyModel.getPhone()));
        startActivity(intent);
    }

    @Override
    public void location() {
        if (companyModel!=null)
        {
            Log.e("id",companyModel.getId()+"_");
            String geo = "http://maps.google.com/maps?q=loc:"+companyModel.getLatitude()+","+companyModel.getLongitude()+"&z=17";
            Uri uri = Uri.parse(geo);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }
        }

    }
}
