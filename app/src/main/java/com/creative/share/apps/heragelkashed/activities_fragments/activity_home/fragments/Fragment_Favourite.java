package com.creative.share.apps.heragelkashed.activities_fragments.activity_home.fragments;

import android.app.ProgressDialog;
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
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.heragelkashed.adapter.FavoriteAdapter;
import com.creative.share.apps.heragelkashed.databinding.FragmentFavouriteBinding;
import com.creative.share.apps.heragelkashed.models.AdModel;
import com.creative.share.apps.heragelkashed.models.FavoriteDataModel;
import com.creative.share.apps.heragelkashed.models.UserModel;
import com.creative.share.apps.heragelkashed.preferences.Preferences;
import com.creative.share.apps.heragelkashed.remote.Api;
import com.creative.share.apps.heragelkashed.share.Common;
import com.creative.share.apps.heragelkashed.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Favourite extends Fragment {
    private HomeActivity activity;
    private FragmentFavouriteBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private int current_page=1;
    private boolean isLoading = false;
    private FavoriteAdapter adapter;
    private List<AdModel> adModelList;

    public static Fragment_Favourite newInstance() {
        return new Fragment_Favourite();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        adModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new GridLayoutManager(activity, 2));
        adapter = new FavoriteAdapter(activity,adModelList,this);
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
        getFavoriteAds();

    }

    public void getFavoriteAds() {

        userModel = preferences.getUserData(activity);

        try {
            Api.getService(Tags.base_url)
                    .getFavoriteAds(userModel.getId(),1)
                    .enqueue(new Callback<FavoriteDataModel>() {
                        @Override
                        public void onResponse(Call<FavoriteDataModel> call, Response<FavoriteDataModel> response) {
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
                        public void onFailure(Call<FavoriteDataModel> call, Throwable t) {
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
                    .getFavoriteAds(userModel.getId(),page)
                    .enqueue(new Callback<FavoriteDataModel>() {
                        @Override
                        public void onResponse(Call<FavoriteDataModel> call, Response<FavoriteDataModel> response) {
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
                        public void onFailure(Call<FavoriteDataModel> call, Throwable t) {
                            try {
                                if (adModelList.get(adModelList.size()-1)==null)
                                {
                                    adModelList.remove(adModelList.size()-1);
                                    adapter.notifyItemRemoved(adModelList.size()-1);
                                    isLoading = false;
                                }


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



    public void deleteFavorite(AdModel adModel1, int pos)
    {

        userModel = preferences.getUserData(activity);

        ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .addFavoriteAds(userModel.getId(),adModel1.getId())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()&&response.body()!=null)
                            {
                                adModelList.remove(pos);
                                adapter.notifyItemRemoved(pos);

                                if (adModelList.size()==0)
                                {
                                    binding.tvNoAds.setVisibility(View.VISIBLE);
                                }else
                                    {
                                        binding.tvNoAds.setVisibility(View.GONE);

                                    }
                                Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
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
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {


                                dialog.dismiss();
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
            dialog.dismiss();

        }
    }
}
