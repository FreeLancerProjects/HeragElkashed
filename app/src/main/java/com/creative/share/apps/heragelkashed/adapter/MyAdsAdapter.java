package com.creative.share.apps.heragelkashed.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_home.fragments.Fragment_My_Adversiment;
import com.creative.share.apps.heragelkashed.databinding.LoadMoreRowBinding;
import com.creative.share.apps.heragelkashed.databinding.MyAdRowBinding;
import com.creative.share.apps.heragelkashed.models.AdModel;

import java.util.List;

public class MyAdsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int DATA = 1;
    private final int LOAD = 2;

    private Context context;
    private List<AdModel>  adModelList;
    private Fragment_My_Adversiment fragment;


    public MyAdsAdapter(Context context, List<AdModel> adModelList, Fragment_My_Adversiment fragment) {
        this.context = context;
        this.adModelList = adModelList;
        this.fragment = fragment;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==DATA) {
            MyAdRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.my_ad_row, parent, false);
            return new Holder1(binding);


        }else
            {
                LoadMoreRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.load_more_row,parent,false);
                return new LoadHolder(binding);
            }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AdModel adModel = adModelList.get(position);

        if (holder instanceof Holder1)
        {
            Holder1 holder1 = (Holder1) holder;
            holder1.binding.setAdModel(adModel);


            holder1.itemView.setOnClickListener(view -> {
                AdModel adModel1 = adModelList.get(holder1.getAdapterPosition());
                fragment.setItemData(adModel1);

            });

        }else if (holder instanceof LoadHolder)
        {
            LoadHolder loadHolder = (LoadHolder) holder;
            loadHolder.binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            loadHolder.binding.progBar.setIndeterminate(true);

        }


    }

    @Override
    public int getItemCount() {
        return adModelList.size();
    }

    public class Holder1 extends RecyclerView.ViewHolder {
        private MyAdRowBinding binding;

        public Holder1(@NonNull MyAdRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    public class LoadHolder extends RecyclerView.ViewHolder {
        private LoadMoreRowBinding binding;

        public LoadHolder(@NonNull LoadMoreRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @Override
    public int getItemViewType(int position) {

        if (adModelList.get(position)==null)
        {
            return LOAD;
        }else
        {
            return DATA;
        }
    }
}
