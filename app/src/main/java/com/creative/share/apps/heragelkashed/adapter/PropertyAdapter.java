package com.creative.share.apps.heragelkashed.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.databinding.PropertyRowBinding;
import com.creative.share.apps.heragelkashed.models.AdModel;

import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.MyHolder> {

    private Context context;
    private List<AdModel.AdDetails> adDetailsList;
    private int index=0;
    private int [] colors = {R.color.colorPrimary,R.color.color1,R.color.color2,R.color.color3,R.color.color4,R.color.color5,R.color.color6,R.color.color7,R.color.color8,R.color.color9};


    public PropertyAdapter(Context context, List<AdModel.AdDetails> adDetailsList) {
        this.context = context;
        this.adDetailsList = adDetailsList;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        PropertyRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.property_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        AdModel.AdDetails adDetails = adDetailsList.get(position);
        holder.binding.setPropertyModel(adDetails);

        if (index<colors.length-1)
        {
            holder.binding.tvTitle.setTextColor(ContextCompat.getColor(context,colors[index]));
            index++;

        }else
        {

            index =0;
            holder.binding.tvTitle.setTextColor(ContextCompat.getColor(context,colors[index]));

        }
    }

    @Override
    public int getItemCount() {
        return adDetailsList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private PropertyRowBinding binding;

        public MyHolder(@NonNull PropertyRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
