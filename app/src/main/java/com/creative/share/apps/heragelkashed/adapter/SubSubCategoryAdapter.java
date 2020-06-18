package com.creative.share.apps.heragelkashed.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_sub_category.SubCategoryActivity;
import com.creative.share.apps.heragelkashed.databinding.AllDeptRowBinding;
import com.creative.share.apps.heragelkashed.databinding.SubSubCategoryRowBinding;
import com.creative.share.apps.heragelkashed.models.SubSubCategoryModel;
import com.creative.share.apps.heragelkashed.tags.Tags;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubSubCategoryAdapter extends RecyclerView.Adapter {
    private final int ITEM = 1;
    private final int DATA = 2;

    private Context context;
    private List<SubSubCategoryModel.SubCategories> subCategoriesList;
    private SubCategoryActivity activity;
    public SubSubCategoryAdapter(Context context, List<SubSubCategoryModel.SubCategories> subCategoriesList) {
        this.context = context;
        this.activity = (SubCategoryActivity) context;
        this.subCategoriesList = subCategoriesList;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == DATA) {
            SubSubCategoryRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.sub_sub_category_row, parent, false);
            return new MyHolder(binding);
        } else {
            AllDeptRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.all_dept_row, parent, false);
            return new AllDeptHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        SubSubCategoryModel.SubCategories subCategories = subCategoriesList.get(position);

        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            myHolder.binding.setSubCategory(subCategories);
            myHolder.binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

            Log.e("icon",subCategories.getIcon()+"__");

            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL_ADS_CATEGORY_ICON+subCategories.getIcon())).fit().into(myHolder.binding.image, new Callback() {
                @Override
                public void onSuccess() {
                    myHolder.binding.progBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });

            myHolder.itemView.setOnClickListener(view -> {
                SubSubCategoryModel.SubCategories subCategories1 = subCategoriesList.get(holder.getAdapterPosition());
                activity.setItemSubCategory(subCategories1);

            });

        } else if (holder instanceof AllDeptHolder) {
            AllDeptHolder allDeptHolder = (AllDeptHolder) holder;

            allDeptHolder.itemView.setOnClickListener(view -> {
                activity.setAllDept();

            });
        }


    }

    @Override
    public int getItemCount() {
        return subCategoriesList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private SubSubCategoryRowBinding binding;

        public MyHolder(@NonNull SubSubCategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class AllDeptHolder extends RecyclerView.ViewHolder {
        private AllDeptRowBinding binding;

        public AllDeptHolder(@NonNull AllDeptRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (subCategoriesList.get(position) == null) {
            return ITEM;
        } else {
            return DATA;
        }
    }



}
