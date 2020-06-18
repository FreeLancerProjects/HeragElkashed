package com.creative.share.apps.heragelkashed.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_home.fragments.fragment_company.Fragment_Companies_Feature;
import com.creative.share.apps.heragelkashed.databinding.CompanyFeatureRowBinding;
import com.creative.share.apps.heragelkashed.models.SubCategoryDataModel;

import java.util.List;

public class CompanyFeatureAdapter extends RecyclerView.Adapter<CompanyFeatureAdapter.MyHolder> {

    private Context context;
    private List<SubCategoryDataModel.SubCategoryModel> subCategoryModelList;
    private Fragment_Companies_Feature fragment_companiesFeature;

    public CompanyFeatureAdapter(Context context, List<SubCategoryDataModel.SubCategoryModel> subCategoryModelList, Fragment_Companies_Feature fragment_companiesFeature) {
        this.context = context;
        this.subCategoryModelList = subCategoryModelList;
        this.fragment_companiesFeature = fragment_companiesFeature;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CompanyFeatureRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.company_feature_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        SubCategoryDataModel.SubCategoryModel subCategoryModel = subCategoryModelList.get(position);
        holder.binding.setSubCategoryModel(subCategoryModel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubCategoryDataModel.SubCategoryModel subCategoryModel = subCategoryModelList.get(position);
                fragment_companiesFeature.setItem(subCategoryModel);
            }
        });


    }

    @Override
    public int getItemCount() {
        return subCategoryModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private CompanyFeatureRowBinding binding;

        public MyHolder(@NonNull CompanyFeatureRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
