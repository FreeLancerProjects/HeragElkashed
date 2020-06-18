package com.creative.share.apps.heragelkashed.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.heragelkashed.databinding.NavChildRowBinding;
import com.creative.share.apps.heragelkashed.models.MainCategoryDataModel;

import java.util.List;

public class SubCategoryNavChildAdapter extends RecyclerView.Adapter<SubCategoryNavChildAdapter.MyHolder> {

    private Context context;
    private List<MainCategoryDataModel.SubCategoryModel> subCategoryModelList;
    private HomeActivity activity;

    public SubCategoryNavChildAdapter(Context context, List<MainCategoryDataModel.SubCategoryModel> subCategoryModelList) {
        this.context = context;
        this.subCategoryModelList = subCategoryModelList;
        this.activity = (HomeActivity) context;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        NavChildRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.nav_child_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        MainCategoryDataModel.SubCategoryModel subCategoryModel = subCategoryModelList.get(position);
        holder.binding.setSubCategoryModel(subCategoryModel);

        holder.itemView.setOnClickListener(view -> {
            MainCategoryDataModel.SubCategoryModel subCategoryModel1 = subCategoryModelList.get(holder.getAdapterPosition());

            activity.setSubCategoryItem(subCategoryModel1);
        });
    }

    @Override
    public int getItemCount() {
        return subCategoryModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private NavChildRowBinding binding;

        public MyHolder(@NonNull NavChildRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
