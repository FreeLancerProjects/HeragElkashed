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
import com.creative.share.apps.heragelkashed.activities_fragments.activity_home.fragments.Fragment_Favourite;
import com.creative.share.apps.heragelkashed.databinding.FavouriteRowBinding;
import com.creative.share.apps.heragelkashed.databinding.LoadMoreRowBinding;
import com.creative.share.apps.heragelkashed.models.AdModel;
import com.creative.share.apps.heragelkashed.tags.Tags;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter {
    private final int LOAD = 1;
    private final int DATA = 2;

    private Context context;
    private List<AdModel> adModelList;
    private Fragment_Favourite fragment_favourite;
    public FavoriteAdapter(Context context, List<AdModel> adModelList,Fragment_Favourite fragment_favourite) {
        this.context = context;
        this.adModelList = adModelList;
        this.fragment_favourite = fragment_favourite;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == DATA) {
            FavouriteRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.favourite_row, parent, false);
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

            myHolder.binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL_ADS+adModel.getMain_image())).fit().into(myHolder.binding.image, new Callback() {
                @Override
                public void onSuccess() {
                    myHolder.binding.progBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });
            myHolder.itemView.setOnClickListener(view -> {
                AdModel adModel1 = adModelList.get(holder.getAdapterPosition());
                fragment_favourite.deleteFavorite(adModel1,holder.getAdapterPosition());
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
        private FavouriteRowBinding binding;

        public MyHolder(@NonNull FavouriteRowBinding binding) {
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
