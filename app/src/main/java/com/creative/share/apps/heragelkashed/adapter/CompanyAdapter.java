package com.creative.share.apps.heragelkashed.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_companies.CompanyActivity;
import com.creative.share.apps.heragelkashed.databinding.CompanyRowBinding;
import com.creative.share.apps.heragelkashed.databinding.LoadMoreRowBinding;
import com.creative.share.apps.heragelkashed.models.CompanyModel;

import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter {
    private final int LOAD = 1;
    private final int DATA = 2;

    private Context context;
    private List<CompanyModel> companyModelList;
    private CompanyActivity activity;
    public CompanyAdapter(Context context, List<CompanyModel> companyModelList) {
        this.context = context;
        this.activity = (CompanyActivity) context;
        this.companyModelList = companyModelList;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == DATA) {
            CompanyRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.company_row, parent, false);
            return new MyHolder(binding);
        } else {
            LoadMoreRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.load_more_row, parent, false);
            return new ProgressHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        CompanyModel companyModel = companyModelList.get(position);

        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            myHolder.binding.setCompanyModel(companyModel);


            myHolder.itemView.setOnClickListener(view -> {
                CompanyModel companyModel2 = companyModelList.get(holder.getAdapterPosition());
                activity.setItemData(companyModel2);
            });

        } else if (holder instanceof ProgressHolder) {
            ProgressHolder progressHolder = (ProgressHolder) holder;

            progressHolder.binding.progBar.setIndeterminate(true);
        }


    }

    @Override
    public int getItemCount() {
        return companyModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private CompanyRowBinding binding;

        public MyHolder(@NonNull CompanyRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class ProgressHolder extends RecyclerView.ViewHolder {
        private LoadMoreRowBinding binding;

        public ProgressHolder(@NonNull LoadMoreRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (companyModelList.get(position) == null) {
            return LOAD;
        } else {
            return DATA;
        }
    }



}
