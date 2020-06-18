package com.creative.share.apps.heragelkashed.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_home.fragments.Fragment_Main;
import com.creative.share.apps.heragelkashed.databinding.HomeSubCategoryRowBinding;
import com.creative.share.apps.heragelkashed.models.MainCategoryDataModel;
import com.creative.share.apps.heragelkashed.tags.Tags;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeSubCategoryAdapter extends RecyclerView.Adapter<HomeSubCategoryAdapter.MyHolder> {

    private Context context;
    private Fragment_Main fragment_main;
    private List<MainCategoryDataModel.SubCategoryModel> subCategoryModelList;

    public HomeSubCategoryAdapter(Context context, Fragment_Main fragment_main, List<MainCategoryDataModel.SubCategoryModel> subCategoryModelList) {
        this.context = context;
        this.fragment_main = fragment_main;
        this.subCategoryModelList = subCategoryModelList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        HomeSubCategoryRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.home_sub_category_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        MainCategoryDataModel.SubCategoryModel subCategoryModel = subCategoryModelList.get(position);
        holder.binding.setSubCategoryModel(subCategoryModel);
        holder.binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL_ADS_CATEGORY+subCategoryModel.getImage())).fit().into(holder.binding.image, new Callback() {
            @Override
            public void onSuccess() {
                holder.binding.progBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });

        holder.itemView.setOnClickListener(view -> {
            MainCategoryDataModel.SubCategoryModel subCategoryModel1 = subCategoryModelList.get(holder.getAdapterPosition());
            fragment_main.setSubCategoryItem(subCategoryModel1);
        });
    }

    @Override
    public int getItemCount() {
        return subCategoryModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private HomeSubCategoryRowBinding binding;

        public MyHolder(@NonNull HomeSubCategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
