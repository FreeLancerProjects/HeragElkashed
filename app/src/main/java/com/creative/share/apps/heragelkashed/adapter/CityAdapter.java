package com.creative.share.apps.heragelkashed.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_add_ads.AddAdsActivity;
import com.creative.share.apps.heragelkashed.databinding.CityRowBinding;
import com.creative.share.apps.heragelkashed.models.FormDataModel;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyHolder> {

    private Context context;
    private List<FormDataModel.CityModel> list;
    private AddAdsActivity activity;

    public CityAdapter(Context context, List<FormDataModel.CityModel> list) {
        this.context = context;
        this.list = list;
        activity = (AddAdsActivity) context;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CityRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.city_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        FormDataModel.CityModel cityModel = list.get(position);
        holder.binding.setCityModel(cityModel);
        holder.itemView.setOnClickListener(view -> {
            FormDataModel.CityModel cityModel1 = list.get(holder.getAdapterPosition());
            activity.setCityModel(cityModel1);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private CityRowBinding binding;

        public MyHolder(@NonNull CityRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
