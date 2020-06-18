package com.creative.share.apps.heragelkashed.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_search.SearchActivity;
import com.creative.share.apps.heragelkashed.databinding.SearchRowBinding;
import com.creative.share.apps.heragelkashed.models.AdModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyHolder> {

    private Context context;
    private List<AdModel> adModelList;
    private String lang;
    private SearchActivity activity;


    public SearchAdapter(Context context, List<AdModel> adModelList) {
        this.context = context;
        this.activity = (SearchActivity) context;
        this.adModelList = adModelList;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SearchRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.search_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        AdModel adModel = adModelList.get(position);
        holder.binding.setAdModel(adModel);
        holder.binding.setLang(lang);
        holder.itemView.setOnClickListener(view -> {
            AdModel adModel1 = adModelList.get(holder.getAdapterPosition());
            activity.setAdData(adModel1);

        });


    }

    @Override
    public int getItemCount() {
        return adModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private SearchRowBinding binding;

        public MyHolder(@NonNull SearchRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
