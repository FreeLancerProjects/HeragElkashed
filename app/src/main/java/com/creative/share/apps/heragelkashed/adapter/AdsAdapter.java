package com.creative.share.apps.heragelkashed.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_sub_sub_categories_details.FragmentSubSubCategoryDetails;
import com.creative.share.apps.heragelkashed.databinding.AdRow1Binding;
import com.creative.share.apps.heragelkashed.databinding.AdRow2Binding;
import com.creative.share.apps.heragelkashed.databinding.LoadMoreRowBinding;
import com.creative.share.apps.heragelkashed.models.AdModel;
import com.creative.share.apps.heragelkashed.tags.Tags;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int DATA1 = 1;
    private final int DATA2 = 2;
    private final int LOAD = 3;

    private Context context;
    private List<AdModel>  adModelList;
    private int type =1;
    private FragmentSubSubCategoryDetails fragment;


    public AdsAdapter(Context context, List<AdModel> adModelList,FragmentSubSubCategoryDetails fragment) {
        this.context = context;
        this.adModelList = adModelList;
        this.fragment = fragment;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==DATA1)
        {
            AdRow1Binding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.ad_row1,parent,false);
            return new Holder1(binding);


        }else if (viewType==DATA2)
        {
            AdRow2Binding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.ad_row2,parent,false);
            return new Holder2(binding);

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
            holder1.binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL_ADS+adModel.getMain_image())).into(holder1.binding.image, new Callback() {
                @Override
                public void onSuccess() {
                    holder1.binding.progBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });

            holder1.itemView.setOnClickListener(view -> {
                AdModel adModel1 = adModelList.get(holder1.getAdapterPosition());
                fragment.setItemData(adModel1);

            });

        }else if (holder instanceof Holder2)
        {
            Holder2 holder2 = (Holder2) holder;
            holder2.binding.setAdModel(adModel);
            holder2.binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL_ADS+adModel.getMain_image())).into(holder2.binding.image, new Callback() {
                @Override
                public void onSuccess() {
                    holder2.binding.progBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });

            holder2.itemView.setOnClickListener(view -> {
                AdModel adModel1 = adModelList.get(holder2.getAdapterPosition());
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
        private AdRow1Binding binding;

        public Holder1(@NonNull AdRow1Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class Holder2 extends RecyclerView.ViewHolder {
        private AdRow2Binding binding;

        public Holder2(@NonNull AdRow2Binding binding) {
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

    public void updateType(int type)
    {
        this.type = type;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (type ==1)
        {
            if (adModelList.get(position)==null)
            {
                return LOAD;
            }else
                {
                    return DATA1;
                }
        }else if (type ==2)
        {
            if (adModelList.get(position)==null)
            {
                return LOAD;
            }else
            {
                return DATA2;
            }
        }

        return DATA1;
    }
}
