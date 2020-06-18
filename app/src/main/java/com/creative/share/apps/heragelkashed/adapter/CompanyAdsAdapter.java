package com.creative.share.apps.heragelkashed.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_company_ads.CompanyAdsActivity;
import com.creative.share.apps.heragelkashed.databinding.AdRow1Binding;
import com.creative.share.apps.heragelkashed.databinding.LoadMoreRowBinding;
import com.creative.share.apps.heragelkashed.models.AdModel;

import java.util.List;

public class CompanyAdsAdapter extends RecyclerView.Adapter {
    private final int LOAD = 1;
    private final int DATA = 2;

    private Context context;
    private List<AdModel> adModelList;
    private CompanyAdsActivity activity;
    public CompanyAdsAdapter(Context context, List<AdModel> adModelList) {
        this.context = context;
        this.activity = (CompanyAdsActivity) context;
        this.adModelList = adModelList;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == DATA) {
            AdRow1Binding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.ad_row1, parent, false);
            return new MyHolder(binding);
        } else {
            LoadMoreRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.load_more_row, parent, false);
            return new ProgressHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        AdModel adModel = adModelList.get(position);

        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            myHolder.binding.setAdModel(adModel);


            myHolder.itemView.setOnClickListener(view -> {
                AdModel adModel1 = adModelList.get(holder.getAdapterPosition());

            });

        } else if (holder instanceof ProgressHolder) {
            ProgressHolder progressHolder = (ProgressHolder) holder;

            progressHolder.binding.progBar.setIndeterminate(true);
        }


    }

    @Override
    public int getItemCount() {
        return adModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private AdRow1Binding binding;

        public MyHolder(@NonNull AdRow1Binding binding) {
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
        if (adModelList.get(position) == null) {
            return LOAD;
        } else {
            return DATA;
        }
    }



}
