package com.creative.share.apps.heragelkashed.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_sub_category.SubCategoryActivity;
import com.creative.share.apps.heragelkashed.databinding.AdTypeRowBinding;
import com.creative.share.apps.heragelkashed.models.AdTypeDataModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class AdTypeAdapter extends RecyclerView.Adapter<AdTypeAdapter.MyHolder> {

    private Context context;
    private List<AdTypeDataModel.AdTypeModel> adTypeModelList;
    private String lang;
    private SubCategoryActivity activity;


    public AdTypeAdapter(Context context, List<AdTypeDataModel.AdTypeModel> adTypeModelList) {
        this.context = context;
        this.adTypeModelList = adTypeModelList;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        this.activity = (SubCategoryActivity) context;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        AdTypeRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.ad_type_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        AdTypeDataModel.AdTypeModel adTypeModel = adTypeModelList.get(position);
        holder.binding.setLang(lang);
        holder.binding.setAdTypeModel(adTypeModel);
        holder.itemView.setOnClickListener(view -> {
            AdTypeDataModel.AdTypeModel adTypeModel1 = adTypeModelList.get(holder.getAdapterPosition());
            activity.setAdTypeData(adTypeModel1,holder.getAdapterPosition());

        });


    }

    @Override
    public int getItemCount() {
        return adTypeModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private AdTypeRowBinding binding;

        public MyHolder(@NonNull AdTypeRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
